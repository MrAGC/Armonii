package com.example.ermonii.fragmentLocal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentLocal : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MusicoAdapter
    private val listaMusicos = mutableListOf<Musico>()

    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId") ?: -1
    }

    companion object {
        fun newInstance(usuarioId: Int): HomeFragmentLocal {
            val fragment = HomeFragmentLocal()
            val args = Bundle()
            args.putInt("usuarioId", usuarioId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        val view = inflater.inflate(R.layout.fragment_home_local, container, false)

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMusicos) // Asegúrate que el ID sea correcto
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MusicoAdapter(listaMusicos)
        recyclerView.adapter = adapter

        // Cargar datos desde la API
        obtenerDatosMusico { musicos ->
            listaMusicos.clear()
            listaMusicos.addAll(musicos)
            adapter.notifyDataSetChanged()
        }

        return view
    }

    // Llamada a API para músicos
    private fun obtenerDatosMusico(onResult: (List<Musico>) -> Unit) {
        RetrofitClient.instance.getMusicos().enqueue(object : Callback<List<Musico>> {
            override fun onResponse(call: Call<List<Musico>>, response: Response<List<Musico>>) {
                if (response.isSuccessful) {
                    val musicos = response.body() ?: emptyList()
                    onResult(musicos)
                } else {
                    Log.e("HomeFragmentLocal", "Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<List<Musico>>, t: Throwable) {
                Log.e("HomeFragmentLocal", "Error de conexión: ${t.message}")
            }
        })
    }
}
