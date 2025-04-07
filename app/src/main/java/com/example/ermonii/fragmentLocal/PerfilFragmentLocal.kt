package com.example.ermonii.fragmentLocal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ermonii.IniciarSesion
import com.example.ermonii.R
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragmentLocal : Fragment() {
    private lateinit var btnCerrarSesion: Button
    private lateinit var btn_editarPerfil: Button
    private lateinit var local: Local

    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId") ?: -1
    }

    companion object {
        fun newInstance(usuarioId: Int): CrearEventoFragmentLocal {
            val fragment = CrearEventoFragmentLocal()
            val args = Bundle()
            args.putInt("usuarioId", usuarioId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil_local, container, false)

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



    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(context: Context) {
        // Infla el diseño del diálogo
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_local, null)

        // Inicializa los EditText y el botón del diálogo
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


    private fun actualizarUI(view: View) {
        val txtNombreLocal = view.findViewById<TextView>(R.id.txtNombreLocal)

        txtNombreLocal.text = local.nombre
    }

    private suspend fun obtenerDatosLocal(view: View) {
        try {
            val listaLocal = RetrofitClient.instance.getLocales() // ✅ Ahora directamente devuelve la lista

            // Buscar el local cuyo idUsuario coincida con usuarioId
            val localEncontrado = listaLocal.find { it.idUsuario == usuarioId }

            if (localEncontrado != null) {
                local = localEncontrado
                actualizarUI(view) // ✅ Se actualiza la UI con el nuevo local
            } else {
                Log.e("PerfilFragmentMusico", "No se encontró local para usuarioId: $usuarioId")
                Toast.makeText(requireContext(), "No se encontró el local", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("PerfilFragmentMusico", "Error al obtener los datos del local", e)
        }
    }

}