package com.example.ermonii

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.preference.PreferenceManager
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MapaFragment : Fragment(), LocationListener {

    val PERMISSIONS_CODE = 1
    lateinit var mapView: MapView
    var markanadida = false
    var ubicado = false
    private var ubicacionMarker: Marker? = null

    val localesList = listOf(
        Locales(
            id = 1,
            name = "Parque Central",
            description = "Un hermoso parque en el centro de la ciudad.",
            image = R.drawable.parque_central,
            PZCAT_LATITUDE = 19.4326,
            PZCAT_LONGITUDE = -99.1332,
            rating = 4
        ),
        Locales(
            id = 2,
            name = "Museo de Arte",
            description = "Museo que alberga obras de arte contemporáneo.",
            image = R.drawable.museo_de_arte,
            PZCAT_LATITUDE = 19.4269,
            PZCAT_LONGITUDE = -99.1710,
            rating = 5
        ),
        Locales(
            id = 3,
            name = "Ikea",
            description = "Compra tus muebles",
            image = R.drawable.ikea,
            PZCAT_LATITUDE = 37.461999831458925,
            PZCAT_LONGITUDE = -122.13925518445407,
            rating = 3
        ),
        Locales(
            id = 4,
            name = "Ikea2",
            description = "Compra tus muebles2",
            image = R.drawable.ikea,
            PZCAT_LATITUDE = 37.42199682546436,
            PZCAT_LONGITUDE = -122.08087202787353,
            rating = 2

        ),
        Locales(
            id = 4,
            name = "Plaça Cataluña",
            description = "Plaza central con árboles y esculturas, rodeada de tiendas y cafeterías, que se usa para eventos especiales.",
            image = R.drawable.plaza_catalunya,
            PZCAT_LATITUDE = 41.38702493692096,
            PZCAT_LONGITUDE = 2.1700450818313377,
            rating = 5

        )
    )

    companion object {
        private const val DEFAULT_ZOOM = 17.0

        // Coordenadas Plaza Cataluña
        private const val PZCAT_LATITUDE = 41.3870
        private const val PZCAT_LONGITUDE = 2.1700

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapaFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_mapa, container, false)

        // Inicializar el MapView
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        mapView = view.findViewById(R.id.MapView)
        mapView.setMultiTouchControls(true)

        mapView.setOnTouchListener { _, _ ->
            mapView.overlays.forEach { overlay ->
                if (overlay is Marker) {
                    overlay.closeInfoWindow() // Close the info window of the marker
                }
            }
            false
        }

        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_CODE)
        } else {
            initializeLocationUpdates()
        }

        if (!ubicado) {
            initializeLocationUpdates()
            ubicado = true
        }

        // Configurar el botón para actualizar la ubicación
        val button = view.findViewById<Button>(R.id.btnUpdate)
        button.setOnClickListener {
            initializeLocationUpdates()
        }

        return view
    }

    private fun initializeLocationUpdates() {
        val locationManager = requireContext().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                locatePoint(location)
                ponerMarcasLocales(PZCAT_LATITUDE, PZCAT_LONGITUDE)
                addUbicacioMarker(location)
            } else {
                Toast.makeText(requireContext(), "Last known location is null, waiting for location update...", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUbicacioMarker(location: Location?) {
        // Elimina el marcador "Yo" existente si ya está añadido
        ubicacionMarker?.let {
            mapView.overlays.remove(it) // Elimina el marcador "Yo" del mapa.
            mapView.invalidate() // Redibuja el mapa para reflejar los cambios.
        }

        // Crea y añade un nuevo marcador "Yo"
        if (location != null) {
            val marker = Marker(mapView)
            marker.position = GeoPoint(location.latitude, location.longitude)
            marker.title = "Yo"
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.yo_mark) // Cambia "yo_mark" por tu drawable.
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker) // Añade el marcador al mapa.
            mapView.invalidate() // Redibuja el mapa.

            // Almacena el nuevo marcador "Yo"
            ubicacionMarker = marker
        } else {
            Toast.makeText(requireContext(), "Ubicación no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ponerMarcasLocales(lat: Double, lon: Double) {
        for (local in localesList) {
            val marker = Marker(mapView)
            marker.position = GeoPoint(local.PZCAT_LATITUDE, local.PZCAT_LONGITUDE)
            marker.title = local.name
            marker.snippet = local.description
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.location_mark)
            marker.infoWindow = CustomInfoWindow(mapView)
            marker.setRelatedObject(local) // Asociar el objeto Locales al marcador
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    override fun onLocationChanged(location: Location) {
        locatePoint(location)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                initializeLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun locatePoint(location: Location?) {
        if (location != null) {
            val mapController = mapView.controller
            mapController.setZoom(DEFAULT_ZOOM)
            mapController.setCenter(GeoPoint(location.latitude, location.longitude))
        } else {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }
    }
}