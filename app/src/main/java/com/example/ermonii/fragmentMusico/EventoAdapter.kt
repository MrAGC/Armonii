package com.example.ermonii.fragmentMusico

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.clases.Evento
import com.example.ermonii.R
import com.example.ermonii.clases.Local
import com.example.ermonii.fragmentMusico.HomeFragmentMusico.Companion.locales

@Suppress("NAME_SHADOWING")
class EventoAdapter(private var eventList: List<Evento>, private var localList: List<Local>) :
    RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.eventName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.eventDescription)
        val dateTextView: TextView = itemView.findViewById(R.id.eventDate) // Añadido
        val imageView: ImageView = itemView.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val currentEvento = eventList[position]

        // Datos básicos
        holder.nameTextView.text = currentEvento.nombre
        holder.descriptionTextView.text = "Descripción: " + currentEvento.descripcion
        holder.dateTextView.text = "Fecha: " + currentEvento.fecha // Usamos la fecha del evento

        // Imagen estática (ya que la API no envía imágenes)
        holder.imageView.setImageResource(R.drawable.logo_armonii) // Imagen por defecto

        // Diálogo
        holder.itemView.setOnClickListener {
            mostrarDialogoImagen(holder.itemView.context, currentEvento)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarDialogoImagen(context: Context, evento: Evento) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_imagen_descripcion)

        val local = localList.find { it.id == evento.local }

        Log.d("HOME_FRAGMENT_MUSICO", """
                ===== EVENTO =====
                ID: ${evento.id}
                Nombre: ${evento.nombre}
                Local: ${evento.local}
                Musico: ${evento.music}
                """.trimIndent())

        val imagenDialog = dialog.findViewById<ImageView>(R.id.dialog_imagen)
        val descripcionDialog = dialog.findViewById<TextView>(R.id.dialog_descripcion)
        val txtNombre = dialog.findViewById<TextView>(R.id.txtNombre)
        val txtLocal = dialog.findViewById<TextView>(R.id.txtLocal)
        val txtDuracion = dialog.findViewById<TextView>(R.id.txtDuracion)


        // Usar imagen por defecto y datos del evento
        imagenDialog.setImageResource(R.drawable.logo_armonii)
        txtNombre.text = evento.nombre
        txtLocal.text = "Local: " + (local?.nombre ?: "Nombre no disponible")
        txtDuracion.text = "Duracion: ${evento.duracion} minutos"
        descripcionDialog.text = "Descripción: ${evento.descripcion}"

        // Configuración del diálogo (existente)
        val window = dialog.window
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
                         )
        window?.setGravity(Gravity.CENTER)
        window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    override fun getItemCount() = eventList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Evento>) {
        eventList = newList
        notifyDataSetChanged()
    }
}