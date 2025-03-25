package com.example.ermonii.fragmentMusico

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Evento
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.RetrofitClient
import com.example.ermonii.clases.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentMusico : Fragment() {
    companion object {
        var locales = emptyList<Local>()
        var eventos = emptyList<Evento>()
    }

    private lateinit var adapter: EventoAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(R.layout.fragment_home_musico, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializar el adaptador
        val adapter = EventoAdapter(eventos, locales)
        recyclerView.adapter = adapter

        // Obtenemos los locales desde la API
        RetrofitClient.instance.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body() ?: emptyList()

                    // Convertir usuarios tipo "Local" a instancias de Local
                    locales = usuarios.filter { it.tipo == "Local" }.map { usuario ->
                        Local(
                            id = usuario.id,
                            nombre = usuario.nombre,
                            correo = usuario.correo,
                            contrasenya = usuario.contrasenya,
                            telefono = usuario.telefono,
                            latitud = usuario.latitud,
                            longitud = usuario.longitud,
                            fechaRegistro = usuario.fechaRegistro,
                            estado = usuario.estado,
                            chat = usuario.chat,
                            valoracion = usuario.valoracion,
                            image = 0,
                            rating = 0
                             )
                    }

                    // Imprimir en Log para verificar
                    locales.forEach { local ->
                        Log.d("HOME_FRAGMENT_MUSICO", """
                ===== LOCAL =====
                ID: ${local.id}
                Nombre: ${local.nombre}
                Correo: ${local.correo}
                Teléfono: ${local.telefono}
                Latitud: ${local.latitud}
                Longitud: ${local.longitud}
                Fecha Registro: ${local.fechaRegistro}
                Estado: ${local.estado}
                Valoración: ${local.valoracion}
                Image: ${local.image}
                Rating: ${local.rating}
                """.trimIndent())
                    }
                } else {
                    Log.e("HOME_FRAGMENT_MUSICO", "Error al obtener usuarios: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Log.e("HOME_FRAGMENT_MUSICO", "Error en la llamada: ${t.message}", t)
            }
        })




        // Obtener eventos desde la API
        val apiService = RetrofitClient.instance
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    eventos = response.body() ?: emptyList()

                    // Mostrar eventos en el Logcat
                    eventos.forEach { evento ->
                        Log.d("HOME_FRAGMENT_MUSICO", """
                    ===== EVENTO =====
                    ID: ${evento.id}
                    Nombre: ${evento.nombre}
                    Fecha: ${evento.fecha}
                    Descripción: ${evento.descripcion}
                    Duración: ${evento.duracion} minutos
                    Estado: ${evento.estado}
                    Local ID: ${locales.find { it.id == evento.local }}
                """.trimIndent())
                    }

                    adapter.updateList(eventos)
                } else {
                    Log.e("HOME_FRAGMENT_MUSICO", "Respuesta no exitosa: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                Log.e("HOME_FRAGMENT_MUSICO", "Error en la llamada: ${t.message}", t)
            }
        })

        return view
    }
}