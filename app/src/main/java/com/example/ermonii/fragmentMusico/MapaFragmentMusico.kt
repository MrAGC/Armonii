package com.example.ermonii.fragmentMusico

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
import com.example.ermonii.clases.Local
import com.example.ermonii.R
import java.util.Date

class MapaFragmentMusico : Fragment(), LocationListener {

    val PERMISSIONS_CODE = 1
    lateinit var mapView: MapView
    var markanadida = false
    var ubicado = false
    private var ubicacionMarker: Marker? = null

    val localesList = listOf(
        Local(
            id = 1,
            name = "Parque Central",
            correo = "parque@email.com",
            contrasenya = "parque123",
            telefono = 123456789,
            latitud = 19.4326f,
            longitud = -99.1332f,
            fechaRegistro = Date(),
            estado = true,
            chat = listOf(),
            valoracion = 4.0f,
            description = "Un hermoso parque en el centro de la ciudad.",
            image = R.drawable.parque_central,
            PZCAT_LATITUDE = 19.4326,
            PZCAT_LONGITUDE = -99.1332,
            rating = 4
             ),
        Local(
            id = 2,
            name = "Museo de Arte",
            correo = "museo@email.com",
            contrasenya = "museo123",
            telefono = 234567890,
            latitud = 19.4269f,
            longitud = -99.1710f,
            fechaRegistro = Date(),
            estado = true,
            chat = listOf(),
            valoracion = 5.0f,
            description = "Museo que alberga obras de arte contemporáneo.",
            image = R.drawable.museo_de_arte,
            PZCAT_LATITUDE = 19.4269,
            PZCAT_LONGITUDE = -99.1710,
            rating = 5
             ),
        Local(
            id = 3,
            name = "Ikea",
            correo = "ikea@email.com",
            contrasenya = "ikea123",
            telefono = 345678901,
            latitud = 37.461999831458925f,
            longitud = -122.13925518445407f,
            fechaRegistro = Date(),
            estado = true,
            chat = listOf(),
            valoracion = 3.0f,
            description = "Compra tus muebles",
            image = R.drawable.ikea,
            PZCAT_LATITUDE = 37.461999831458925,
            PZCAT_LONGITUDE = -122.13925518445407,
            rating = 3
             ),
        Local(
            id = 4,
            name = "Ikea2",
            correo = "ikea2@email.com",
            contrasenya = "ikea2123",
            telefono = 456789012,
            latitud = 37.42199682546436f,
            longitud = -122.08087202787353f,
            fechaRegistro = Date(),
            estado = true,
            chat = listOf(),
            valoracion = 2.0f,
            description = "Compra tus muebles2",
            image = R.drawable.ikea,
            PZCAT_LATITUDE = 37.42199682546436,
            PZCAT_LONGITUDE = -122.08087202787353,
            rating = 2
             ),
        Local(
            id = 5,
            name = "Plaça Cataluña",
            correo = "plaza@email.com",
            contrasenya = "plaza123",
            telefono = 567890123,
            latitud = 41.38702493692096f,
            longitud = 2.1700450818313377f,
            fechaRegistro = Date(),
            estado = true,
            chat = listOf(),
            valoracion = 5.0f,
            description = "Plaza central con árboles y esculturas, rodeada de tiendas y cafeterías, que se usa para eventos especiales.",
            image = R.drawable.plaza_catalunya,
            PZCAT_LATITUDE = 41.38702493692096,
            PZCAT_LONGITUDE = 2.1700450818313377,
            rating = 5
             )
                            )

    companion object {
        private const val DEFAULT_ZOOM = 17.0
        private const val PZCAT_LATITUDE = 41.3870
        private const val PZCAT_LONGITUDE = 2.1700

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapaFragmentMusico().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retener la instancia del fragmento
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_mapa_musico, container, false)

        // Reinicializar el MapView
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        mapView = view.findViewById(R.id.MapView)
        mapView.setMultiTouchControls(true)

        // Establecer el zoom mínimo y máximo
        mapView.minZoomLevel = 5.0
        mapView.maxZoomLevel = 20.0

        // Configurar el listener para deshabilitar gestos de ViewPager2 cuando se interactúa con el mapa
        mapView.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // Deshabilitar gestos de ViewPager2
                    (requireActivity() as? MenuActivityMusico)?.disableViewPagerGestures()
                }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    // Habilitar gestos de ViewPager2
                    (requireActivity() as? MenuActivityMusico)?.enableViewPagerGestures()

                    // Cerrar las ventanas de información de los marcadores al pulsar fuera de ellos
                    mapView.overlays.forEach { overlay ->
                        if (overlay is Marker) {
                            overlay.closeInfoWindow() // Cierra la ventana de información del marcador
                        }
                    }
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
            try {
                // Verificar si los permisos están concedidos
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    // Si los permisos están concedidos, iniciar actualizaciones de ubicación
                    initializeLocationUpdates()
                } else {
                    // Si los permisos no están concedidos, solicitarlos nuevamente
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_CODE
                                                     )
                    Toast.makeText(requireContext(), "Permiso de ubicación requerido para actualizar", Toast.LENGTH_LONG).show()
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error de seguridad: Verifica los permisos en configuración", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error inesperado al actualizar la ubicación", Toast.LENGTH_LONG).show()
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // Reiniciar el MapView cuando el fragmento se vuelve a mostrar
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Pausar el MapView cuando el fragmento se oculta
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach() // Liberar recursos del MapView cuando el fragmento se destruye
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
                Toast.makeText(requireContext(), "Esperando actualización de ubicación...", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUbicacioMarker(location: Location?) {
        ubicacionMarker?.let {
            mapView.overlays.remove(it)
            mapView.invalidate()
        }

        if (location != null) {
            val marker = Marker(mapView)
            marker.position = GeoPoint(location.latitude, location.longitude)
            marker.title = "Yo"
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.yo_mark)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
            mapView.invalidate()
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
            marker.setRelatedObject(local)
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
                Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show()
                initializeLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun locatePoint(location: Location?) {
        if (location != null) {
            val mapController = mapView.controller
            mapController.setZoom(DEFAULT_ZOOM)
            mapController.setCenter(GeoPoint(location.latitude, location.longitude))
        } else {
            Toast.makeText(requireContext(), "Ubicación no encontrada", Toast.LENGTH_SHORT).show()
        }
    }
}




