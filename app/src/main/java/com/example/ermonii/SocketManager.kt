package com.example.ermonii

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
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

data class MessageDto(
    @SerializedName("type") val type: String,
    @SerializedName("sender") val sender: String? = null,
    @SerializedName("recipient") val recipient: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("error") val error: String? = null
                     )

class SocketManager(
    private val userId: String,
    private val onMessageReceived: (MessageDto) -> Unit,
    private val onError: (String) -> Unit
                   ) {
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    private val senderExecutor = Executors.newSingleThreadExecutor()
    private val receiverExecutor = Executors.newSingleThreadExecutor()
    private val connectionExecutor = Executors.newSingleThreadExecutor()
    private val SERVER_IP = "10.0.1.41"
    private val SERVER_PORT = 12345

    internal val isConnected = AtomicBoolean(false)
    private val reconnectDelay = AtomicLong(5000L)
    private val messageQueue = ConcurrentLinkedQueue<MessageDto>()
    private val heartbeatExecutor = Executors.newSingleThreadScheduledExecutor()
    private val gson = Gson()

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
                    soTimeout = 30000
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

                sendMessageDto(MessageDto(
                    type = "AUTH",
                    sender = userId
                                         ))

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
                    val jsonMessage = input?.readLine() ?: throw IOException("Connection closed")
                    Log.d("SocketManager", "📥 Received: $jsonMessage")

                    val message = gson.fromJson(jsonMessage, MessageDto::class.java)
                    when (message.type) {
                        "MESSAGE" -> handleMessage(message)
                        "ERROR" -> handleError(message)
                        "HEARTBEAT" -> handleHeartbeat(message)
                        else -> handleUnknownMessage(message)
                    }
                } catch (e: Exception) {
                    if (isConnected.get()) {
                        handleNetworkError("Listener error: ${e.message}")
                    }
                }
            }
        }
    }

    private fun startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate({
                                                  if (isConnected.get()) {
                                                      sendMessageDto(MessageDto(
                                                          type = "HEARTBEAT",
                                                          content = "PING"
                                                                               ))
                                                      Log.d("SocketManager", "❤️ Sent heartbeat")
                                                  }
                                              }, 0, 15, TimeUnit.SECONDS)
    }

    private fun handleMessage(message: MessageDto) {
        if (message.sender == null || message.content == null) {
            onError("Invalid message format")
            return
        }
        onMessageReceived(message)
    }

    private fun handleError(message: MessageDto) {
        val error = message.error ?: "Unknown error"
        Log.e("SocketManager", "⛔ Server error: $error")
        onError(error)
    }

    private fun handleHeartbeat(message: MessageDto) {
        if (message.content == "PONG") {
            Log.d("SocketManager", "❤️ Received PONG")
        }
    }

    private fun handleUnknownMessage(message: MessageDto) {
        Log.w("SocketManager", "⚠️ Unknown message type: ${message.type}")
        onError("Received unknown message type")
    }

    fun sendChatMessage(recipient: String, content: String) {
        val message = MessageDto(
            type = "MESSAGE",
            sender = userId,
            recipient = recipient,
            content = content
                                )

        senderExecutor.execute {
            if (!isConnected.get()) {
                messageQueue.add(message)
                reconnect()
                return@execute
            }

            try {
                sendMessageDto(message)
                Log.d("SocketManager", "📤 Sent to $recipient: $content")
            } catch (e: Exception) {
                messageQueue.add(message)
                handleNetworkError("Send error: ${e.message}")
            }
        }
    }

    private fun sendMessageDto(message: MessageDto) {
        val json = gson.toJson(message)
        output?.apply {
            println(json)
            flush()
        }
    }

    private fun processMessageQueue() {
        senderExecutor.execute {
            while (isConnected.get() && messageQueue.isNotEmpty()) {
                val message = messageQueue.poll()
                sendMessageDto(message)
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
                reconnectDelay.set(minOf(reconnectDelay.get() * 2, 30000L))
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