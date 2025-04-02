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
        socketManager = SocketManager(userId) { remitente, contenido ->
            activity?.runOnUiThread {
                if (remitente == "SERVER") {
                    Toast.makeText(requireContext(), contenido, Toast.LENGTH_LONG).show()
                } else {
                    manejarNuevoMensaje(userId, remitente, contenido)
                }
            }
        }
    }

    private fun manejarNuevoMensaje(userId: String, remitente: String, contenido: String) {
        val esMio = (remitente == userId)

        val mensaje = Mensaje(
            id = System.currentTimeMillis().toInt(),
            idUsuarioLocal = userId,
            idUsuarioMusico = if (esMio) currentChatId ?: "" else remitente,
            mensaje = contenido,
            fechaEnvio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            emisor = if (esMio) "local" else "musico"
                             )

        activity?.runOnUiThread {
            adapterMensajes.addMessage(mensaje)
            recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
        }
    }

    private fun setupRecyclerViews(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerViewChats).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatsAdapter { chat ->
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
                "fechaEnvio": "10:30",
                "mensaje": "Hola, ¿estás disponible?",
                "emisor": "musico"
            },
            {
                "id": 2,
                "idUsuarioLocal": "$userId",
                "idUsuarioMusico": "2",
                "fechaEnvio": "10:31",
                "mensaje": "Necesitamos un músico",
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
            idUsuarioLocal = userId,
            idUsuarioMusico = currentChatId!!,
            mensaje = mensajeTexto,
            fechaEnvio = dateFormat.format(Date()),
            emisor = "local"
                                )

        adapterMensajes.addMessage(newMessage)
        recyclerViewMensajes.smoothScrollToPosition(adapterMensajes.itemCount - 1)
        socketManager.sendMessage(currentChatId!!, mensajeTexto)
        etMensaje.text.clear()
    }

    private fun mostrarConversacion(musicoId: String) {
        view?.findViewById<ViewSwitcher>(R.id.viewSwitcher)?.showNext()
        view?.findViewById<TextView>(R.id.txtNombreContacto)?.text = musicoId
        cargarHistorialMensajes(musicoId)
    }

    private fun cargarHistorialMensajes(musicoId: String) {
        val jsonSimulado = """
        [
            {
                "id": 10,
                "idUsuarioLocal": "${usuarioId}",
                "idUsuarioMusico": "$musicoId",
                "fechaEnvio": "10:35",
                "mensaje": "Hola, ¿en qué puedo ayudarte?",
                "emisor": "musico"
            },
            {
                "id": 11,
                "idUsuarioLocal": "${usuarioId}",
                "idUsuarioMusico": "$musicoId",
                "fechaEnvio": "10:36",
                "mensaje": "Estoy disponible",
                "emisor": "local"
            }
        ]
        """.trimIndent()

        val gson = Gson()
        val type = object : TypeToken<List<Mensaje>>() {}.type
        val historial: List<Mensaje> = gson.fromJson(jsonSimulado, type)
        adapterMensajes.submitList(historial)
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

    override fun onDestroy() {
        super.onDestroy()
        socketManager.desconectar()
    }
}