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
        obtenerIDUsuarioIniciado()  // Aquí se obtiene el ID correctamente
        val userId = usuarioId.toString()
        setupSocketManager(userId)
        setupRecyclerViews(view)
        setupUIListeners(view, userId)
        cargarChatsIniciales(userId)
    }

    // En tu ChatFragmentMusico
    private fun setupSocketManager(userId: String) {
        socketManager = SocketManager(userId) { remitente, contenido ->
            activity?.runOnUiThread {
                if (remitente == "SERVER") {
                    // Mostrar como Toast o Snackbar
                    Toast.makeText(requireContext(), contenido, Toast.LENGTH_LONG).show()
                } else {
                    // Manejo normal de mensajes
                    manejarNuevoMensaje(userId, remitente, contenido)
                }
            }
        }
    }

    private fun manejarNuevoMensaje(userId: String, remitente: String, contenido: String) {
        val esMio = (remitente == userId) // Determinar si es mensaje propio

        val mensaje = Mensaje(
            id = System.currentTimeMillis().toInt(), // Generar ID único
            idUsuarioLocal = if (esMio) currentChatId ?: "" else remitente, // Usar currentChatId si es del músico
            idUsuarioMusico = userId,
            mensaje = contenido,
            fechaEnvio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            emisor = if (esMio) "musico" else "local"
                             )


        // Añadir al adapter y actualizar UI
        activity?.runOnUiThread {
            adapterMensajes.addMessage(mensaje)
            recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
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
                mostrarConversacion("2")
            }.also { adapterChats = it }
        }

        // Configurar RecyclerView de mensajes
        recyclerViewMensajes = view.findViewById(R.id.recyclerViewMensajes)
        recyclerViewMensajes.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true // Key para scroll automático
            }
            adapter = MensajesAdapter(usuarioId.toString()).also { adapterMensajes = it }
        }
    }

    private fun cargarChatsIniciales(userId: String) {
        // Simulación de respuesta de la API con datos de la tabla Mensaje
        val jsonSimulado = """
        [
            {
                "id": 1,
                "idUsuarioLocal": "6",
                "idUsuarioMusico": "$userId",
                "fechaEnvio": "10:30",
                "mensaje": "Hola, ¿estás disponible?",
                "emisor": "local"
            },
            {
                "id": 2,
                "idUsuarioLocal": "7",
                "idUsuarioMusico": "$userId",
                "fechaEnvio": "10:31",
                "mensaje": "Necesitamos un músico",
                "emisor": "local"
            }
        ]
    """.trimIndent()

        // Parseamos el JSON a una lista de Mensaje usando Gson
        val gson = Gson()
        val type = object : TypeToken<List<Mensaje>>() {}.type
        val mensajesList: List<Mensaje> = gson.fromJson(jsonSimulado, type)

        // Actualizamos el adaptador con la lista de mensajes
        adapterChats.submitList(mensajesList)
    }


    private fun setupUIListeners(view: View, userId: String) {
        val btnEnviar = view.findViewById<Button>(R.id.btnEnviar)
        val etMensaje = view.findViewById<EditText>(R.id.etMensaje)

        btnEnviar.setOnClickListener {
            enviarMensaje(etMensaje, userId.toString())
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

        // Mostrar un aviso con el id del destinatario y el mensaje
        Toast.makeText(requireContext(), "Enviando a $currentChatId: $mensajeTexto", Toast.LENGTH_LONG).show()

        val newMessage = crearMensaje(currentChatId!!, userId, mensajeTexto, "musico")
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
        // Simulación de respuesta de la API con datos de la tabla Mensaje para el historial
        val jsonSimulado = """
        [
            {
                "id": 10,
                "idUsuarioLocal": "$contactoId",
                "idUsuarioMusico": "1",
                "fechaEnvio": "10:35",
                "mensaje": "Hola, ¿en qué puedo ayudarte?",
                "emisor": "local"
            },
            {
                "id": 11,
                "idUsuarioLocal": "$contactoId",
                "idUsuarioMusico": "2",
                "fechaEnvio": "10:36",
                "mensaje": "Estoy disponible",
                "emisor": "musico"
            }
        ]
    """.trimIndent()

        // Parseamos el JSON a una lista de Mensaje usando Gson
        val gson = Gson()
        val type = object : TypeToken<List<Mensaje>>() {}.type
        val historial: List<Mensaje> = gson.fromJson(jsonSimulado, type)

        // Actualizamos el adaptador con la lista del historial
        adapterMensajes.submitList(historial)
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

    override fun onDestroy() {
        super.onDestroy()
        socketManager.desconectar()
    }
}