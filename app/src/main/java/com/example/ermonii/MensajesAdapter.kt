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

class MensajesAdapter(
    private val currentUserId: String
                     ) : ListAdapter<Mensaje, MensajesAdapter.MensajeViewHolder>(DiffCallback()) {

    companion object {
        private const val TIPO_EMISOR = 0
        private const val TIPO_RECEPTOR = 1
    }

    inner class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mensajeTextView: TextView = itemView.findViewById(R.id.mensajeTextView)
        private val fechaEnvioTextView: TextView = itemView.findViewById(R.id.fechaEnvioTextView)

        fun bind(mensaje: Mensaje) {
            try {
                mensajeTextView.text = mensaje.mensaje ?: ""
                fechaEnvioTextView.text = mensaje.fechaEnvio ?: ""
            } catch (e: Exception) {
                Log.e("MensajesAdapter", "Error binding: ${e.message}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        return try {
            val layout = when (viewType) {
                TIPO_EMISOR -> R.layout.item_mensaje_emisor
                else -> R.layout.item_mensaje_receptor
            }
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            MensajeViewHolder(view)
        } catch (e: Exception) {
            Log.e("MensajesAdapter", "Error creando ViewHolder: ${e.message}")
            throw e
        }
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.e("MensajesAdapter", "Error en onBindViewHolder: ${e.message}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            val mensaje = getItem(position)
            val esMio = when (mensaje.emisor) {
                "musico" -> mensaje.idUsuarioMusico == currentUserId
                "local" -> mensaje.idUsuarioLocal == currentUserId
                else -> false
            }
            if (esMio) TIPO_EMISOR else TIPO_RECEPTOR
        } catch (e: Exception) {
            Log.e("MensajesAdapter", "Error en getItemViewType: ${e.message}")
            TIPO_RECEPTOR
        }
    }

    fun addMessage(newMessage: Mensaje) {
        try {
            val newList = currentList.toMutableList().apply { add(newMessage) }
            submitList(newList)
        } catch (e: Exception) {
            Log.e("MensajesAdapter", "Error añadiendo mensaje: ${e.message}")
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