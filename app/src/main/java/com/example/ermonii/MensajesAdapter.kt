package com.example.ermonii.fragmentMusico

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        private val estadoTextView: TextView? = itemView.findViewById(R.id.estadoTextView) // Nullable

        fun bind(mensaje: Mensaje) {
            mensajeTextView.text = mensaje.mensaje
            fechaEnvioTextView.text = mensaje.fechaEnvio

            val esMio = when (mensaje.emisor) {
                "musico" -> mensaje.idUsuarioMusico == currentUserId
                "local" -> mensaje.idUsuarioLocal == currentUserId
                else -> false
            }

            estadoTextView?.let {
                it.text = when (mensaje.estado) {
                    "ERROR" -> "⚠️"
                    "ENVIADO" -> "✓"
                    else -> ""
                }
                it.visibility = if (esMio) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val layout = when (viewType) {
            TIPO_EMISOR -> R.layout.item_mensaje_emisor
            else -> R.layout.item_mensaje_receptor
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        holder.bind(getItem(position))
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
        val newList = currentList.toMutableList().apply {
            add(newMessage)
            sortBy { it.timestamp }
        }
        submitList(newList)
        notifyItemInserted(newList.size - 1)
    }

    fun updateMessages(newMessages: List<Mensaje>) {
        val sorted = newMessages.sortedBy { it.timestamp }
        submitList(sorted)
        notifyDataSetChanged()
    }

    fun actualizarLista(nuevaLista: List<Mensaje>) {
        submitList(nuevaLista.toMutableList())
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