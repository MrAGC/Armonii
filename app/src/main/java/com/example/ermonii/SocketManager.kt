package com.example.ermonii

import android.util.Log
import com.example.ermonii.fragmentMusico.ChatFragmentMusico
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors

class SocketManager(
    private val userId: String,
    private val onMessageReceived: (String, String) -> Unit
                   ) {
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var isConnected = false

    init {
        connectToServer()
    }

    private fun connectToServer() {
        executor.execute {
            try {
                // Conexión sin timeout para evitar desconexiones innecesarias
                socket = Socket("10.0.2.2", 12345)
                output = PrintWriter(socket!!.getOutputStream(), true)
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                output!!.println(userId)
                isConnected = true
                Log.d("SocketManager", "Conexión exitosa")
                startMessageListener()
            } catch (e: Exception) {
                Log.e("SocketManager", "Error de conexión: ${e.message}")
                reconnect()
            }
        }
    }


    private fun startMessageListener() {
        executor.execute {
            while (isConnected) {
                try {
                    input?.readLine()?.let { rawMessage ->
                        Log.d("SocketManager", "Mensaje recibido: $rawMessage")

                        when {
                            rawMessage.startsWith("MESSAGE|") -> {
                                Log.d("SocketManager", "Mensaje válido: $rawMessage")
                                val parts = rawMessage.split("|", limit = 3)
                                if (parts.size == 3) {
                                    val tipo = parts[0] // "MESSAGE"
                                    val remitente = parts[1]
                                    val contenido = parts[2]

                                    onMessageReceived(remitente, contenido)
                                }
                                else {
                                    Log.e("SocketManager", "Formato MESSAGE inválido: $rawMessage")
                                }
                            }
                            else -> {
                                Log.e("SocketManager", "Tipo de mensaje desconocido: $rawMessage")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error recibiendo mensajes: ${e.message}")
                    reconnect()
                }
            }
        }
    }

    fun sendMessage(destinatario: String, contenido: String) {
        if (isConnected) {
            executor.execute {
                try {
                    // Formato: "destinatario|contenido"
                    val mensaje = "$destinatario|$contenido"
                    output?.println(mensaje)
                    Log.d("SocketManager", "Mensaje enviado: $mensaje")
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error enviando mensaje: ${e.message}")
                }
            }
        }
    }

    private fun reconnect() {
        desconectar()
        executor.execute {
            Thread.sleep(5000)
            connectToServer()
        }
    }

    fun desconectar() {
        executor.execute {
            try {
                isConnected = false
                input?.close()
                output?.close()
                socket?.close()
                Log.d("SocketManager", "Desconexión exitosa")
            } catch (e: Exception) {
                Log.e("SocketManager", "Error en desconexión: ${e.message}")
            }
        }
    }
}