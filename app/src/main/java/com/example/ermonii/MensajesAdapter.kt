package com.example.ermonii.fragmentMusico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Mensaje

class MensajesAdapter () : ListAdapter<Mensaje, MensajesAdapter.MensajeViewHolder>(DiffCallback()) {

    companion object {
        private const val TIPO_EMISOR = 0
        private const val TIPO_RECEPTOR = 1
    }

    inner class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mensajeTextView: TextView = itemView.findViewById(R.id.mensajeTextView)
        private val fechaEnvioTextView: TextView = itemView.findViewById(R.id.fechaEnvioTextView)

        fun bind(mensaje: Mensaje) {
            mensajeTextView.text = mensaje.mensaje
            fechaEnvioTextView.text = mensaje.fechaEnvio
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
        return if (getItem(position).emisor == "musico") TIPO_EMISOR else TIPO_RECEPTOR
    }

    fun addMessage(newMessage: Mensaje) {
        val newList = currentList.toMutableList().apply { add(newMessage) }
        submitList(newList)
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