package com.example.ermonii.fragmentLocal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Musico

class MusicoAdapter(private val musicos: List<Musico>) :
    RecyclerView.Adapter<MusicoAdapter.MusicoViewHolder>() {

    class MusicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.txtNombreMusico)
        val tvApodo: TextView = itemView.findViewById(R.id.txtApodo)
        val tvEdad: TextView = itemView.findViewById(R.id.txtEdad)
        val tvEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val tvGenero: TextView = itemView.findViewById(R.id.txtGeneroMusical)
        val tvBiografia: TextView = itemView.findViewById(R.id.txtBiografia)
        val imgMusico: ImageView = itemView.findViewById(R.id.imgMusico)
        val imgVal1: ImageView = itemView.findViewById(R.id.imgVal1)
        val imgVal2: ImageView = itemView.findViewById(R.id.imgVal2)
        val imgVal3: ImageView = itemView.findViewById(R.id.imgVal3)
        val imgVal4: ImageView = itemView.findViewById(R.id.imgVal4)
        val imgVal5: ImageView = itemView.findViewById(R.id.imgVal5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_musico, parent, false)
        return MusicoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MusicoViewHolder, position: Int) {
        val musico = musicos[position]

        holder.tvNombre.text = "${musico.nombre} ${musico.apellido}"
        if (musico.apodo.isEmpty()) {
            holder.tvApodo.visibility = View.GONE
        } else {
            holder.tvApodo.visibility = View.VISIBLE
            holder.tvApodo.text = musico.apodo
        }
        holder.tvEdad.text = "Edad: ${musico.edad} - "
        holder.tvEstado.text = if (musico.estado) "Activo" else "Inactivo"
        holder.tvEstado.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (musico.estado) R.color.green else R.color.red
                                  )
                                    )
        holder.tvGenero.text = musico.generoMusical.toString()
        holder.tvBiografia.text = "Biografía: ${musico.biografia}"
        //holder.imgMusico.setImageResource(musico.image)

        // Configuración de las estrellas (valoración) en el item
        val valoracion = musico.valoracion
        val estrellas = listOf(
            holder.imgVal1,
            holder.imgVal2,
            holder.imgVal3,
            holder.imgVal4,
            holder.imgVal5
                              )

        if (valoracion == null) {
            // Mostrar 5 estrellas completas si no hay valoraciones
            estrellas.forEach { it.setImageResource(R.drawable.star_fill_img) }
        } else {
            val estrellasCompletas = valoracion.toInt()
            val tieneMedia = (valoracion - estrellasCompletas) >= 0.5f

            estrellas.forEachIndexed { index, imageView ->
                imageView.setImageResource(
                    when {
                        index < estrellasCompletas -> R.drawable.star_fill_img
                        index == estrellasCompletas && tieneMedia -> R.drawable.star_half
                        else -> R.drawable.star_black_img
                    }
                                          )
            }
        }

        // Listener para mostrar el dialog con los detalles del músico
        holder.itemView.setOnClickListener {
            showMusicoDialog(holder.itemView.context, musico)
        }
    }

    // Muestra la pantalla con detalle de cada usuario
    @SuppressLint("SetTextI18n")
    private fun showMusicoDialog(context: Context, musico: Musico) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_musico, null)
        val builder = AlertDialog.Builder(context).setView(dialogView).setCancelable(true)

        val imgMusicoCompleto: ImageView = dialogView.findViewById(R.id.imgMusicoCompleto)
        val txtNombreCompleto: TextView = dialogView.findViewById(R.id.txtNombreCompleto)
        val txtApodoCompleto: TextView = dialogView.findViewById(R.id.txtApodoCompleto)
        val txtBiografiaCompleta: TextView = dialogView.findViewById(R.id.txtBiografiaCompleta)
        val txtEdadCompleta: TextView = dialogView.findViewById(R.id.txtEdadCompleta)
        val txtGeneroCompleto: TextView = dialogView.findViewById(R.id.txtGeneroCompleto)
        val txtEstadoCompleto: TextView = dialogView.findViewById(R.id.txtEstadoCompleto)
        val txtGeneroMusicalCompleto: TextView =
            dialogView.findViewById(R.id.txtGeneroMusicalCompleto)
        val imgVal1: ImageView = dialogView.findViewById(R.id.imgVal1)
        val imgVal2: ImageView = dialogView.findViewById(R.id.imgVal2)
        val imgVal3: ImageView = dialogView.findViewById(R.id.imgVal3)
        val imgVal4: ImageView = dialogView.findViewById(R.id.imgVal4)
        val imgVal5: ImageView = dialogView.findViewById(R.id.imgVal5)

        // Set the details in the dialog
        //imgMusicoCompleto.setImageResource(musico.image)
        txtNombreCompleto.text = "${musico.nombre} ${musico.apellido}"
        if (musico.apodo.isNotEmpty()) {
            txtApodoCompleto.text = musico.apodo
            txtApodoCompleto.visibility = View.VISIBLE
        } else {
            txtApodoCompleto.visibility = View.GONE
        }
        txtBiografiaCompleta.text = musico.biografia
        txtEdadCompleta.text = "Edad: ${musico.edad}"
        txtGeneroCompleto.text = "Género: ${musico.generoMusical}"
        txtEstadoCompleto.text = if (musico.estado) "Activo" else "Inactivo"
        txtEstadoCompleto.setTextColor(
            ContextCompat.getColor(
                context,
                if (musico.estado) R.color.green else R.color.red
                                  )
                                      )
        txtGeneroMusicalCompleto.text =
            "Género Musical: ${musico.generoMusical}"

        // Configuración de las estrellas en el dialog (similar a onBindViewHolder)
        val valoracion = musico.valoracion
        val estrellas = listOf(imgVal1, imgVal2, imgVal3, imgVal4, imgVal5)

        if (valoracion == null) {
            // Mostrar 5 estrellas completas si no hay valoraciones
            estrellas.forEach { it.setImageResource(R.drawable.star_fill_img) }
        } else {
            val estrellasCompletas = valoracion.toInt()
            val tieneMedia = (valoracion - estrellasCompletas) >= 0.5f

            estrellas.forEachIndexed { index, imageView ->
                imageView.setImageResource(
                    when {
                        index < estrellasCompletas -> R.drawable.star_fill_img
                        index == estrellasCompletas && tieneMedia -> R.drawable.star_half
                        else -> R.drawable.star_black_img
                    }
                                          )
            }
        }

        // Muestra el dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int = musicos.size
}
