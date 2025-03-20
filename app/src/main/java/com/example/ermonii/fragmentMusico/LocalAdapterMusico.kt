package com.example.ermonii.fragmentMusico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.example.ermonii.clases.Local
import com.example.ermonii.R

class LocalAdapterMusico(private val localList: List<Local>) : RecyclerView.Adapter<LocalAdapterMusico.LocalViewHolder>() {

    // Crear las vistas que se van a usar para mostrar los datos
    class LocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.localName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.localDescription)
        val imageView: ImageView = itemView.findViewById(R.id.localImage)
    }

    // Este método infla el layout para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_local, parent, false)
        return LocalViewHolder(itemView)
    }

    // Este método asigna los valores de cada Local a las vistas
    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val currentLocal = localList[position]
        holder.nameTextView.text = currentLocal.nombre
        holder.descriptionTextView.text = currentLocal.description
        holder.imageView.setImageResource(currentLocal.image)
    }

    // Retorna el tamaño de la lista
    override fun getItemCount() = localList.size
}
