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
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.SocketManager
import com.example.ermonii.MessageDto
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
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private lateinit var recyclerViewMensajes: RecyclerView
    private val gson = Gson()

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
            onMessageReceived = { message ->
                activity?.runOnUiThread {
                    when (message.type) {
                        "MESSAGE" -> handleIncomingMessage(message)
                        "NEW_CHAT" -> handleNewChat(message)
                        "ERROR" -> showErrorToast(message.error ?: "Error desconocido")
                        "HEARTBEAT" -> Log.d("Chat", "Latido recibido")
                        else -> Log.w("Chat", "Tipo de mensaje no manejado: ${message.type}")
                    }
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    showErrorToast(error)
                }
            }
                                     )
    }

    private fun handleIncomingMessage(message: MessageDto) {
        val senderId = message.sender ?: return
        val content = message.content ?: return

        val currentChat = currentChatId ?: run {
            createNewChat(senderId)
            senderId
        }

        if (currentChat == senderId) {
            addMessageToUI(senderId, content)
        }
        updateChatList(senderId, content)
    }

    private fun createNewChat(contactoId: String) {
        currentChatId = contactoId
        // Aquí puedes añadir lógica adicional si necesitas:
        // - Notificar al servidor del nuevo chat
        // - Crear un nuevo registro en tu base de datos local
        // - Actualizar cualquier otro estado necesario
        mostrarConversacion(contactoId)
    }

    private fun addMessageToUI(senderId: String, content: String) {
        val mensaje = Mensaje(
            id = System.currentTimeMillis().toInt(),
            idUsuarioLocal = if (senderId == usuarioId.toString()) usuarioId.toString() else senderId,
            idUsuarioMusico = if (senderId == usuarioId.toString()) senderId else usuarioId.toString(),
            mensaje = content,
            fechaEnvio = dateFormat.format(Date()),
            emisor = if (senderId == usuarioId.toString()) "local" else "musico"
                             )

        adapterMensajes.addMessage(mensaje)
        scrollToBottom()
    }

    private fun updateChatList(remitente: String, ultimoMensaje: String) {
        val nuevosChats = adapterChats.currentList.toMutableList().map {
            if (it.idUsuarioMusico == remitente || it.idUsuarioLocal == remitente) {
                it.copy(
                    mensaje = ultimoMensaje,
                    fechaEnvio = dateFormat.format(Date())
                       )
            } else {
                it
            }
        }
        adapterChats.submitList(nuevosChats)
    }

    private fun handleNewChat(message: MessageDto) {
        val contactId = message.recipient ?: return
        val initialMessage = message.content ?: return
        actualizarListaChatsLocal(contactId, initialMessage)
    }

    private fun actualizarListaChatsLocal(remitente: String, ultimoMensaje: String) {
        val nuevosChats = adapterChats.currentList.toMutableList().map {
            if (it.idUsuarioMusico == remitente) {
                it.copy(
                    mensaje = ultimoMensaje,
                    fechaEnvio = dateFormat.format(Date())
                       )
            } else {
                it
            }
        }
        adapterChats.submitList(nuevosChats)
    }

    private fun showErrorToast(error: String) {
        Toast.makeText(
            requireContext(),
            "Error: ${error.take(50)}" + if (error.length > 50) "..." else "",
            Toast.LENGTH_LONG
                      ).show()
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
        val jsonSimulado = """
            [
                {
                    "id": 1,
                    "idUsuarioLocal": "$userId",
                    "idUsuarioMusico": "1",
                    "fechaEnvio": "14:30",
                    "mensaje": "¿Podrías tocar en mi boda?",
                    "emisor": "local"
                }
            ]
        """.trimIndent()

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
        socketManager.sendChatMessage(currentChatId!!, mensajeTexto)
        etMensaje.text.clear()
        scrollToBottom()
    }

    private fun scrollToBottom() {
        recyclerViewMensajes.post {
            (recyclerViewMensajes.layoutManager as? LinearLayoutManager)?.apply {
                smoothScrollToPosition(recyclerViewMensajes, null, adapterMensajes.itemCount - 1)
            }
        }
    }

    private fun mostrarConversacion(musicoId: String) {
        view?.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showNext()
        view?.findViewById<TextView>(R.id.txtNombreContacto)?.text = musicoId
        cargarHistorialMensajes(musicoId)
        scrollToBottom()
    }

    private fun cargarHistorialMensajes(musicoId: String) {
        val jsonConversacion = when (musicoId) {
            "1" -> """
                [
                    {
                        "id": 1001,
                        "idUsuarioLocal": "$usuarioId",
                        "idUsuarioMusico": "1",
                        "fechaEnvio": "14:25",
                        "mensaje": "Hola, busco un violinista",
                        "emisor": "local"
                    },
                    {
                        "id": 1002,
                        "idUsuarioLocal": "$usuarioId",
                        "idUsuarioMusico": "1",
                        "fechaEnvio": "14:28",
                        "mensaje": "Hola, sí toco violín. ¿Para qué fecha?",
                        "emisor": "musico"
                    }
                ]
            """
            else -> "[]"
        }

        val type = object : TypeToken<List<Mensaje>>() {}.type
        val historial: List<Mensaje> = gson.fromJson(jsonConversacion, type)
        adapterMensajes.actualizarLista(historial.sortedBy { it.fechaEnvio })
    }

    companion object {
        fun newInstance(usuarioId: Int) = ChatFragmentLocal().apply {
            arguments = Bundle().apply { putInt("usuario", usuarioId) }
        }
    }

    private fun obtenerIDUsuarioIniciado() {
        usuarioId = requireArguments().getInt("usuario", -1)
    }

    override fun onDestroy() {
        if (!requireActivity().isChangingConfigurations) {
            socketManager.disconnect()
        }
        super.onDestroy()
    }
}