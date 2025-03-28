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
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.SocketManager
import com.example.ermonii.clases.Mensaje
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragmentMusico : Fragment() {
    private lateinit var socketManager: SocketManager
    private lateinit var adapterChats: ChatsAdapter
    private lateinit var adapterMensajes: MensajesAdapter
    private var currentChatId: String? = null
    private val mensajes = mutableListOf<Mensaje>()
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private lateinit var recyclerViewMensajes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View {
        return inflater.inflate(R.layout.fragment_chat_musico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = obtenerUserId()
        setupSocketManager(userId)
        setupRecyclerViews(view)
        setupUIListeners(view)
        cargarChatsIniciales(userId)
    }

    private fun setupSocketManager(userId: String) {
        socketManager = SocketManager(userId) { sender, message ->
            activity?.runOnUiThread {
                manejarNuevoMensaje(userId, sender, message)
            }
        }
    }

    private fun manejarNuevoMensaje(userId: String, sender: String, message: String) {
        actualizarListaChats(sender, message, userId)
        if (sender == currentChatId) {
            val newMessage = crearMensaje(sender, userId, message, "local")
            adapterMensajes.addMessage(newMessage)
            scrollToBottom()
        }
    }

    private fun actualizarListaChats(sender: String, message: String, userId: String) {
        val nuevosChats = adapterChats.currentList.toMutableList().apply {
            removeAll { it.idUsuarioLocal == sender }
            add(0, crearMensaje(sender, userId, message, "local"))
        }
        adapterChats.submitList(nuevosChats)
    }

    private fun crearMensaje(sender: String, userId: String, message: String, emisor: String): Mensaje {
        return Mensaje(
            id = System.currentTimeMillis().toInt(),
            idUsuarioLocal = sender,
            idUsuarioMusico = userId,
            fechaEnvio = dateFormat.format(Date()),
            mensaje = message,
            emisor = emisor
                      )
    }

    private fun setupRecyclerViews(view: View) {
        // Configurar RecyclerView de chats
        view.findViewById<RecyclerView>(R.id.recyclerViewChats).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatsAdapter { chat ->
                currentChatId = chat.idUsuarioLocal
                mostrarConversacion(chat.idUsuarioLocal)
            }.also { adapterChats = it }
        }

        // Configurar RecyclerView de mensajes
        recyclerViewMensajes = view.findViewById(R.id.recyclerViewMensajes)
        recyclerViewMensajes.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true // Key para scroll automático
            }
            adapter = MensajesAdapter().also { adapterMensajes = it }
        }
    }

    private fun cargarChatsIniciales(userId: String) {
        val chatsIniciales = listOf(
            crearMensaje("local_1", userId, "Hola, ¿estás disponible?", "local"),
            crearMensaje("local_2", userId, "Necesitamos un músico", "local")
                                   )
        adapterChats.submitList(chatsIniciales)
    }

    private fun setupUIListeners(view: View) {
        val btnEnviar = view.findViewById<Button>(R.id.btnEnviar)
        val etMensaje = view.findViewById<EditText>(R.id.etMensaje)

        btnEnviar.setOnClickListener {
            enviarMensaje(etMensaje)
        }

        view.findViewById<Button>(R.id.btnVolver).setOnClickListener {
            currentChatId = null
            view.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showPrevious()
        }
    }

    private fun enviarMensaje(etMensaje: EditText) {
        val mensajeTexto = etMensaje.text.toString().trim()
        if (mensajeTexto.isEmpty() || currentChatId == null) {
            Toast.makeText(requireContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show()
            return
        }

        val newMessage = crearMensaje(currentChatId!!, "musico_123", mensajeTexto, "musico")
        adapterMensajes.addMessage(newMessage)
        scrollToBottom()
        socketManager.sendMessage(currentChatId!!, mensajeTexto)
        etMensaje.text.clear()
    }

    private fun scrollToBottom() {
        recyclerViewMensajes.post {
            // Scroll suave al final
            recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
        }
    }

    private fun mostrarConversacion(contactoId: String) {
        view?.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showNext()
        view?.findViewById<TextView>(R.id.txtNombreContacto)?.text = contactoId
        cargarHistorialMensajes(contactoId)
        scrollToBottom()
    }

    private fun cargarHistorialMensajes(contactoId: String) {
        val historial = listOf(
            crearMensaje(contactoId, "musico_123", "Hola, ¿en qué puedo ayudarte?", "local"),
            crearMensaje(contactoId, "musico_123", "Estoy disponible", "musico")
                              )
        adapterMensajes.submitList(historial)
    }

    private fun obtenerUserId(): String {
        return "musico_123"
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.desconectar()
    }
}