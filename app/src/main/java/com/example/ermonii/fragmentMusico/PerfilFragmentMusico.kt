package com.example.ermonii.fragmentMusico

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ermonii.IniciarSesion
import com.example.ermonii.R

class PerfilFragmentMusico : Fragment() {

    private lateinit var btnCerrarSesion: Button
    private lateinit var btn_editarPerfil: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        // Inflar el layout y obtener la vista
        val view = inflater.inflate(R.layout.fragment_perfil_musico, container, false)

        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)

        // Configurar el listener del botón
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(requireActivity(), IniciarSesion::class.java)
            startActivity(intent)

            requireActivity().finish()
        }

        btn_editarPerfil = view.findViewById(R.id.btn_editarPerfil)

        btn_editarPerfil.setOnClickListener {
            showEditDialog(requireContext())
        }

        return view
    }

    private fun showEditDialog(context: Context) {
        // Infla el diseño del diálogo
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_musico, null)

        // Inicializa los EditText y el botón del diálogo
        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombre)
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreo)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)

        // Crea el AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)

        // Crea el AlertDialog
        val dialog = dialogBuilder.create()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        // Muestra el diálogo
        dialog.show()
    }

}
