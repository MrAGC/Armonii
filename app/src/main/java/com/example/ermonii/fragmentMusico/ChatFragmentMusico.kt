package com.example.ermonii.fragmentMusico

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
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.SocketManager
import com.example.ermonii.clases.Mensaje
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragmentMusico : Fragment() {
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
        return inflater.inflate(R.layout.fragment_chat_musico, container, false)
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
                    if (isAdded && !isDetached) {
                        Log.d("Chat", "Mensaje recibido de $remitente: $contenido")

                        // Manejar mensajes del sistema
                        if (remitente == "SERVER") {
                            when {
                                contenido.startsWith("NUEVO_CHAT|") -> {
                                    val partes = contenido.split("|")
                                    if (partes.size == 3) {
                                        actualizarListaChatsMusico(partes[1], partes[2])
                                    }
                                }
                                else -> Toast.makeText(
                                    requireContext(),
                                    contenido,
                                    Toast.LENGTH_LONG
                                                      ).show()
                            }
                        } else {
                            manejarNuevoMensaje(userId, remitente, contenido)
                        }
                    }
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    if (isAdded && !isDetached) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${error.take(50)}...",
                            Toast.LENGTH_SHORT
                                      ).show()
                    }
                }
            }
                                     )
    }


    private fun manejarNuevoMensaje(userId: String, remitente: String, contenido: String) {
        val esMio = (remitente == userId)
        val chatIdActual = currentChatId ?: run {
            // Si no hay chat activo, crear uno nuevo
            currentChatId = remitente
            mostrarConversacion(remitente)
            remitente
        }

        val mensaje = Mensaje(
            id = System.currentTimeMillis().toInt(),
            idUsuarioLocal = if (esMio) chatIdActual else remitente,
            idUsuarioMusico = userId,
            mensaje = contenido,
            fechaEnvio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            emisor = if (esMio) "musico" else "local"
                             )

        // Actualizar UI de forma segura
        activity?.runOnUiThread {
            if (currentChatId == remitente) {
                adapterMensajes.addMessage(mensaje)
                recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
            }
            actualizarListaChatsMusico(remitente, contenido)
        }
    }

    private fun actualizarListaChatsMusico(remitente: String, ultimoMensaje: String) {
        val nuevosChats = adapterChats.currentList.toMutableList().map {
            if (it.idUsuarioLocal == remitente) { // Buscar por ID de local
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
            adapter = ChatsAdapter(esMusico = true) { chat -> // Pasar true para músico
                currentChatId = chat.idUsuarioLocal
                mostrarConversacion(chat.idUsuarioLocal)
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
        // Chats recientes con diferentes locales
        val jsonSimulado = """
    [
        {
            "id": 1,
            "idUsuarioLocal": "6", // ID del local
            "idUsuarioMusico": "$userId", // 1
            "fechaEnvio": "10:15",
            "mensaje": "Confirmado el ensayo",
            "emisor": "musico"
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
            idUsuarioLocal = currentChatId!!,
            idUsuarioMusico = userId,
            mensaje = mensajeTexto,
            fechaEnvio = dateFormat.format(Date()),
            emisor = "musico",
            estado = "ENVIADO"
                                )

        adapterMensajes.addMessage(newMessage)
        socketManager.sendMessage(currentChatId!!, mensajeTexto)
        etMensaje.text.clear()
        scrollToBottom()
    }

    // En ChatFragmentLocal.kt
    private fun scrollToBottom() {
        recyclerViewMensajes.post {
            val layoutManager = recyclerViewMensajes.layoutManager as? LinearLayoutManager ?: return@post
            val adapter = recyclerViewMensajes.adapter ?: return@post
            val itemCount = adapter.itemCount

            if (itemCount == 0) return@post

            // Scroll suave con posición exacta
            val smoothScroller = object : LinearSmoothScroller(requireContext()) {
                override fun getVerticalSnapPreference(): Int = SNAP_TO_END

                override fun calculateDtToFit(
                    viewStart: Int,
                    viewEnd: Int,
                    boxStart: Int,
                    boxEnd: Int,
                    snapPreference: Int
                                             ): Int = boxEnd - viewEnd
            }

            smoothScroller.targetPosition = itemCount - 1
            layoutManager.startSmoothScroll(smoothScroller)

            // Scroll inmediato como respaldo
            recyclerViewMensajes.postDelayed({
                                                 recyclerViewMensajes.scrollToPosition(itemCount - 1)
                                             }, 100)
        }
    }

    private fun mostrarConversacion(contactoId: String) {
        view?.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showNext()
        view?.findViewById<TextView>(R.id.txtNombreContacto)?.text = contactoId
        cargarHistorialMensajes(contactoId)
        scrollToBottom()
    }

    private fun cargarHistorialMensajes(localId: String) {
        // Conversaciones específicas para cada local
        val jsonConversacion = when (localId) {
            "6" -> """ // ID del local
        [
            {
                "id": 3001,
                "idUsuarioLocal": "6",
                "idUsuarioMusico": "$usuarioId", // 1
                "fechaEnvio": "09:00",
                "mensaje": "Buenos días, ¿ensayamos hoy?",
                "emisor": "local"
            },
            {
                "id": 3002,
                "idUsuarioLocal": "6",
                "idUsuarioMusico": "$usuarioId", // 1
                "fechaEnvio": "09:15",
                "mensaje": "Sí, a las 18:00 en el estudio",
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

        adapterMensajes.actualizarLista(mensajesFiltrados)
    }



    companion object {
        fun newInstance(usuarioId: Int): ChatFragmentMusico {
            val fragment = ChatFragmentMusico()
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

    override fun onResume() {
        super.onResume()
        if (!socketManager.isConnected.get()) {
            socketManager.reconnect()
        }
    }

    override fun onPause() {
        super.onPause()
        // Mantener conexión si está en segundo plano
        if (activity?.isChangingConfigurations == false) {
            socketManager.disconnect()
        }
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