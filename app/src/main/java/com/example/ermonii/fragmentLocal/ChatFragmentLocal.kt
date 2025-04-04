package com.example.ermonii.fragmentLocal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.SocketManager
import com.example.ermonii.clases.Mensaje
import com.example.ermonii.fragmentMusico.ChatsAdapter
import com.example.ermonii.fragmentMusico.MensajesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragmentLocal : Fragment() {
    private var usuarioId: Int = -1
    private lateinit var socketManager: SocketManager
    private lateinit var adapterChats: ChatsAdapter
    private lateinit var adapterMensajes: MensajesAdapter
    private var currentChatId: String? = null
    private val mensajes = mutableListOf<Mensaje>()
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private lateinit var recyclerViewMensajes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View {
        return inflater.inflate(R.layout.fragment_chat_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtenerIDUsuarioIniciado()
        val userId = usuarioId.toString()
        setupSocketManager(userId)
        setupRecyclerViews(view)
        setupUIListeners(view, userId)
        cargarChatsIniciales(userId)
    }

    private fun setupSocketManager(userId: String) {
        socketManager = SocketManager(
            userId = userId,
            onMessageReceived = { remitente, contenido ->
                activity?.runOnUiThread {
                    when (remitente) {
                        "SERVER" -> Toast.makeText(
                            requireContext(),
                            contenido,
                            Toast.LENGTH_LONG
                                                  ).show()

                        else -> manejarNuevoMensaje(userId, remitente, contenido)
                    }
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Error de conexión: $error",
                        Toast.LENGTH_LONG
                                  ).show()

                    // Opcional: Intentar reconexión manual si el error es crítico
                    if (error.contains("No conectado", ignoreCase = true)) {
                        socketManager?.reconnect()
                    }
                }
            }
                                     )
    }

    private fun manejarNuevoMensaje(userId: String, remitente: String, contenido: String) {
        val esMio = (remitente == userId)
        val chatIdActual = if (esMio) currentChatId ?: "" else remitente

        // Solo agregar si es el chat activo
        if (chatIdActual == currentChatId) {
            val mensaje = Mensaje(
                id = System.currentTimeMillis().toInt(),
                idUsuarioLocal = userId,
                idUsuarioMusico = chatIdActual,
                mensaje = contenido,
                fechaEnvio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                emisor = if (esMio) "local" else "musico"
                                 )

            activity?.runOnUiThread {
                adapterMensajes.addMessage(mensaje)
                recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
            }
        }

        actualizarListaChatsLocal(remitente, contenido)
    }

    private fun actualizarListaChatsLocal(remitente: String, ultimoMensaje: String) {
        val nuevosChats = adapterChats.currentList.toMutableList().map {
            if (it.idUsuarioMusico == remitente) { // Buscar por ID de músico
                it.copy(
                    mensaje = ultimoMensaje,
                    fechaEnvio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                       )
            } else {
                it
            }
        }
        adapterChats.submitList(nuevosChats)
    }

    private fun setupRecyclerViews(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerViewChats).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatsAdapter(esMusico = false) { chat ->
                currentChatId = chat.idUsuarioMusico
                mostrarConversacion(chat.idUsuarioMusico)
            }.also { adapterChats = it }
        }

        recyclerViewMensajes = view.findViewById(R.id.recyclerViewMensajes)
        recyclerViewMensajes.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = MensajesAdapter(usuarioId.toString()).also { adapterMensajes = it }
        }
    }

    private fun cargarChatsIniciales(userId: String) {
        // Últimos mensajes de diferentes chats para mostrar en la lista
        val jsonSimulado = """
    [
        {
            "id": 1,
            "idUsuarioLocal": "$userId", // 6
            "idUsuarioMusico": "1", // ID del músico
            "fechaEnvio": "14:30",
            "mensaje": "¿Podrías tocar en mi boda?",
            "emisor": "local"
        }
    ]
    """.trimIndent()

        val gson = Gson()
        val type = object : TypeToken<List<Mensaje>>() {}.type
        val mensajesList: List<Mensaje> = gson.fromJson(jsonSimulado, type)
        adapterChats.submitList(mensajesList)
    }

    private fun setupUIListeners(view: View, userId: String) {
        val btnEnviar = view.findViewById<Button>(R.id.btnEnviar)
        val etMensaje = view.findViewById<EditText>(R.id.etMensaje)

        btnEnviar.setOnClickListener {
            enviarMensaje(etMensaje, userId)
        }

        view.findViewById<Button>(R.id.btnVolver).setOnClickListener {
            currentChatId = null
            view.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showPrevious()
        }
    }

    private fun enviarMensaje(etMensaje: EditText, userId: String) {
        val mensajeTexto = etMensaje.text.toString().trim()
        if (mensajeTexto.isEmpty() || currentChatId == null) {
            Toast.makeText(requireContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show()
            return
        }

        val newMessage = Mensaje(
            id = System.currentTimeMillis().toInt(),
            idUsuarioLocal = userId,
            idUsuarioMusico = currentChatId!!,
            mensaje = mensajeTexto,
            fechaEnvio = dateFormat.format(Date()),
            emisor = "local"
                                )

        adapterMensajes.addMessage(newMessage)
        recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
        socketManager.sendMessage(currentChatId!!, mensajeTexto)
        Log.d("EnvioLocal", "Enviando a ${currentChatId}: $mensajeTexto")
        etMensaje.text.clear()
    }

    private fun mostrarConversacion(musicoId: String) {
        view?.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showNext()
        view?.findViewById<TextView>(R.id.txtNombreContacto)?.text = musicoId
        cargarHistorialMensajes(musicoId)
    }

    private fun cargarHistorialMensajes(musicoId: String) {
        // Conversaciones completas diferentes para cada músico
        val jsonConversacion = when (musicoId) {
            "1" -> """ // ID del músico
        [
            {
                "id": 1001,
                "idUsuarioLocal": "$usuarioId", // 6
                "idUsuarioMusico": "1",
                "fechaEnvio": "14:25",
                "mensaje": "Hola, busco un violinista",
                "emisor": "local"
            },
            {
                "id": 1002,
                "idUsuarioLocal": "$usuarioId", // 6
                "idUsuarioMusico": "1",
                "fechaEnvio": "14:28",
                "mensaje": "Hola, sí toco violín. ¿Para qué fecha?",
                "emisor": "musico"
            }
        ]
        """
            else -> "[]"
        }

        val gson = Gson()
        val type = object : TypeToken<List<Mensaje>>() {}.type
        val historial: List<Mensaje> = gson.fromJson(jsonConversacion, type)

        val mensajesFiltrados = historial.sortedBy {
            SimpleDateFormat("HH:mm").parse(it.fechaEnvio)
        }

        adapterMensajes.submitList(mensajesFiltrados)
    }





    companion object {
        fun newInstance(usuarioId: Int): ChatFragmentLocal {
            val fragment = ChatFragmentLocal()
            val bundle = Bundle().apply {
                putInt("usuario", usuarioId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun obtenerIDUsuarioIniciado() {
        usuarioId = requireArguments().getInt("usuario", -1)
    }

    override fun onDestroyView() {
        // Buen lugar para limpiar recursos de UI
        super.onDestroyView()
    }

    override fun onDestroy() {
        // Siempre desconectar cuando el Fragment se destruye permanentemente
        if (!requireActivity().isChangingConfigurations) {
            socketManager?.disconnect()
        }
        super.onDestroy()
    }
}