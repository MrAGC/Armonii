package com.example.ermonii

import android.util.Log
import firebase.com.protolitewrapper.BuildConfig
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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
    private val SERVER_IP = "10.0.1.192"
    private val SERVER_PORT = 12345

    internal val isConnected = AtomicBoolean(false)
    private val reconnectDelay = AtomicLong(5000L)
    private val messageQueue = ConcurrentLinkedQueue<Pair<String, String>>()
    private val heartbeatExecutor = Executors.newSingleThreadScheduledExecutor()


    init {
        connectToServer()
        startHeartbeat()
    }

    private fun connectToServer() {
        connectionExecutor.execute {
            try {
                resetConnection()
                Log.d("SocketManager", "🔗 Connecting...")

                Socket().apply {
                    connect(InetSocketAddress(SERVER_IP, SERVER_PORT), 5000)
                    soTimeout = 10000
                    keepAlive = true
                    socket = this
                }

                output = PrintWriter(
                    OutputStreamWriter(
                        socket!!.getOutputStream(),
                        StandardCharsets.UTF_8
                                      ), true
                                    )

                input = BufferedReader(
                    InputStreamReader(
                        socket!!.getInputStream(),
                        StandardCharsets.UTF_8
                                     )
                                      )

                sendRawMessage(userId)
                isConnected.set(true)
                processMessageQueue()
                startMessageListener()
                Log.d("SocketManager", "✅ Connected")

            } catch (e: Exception) {
                handleNetworkError("Connection error: ${e.message}")
            }
        }
    }

    private fun startMessageListener() {
        receiverExecutor.execute {
            while (isConnected.get()) {
                try {
                    val message = input?.readLine() ?: throw IOException("Connection closed")
                    Log.d("SocketManager", "📥 Received: $message")

                    when {
                        message.startsWith("MENSAJE|") -> handleMessage(message)
                        message.startsWith("ERROR|") -> handleServerError(message)
                        message == "PONG" -> handlePong()
                        else -> handleUnknownMessage(message)
                    }
                } catch (e: Exception) {
                    handleNetworkError("Listener error: ${e.message}")
                }
            }
        }
    }

    private fun startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate({
                                                  if (isConnected.get()) {
                                                      sendRawMessage("PING")
                                                      Log.d("SocketManager", "❤️ Sent heartbeat")
                                                  }
                                              }, 0, 15, TimeUnit.SECONDS)
    }
    private fun handlePong() {
        Log.d("SocketManager", "❤️ Received PONG")
    }

    private fun handleMessage(rawMessage: String) {
        try {
            val parts = rawMessage.split("|", limit = 3)
            if (parts.size != 3) throw Exception("Invalid message format!")

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
                messageQueue.add(Pair(destinatario, contenido))
                reconnect()
                return@execute
            }

            try {
                sendRawMessage("MENSAJE|$destinatario|$contenido")
                Log.d("SocketManager", "📤 Sent to $destinatario: $contenido")
            } catch (e: Exception) {
                messageQueue.add(Pair(destinatario, contenido))
                handleNetworkError("Send error: ${e.message}")
            }
        }
    }

    private fun processMessageQueue() {
        senderExecutor.execute {
            while (isConnected.get() && messageQueue.isNotEmpty()) {
                val (destinatario, contenido) = messageQueue.poll()
                sendMessage(destinatario, contenido)
            }
        }
    }

    private fun sendRawMessage(message: String) {
        output?.apply {
            println(message)
            flush()
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