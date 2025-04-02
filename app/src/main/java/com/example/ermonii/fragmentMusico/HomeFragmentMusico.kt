package com.example.ermonii.fragmentMusico

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Evento
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.RetrofitClient
import kotlinx.coroutines.launch

class HomeFragmentMusico : Fragment() {

    private lateinit var adapter: EventoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(R.layout.fragment_home_musico, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(context)


        // Llamamos a la API antes de inflar el EventoAdapter para tener las listas con los datos
        lifecycleScope.launch {
            try {
                val eventos = llamarAPIEventos()
                val locales = llamarAPILocales()

                // Inicializar el adaptador
                adapter = EventoAdapter(eventos, locales)
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener datos de la API", e)
            }
        }

        return view
    }

    private suspend fun llamarAPIEventos(): List<Evento> {
        return try {
            RetrofitClient.instance.getEventos()  // ✅ Directamente devuelve la lista
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al obtener eventos", e)
            emptyList()
        }
    }

    private suspend fun llamarAPILocales(): List<Local> {
        return try {
            RetrofitClient.instance.getLocales()  // ✅ Directamente devuelve la lista
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al obtener locales", e)
            emptyList()
        }
    }
}