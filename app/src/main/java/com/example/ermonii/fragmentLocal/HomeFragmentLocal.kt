package com.example.ermonii.fragmentLocal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Musico
import com.example.ermonii.fragmentMusico.EventoAdapter
import com.google.type.DateTime
import java.util.Date

class HomeFragmentLocal : Fragment() {
    private lateinit var adapter: MusicoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        val view = inflater.inflate(R.layout.fragment_home_local, container, false)


        // Configurar el RecyclerView
        /*val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMusicos)
        adapter = MusicoAdapter(musicosList) // Usa tu lista existente

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter*/

        return view
    }
}