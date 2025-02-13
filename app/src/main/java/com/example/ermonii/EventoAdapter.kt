import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.Evento
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
    }

    // Retorna el tamaño de la lista
    override fun getItemCount() = eventList.size
}
