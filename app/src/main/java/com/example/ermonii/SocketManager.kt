package com.example.ermonii

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class SocketManager(
    private val userId: String,
    private val onMessageReceived: (String, String) -> Unit,
    private val onError: (String) -> Unit
                   ) {
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    private val senderExecutor = Executors.newSingleThreadExecutor()
    private val receiverExecutor = Executors.newSingleThreadExecutor()
    private val connectionExecutor = Executors.newSingleThreadExecutor()

    private val isConnected = AtomicBoolean(false)
    private val reconnectDelay = AtomicLong(5000L)

    init {
        connectToServer()
    }

    private fun connectToServer() {
        connectionExecutor.execute {
            try {
                Log.d("SocketManager", "🔗 Connecting to server...")
                resetConnection()

                Socket("10.0.2.2", 12345).apply {
                    keepAlive = true
                    soTimeout = 15000
                    socket = this
                }

                output = PrintWriter(
                    OutputStreamWriter(
                        socket!!.getOutputStream(),
                        StandardCharsets.UTF_8
                                      ),
                    true
                                    )

                input = BufferedReader(
                    InputStreamReader(
                        socket!!.getInputStream(),
                        StandardCharsets.UTF_8
                                     )
                                      )

                // Enviar ID de usuario inicial
                output!!.apply {
                    println(userId)
                    flush()
                }

                isConnected.set(true)
                reconnectDelay.set(5000L)
                Log.d("SocketManager", "✅ Connection established")
                startMessageListener()

            } catch (e: Exception) {
                handleNetworkError("Connection error: ${e.message}")
            }
        }
    }

    private fun startMessageListener() {
        receiverExecutor.execute {
            while (isConnected.get()) {
                try {
                    val message = input?.readLine() ?: throw Exception("Null message received")
                    Log.d("SocketManager", "📥 Received: $message")

                    when {
                        message.startsWith("MENSAJE|") -> handleMessage(message)
                        message.startsWith("ERROR|") -> handleServerError(message)
                        else -> handleUnknownMessage(message)
                    }
                } catch (e: Exception) {
                    handleNetworkError("Listener error: ${e.message}")
                }
            }
        }
    }

    private fun handleMessage(rawMessage: String) {
        try {
            val parts = rawMessage.split("|", limit = 3)
            if (parts.size != 3) throw Exception("Invalid message format")

            val remitente = parts[1]
            val contenido = parts[2]
            onMessageReceived(remitente, contenido)

        } catch (e: Exception) {
            onError("Failed to parse message: ${e.message}")
        }
    }

    private fun handleServerError(rawMessage: String) {
        val error = rawMessage.removePrefix("ERROR|")
        Log.e("SocketManager", "⛔ Server error: $error")
        onError(error)
    }

    private fun handleUnknownMessage(message: String) {
        Log.w("SocketManager", "⚠️ Unknown message type: $message")
        onError("Received unknown message format")
    }

    fun sendMessage(destinatario: String, contenido: String) {
        senderExecutor.execute {
            if (!isConnected.get()) {
                onError("Not connected to server")
                return@execute
            }

            try {
                output?.apply {
                    println("$destinatario|$contenido")
                    flush()
                }
                Log.d("SocketManager", "📤 Sent to $destinatario: $contenido")
            } catch (e: Exception) {
                handleNetworkError("Send error: ${e.message}")
            }
        }
    }

    private fun handleNetworkError(error: String) {
        Log.e("SocketManager", "❌ $error")
        onError(error)
        reconnect()
    }

    internal fun reconnect() {
        if (!isConnected.compareAndSet(true, false)) return

        Log.d("SocketManager", "🔁 Reconnecting in ${reconnectDelay.get()/1000}s...")
        resetConnection()

        connectionExecutor.execute {
            try {
                Thread.sleep(reconnectDelay.get())
                reconnectDelay.set(minOf(reconnectDelay.get() * 2, 30000L)) // Backoff exponencial
                connectToServer()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun resetConnection() {
        try {
            input?.close()
            output?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e("SocketManager", "Error closing resources: ${e.message}")
        }

        input = null
        output = null
        socket = null
    }

    fun disconnect() {
        isConnected.set(false)
        resetConnection()
        Log.d("SocketManager", "🔌 Disconnected")
    }
}