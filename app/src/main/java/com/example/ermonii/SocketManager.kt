package com.example.ermonii

import android.util.Log
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
        conectar()
    }

    private fun conectar() {
        executor.execute {
            try {
                socket = Socket("10.0.2.2", 12345).apply {
                    soTimeout = 5000 // Timeout de 5 segundos
                }
                output = PrintWriter(socket!!.getOutputStream(), true)
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                // Enviar solo el ID de usuario al conectarse
                output!!.println(userId)
                isConnected = true
                escucharMensajes()

            } catch (e: Exception) {
                Log.e("SocketManager", "Error de conexión: ${e.message}")
                reiniciarConexion()
            }
        }
    }

    private fun escucharMensajes() {
        executor.execute {
            while (isConnected) {
                try {
                    val mensaje = input?.readLine() ?: break
                    Log.d("SocketManager", "Mensaje recibido: $mensaje")

                    // Formato esperado: "remitente|contenido"
                    if (mensaje.contains("|")) {
                        val partes = mensaje.split("|", limit = 2)
                        val remitente = partes[0]
                        val contenido = partes[1]
                        onMessageReceived(remitente, contenido)
                    } else if (mensaje.startsWith("ERROR")) {
                        Log.e("SocketManager", "Error del servidor: $mensaje")
                    }
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error recibiendo mensaje: ${e.message}")
                    reiniciarConexion()
                }
            }
        }
    }

    fun sendMessage(destinatario: String, mensaje: String) {
        if (isConnected) {
            executor.execute {
                try {
                    // Formato correcto: "destinatario|mensaje"
                    val mensajeCompleto = "$destinatario|$mensaje"
                    output?.println(mensajeCompleto)
                    Log.d("SocketManager", "Mensaje enviado: $mensajeCompleto")
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error enviando mensaje: ${e.message}")
                }
            }
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
                Log.e("SocketManager", "Error desconectando: ${e.message}")
            }
        }
    }

    private fun reiniciarConexion() {
        desconectar()
        // Reconexión automática después de 3 segundos
        executor.execute {
            Thread.sleep(3000)
            conectar()
        }
    }
}