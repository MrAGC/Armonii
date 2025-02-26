package com.example.ermonii.FragmentMusico

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.Clases.Evento
import com.example.ermonii.R

class EventoAdapter(private val eventList: List<Evento>) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    // Crear las vistas que se van a usar para mostrar los datos
    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.eventName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.eventDescription)
        val imageView: ImageView = itemView.findViewById(R.id.eventImage)  // Imagen del evento (debe ser la del Local)
    }

    // Este método infla el layout para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(itemView)
    }

    // Este método asigna los valores de cada Evento a las vistas
    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val currentEvento = eventList[position]
        holder.nameTextView.text = currentEvento.name
        holder.descriptionTextView.text = currentEvento.description

        // Asignar la imagen del Local asociado al evento al ImageView
        holder.imageView.setImageResource(currentEvento.local.image)  // Usa la imagen del Local

        // Configurar el listener para mostrar el diálogo al hacer clic en la imagen
        holder.imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Mostrar el diálogo personalizado
                    mostrarDialogoImagen(holder.itemView.context, currentEvento)
                    true // Consumir el evento
                }
                else -> false // No consumir otros eventos
            }
        }
    }

    // Método para mostrar el diálogo con la imagen y la descripción del evento
    private fun mostrarDialogoImagen(context: Context, evento: Evento) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_imagen_descripcion)

        // Configurar la imagen y la descripción en el diálogo
        val imagenDialog = dialog.findViewById<ImageView>(R.id.dialog_imagen)
        val descripcionDialog = dialog.findViewById<TextView>(R.id.dialog_descripcion)

        imagenDialog.setImageResource(evento.local.image)  // Usar la imagen del Local
        descripcionDialog.text = evento.description  // Usar la descripción del Evento

        // Configurar el tamaño y la posición del diálogo
        val window = dialog.window
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% del ancho de la pantalla
            ViewGroup.LayoutParams.WRAP_CONTENT
                         )
        window?.setGravity(Gravity.CENTER) // Centrar el diálogo en la pantalla

        // Aplicar la animación al diálogo (si tienes una animación personalizada)
        window?.setWindowAnimations(R.style.DialogAnimation)

        // Cerrar el diálogo al tocar fuera
        dialog.setCanceledOnTouchOutside(true)

        // Mostrar el diálogo
        dialog.show()
    }

    // Retorna el tamaño de la lista
    override fun getItemCount() = eventList.size
}