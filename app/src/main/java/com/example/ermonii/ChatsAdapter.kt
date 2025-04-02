package com.example.ermonii.fragmentMusico

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Mensaje

class ChatsAdapter(
    private val onChatClick: (Mensaje) -> Unit
                  ) : ListAdapter<Mensaje, ChatsAdapter.ChatViewHolder>(DiffCallback()) {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtContactName: TextView = itemView.findViewById(R.id.txtContactName)
        private val mensajeTextView: TextView = itemView.findViewById(R.id.mensajeTextView)
        private val fechaEnvioTextView: TextView = itemView.findViewById(R.id.fechaEnvioTextView)

        fun bind(mensaje: Mensaje) {
            try {
                txtContactName.text = mensaje.idUsuarioMusico ?: "Desconocido"
                mensajeTextView.text = mensaje.mensaje ?: ""
                fechaEnvioTextView.text = mensaje.fechaEnvio ?: ""
                itemView.setOnClickListener { onChatClick(mensaje) }
            } catch (e: Exception) {
                Log.e("ChatsAdapter", "Error binding: ${e.message}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return try {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
            ChatViewHolder(itemView)
        } catch (e: Exception) {
            Log.e("ChatsAdapter", "Error creando ViewHolder: ${e.message}")
            throw e
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.e("ChatsAdapter", "Error en onBindViewHolder: ${e.message}")
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Mensaje>() {
        override fun areItemsTheSame(oldItem: Mensaje, newItem: Mensaje): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mensaje, newItem: Mensaje): Boolean {
            return oldItem == newItem
        }
    }
}