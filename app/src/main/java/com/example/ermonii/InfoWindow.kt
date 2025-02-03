package com.example.ermonii

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomInfoWindow(mapView: MapView) : InfoWindow(R.layout.custom_info_window, mapView) {

    override fun onOpen(item: Any?) {
        val marker = item as? Marker ?: return

        val imageView = mView.findViewById<ImageView>(R.id.info_window_image)
        val titleView = mView.findViewById<TextView>(R.id.info_window_title)
        val snippetView = mView.findViewById<TextView>(R.id.info_window_snippet)
        val ratingStarsLayout = mView.findViewById<LinearLayout>(R.id.info_window_rating_stars)

        // Asignar el título y la descripción del marcador a las vistas
        titleView.text = marker.title
        snippetView.text = marker.snippet

        // Recuperar la imagen asociada al marcador
        val local = marker.relatedObject as? Locales // Recupera el objeto Locales

        // Cargar la imagen desde los recursos locales
        if (local != null) {
            imageView.setImageResource(local.image) // Cargar desde recursos locales

            // Mostrar la puntuación en estrellas
            ratingStarsLayout.removeAllViews() // Limpiar las estrellas anteriores
            for (i in 1..local.rating) {
                val starImageView = ImageView(mView.context)
                starImageView.setImageResource(R.drawable.star) // Cambia "ic_star" por tu drawable de estrella
                starImageView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                ratingStarsLayout.addView(starImageView)
            }
        } else {
            imageView.setImageResource(R.drawable.logodurum) // Imagen por defecto
        }
    }

    override fun onClose() {
        // No es necesario hacer nada aquí
    }
}