package com.example.ermonii.FragmentMusico

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ermonii.IniciarSesion
import com.example.ermonii.R

class PerfilFragmentMusico : Fragment() {

    private lateinit var btnCerrarSesion: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        // Inflar el layout y obtener la vista
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)

        // Configurar el listener del botón
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(requireActivity(), IniciarSesion::class.java)
            startActivity(intent)

            requireActivity().finish()
        }

        return view
    }
}
