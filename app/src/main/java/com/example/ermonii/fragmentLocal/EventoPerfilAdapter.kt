package com.example.ermonii.fragmentLocal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Evento
import com.google.android.material.button.MaterialButton

class EventoPerfilAdapter(
    private val eventos: List<Evento>,
    private val onEditarClick: (Evento) -> Unit,
    private val onEliminarClick: (Evento) -> Unit
                         ) : RecyclerView.Adapter<EventoPerfilAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.eventName)
        val descripcion: TextView = itemView.findViewById(R.id.eventDescription)
        val fecha: TextView = itemView.findViewById(R.id.eventDate)
        val btnEditar: MaterialButton = itemView.findViewById(R.id.btn_editarEvento)
        val btnEliminar: MaterialButton = itemView.findViewById(R.id.btn_eliminarEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento_perfil, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.nombre.text = evento.nombre
        holder.descripcion.text = evento.descripcion
        holder.fecha.text = evento.fecha.replace("T", "   ").replace(Regex(":00$"), "")

        holder.btnEditar.setOnClickListener { onEditarClick(evento) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(evento) }
    }

    override fun getItemCount(): Int = eventos.size
}
