package com.example.ermonii.fragmentMusico

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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ermonii.IniciarSesion
import com.example.ermonii.R
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragmentMusico : Fragment() {

    private lateinit var btnCerrarSesion: Button
    private lateinit var btn_editarPerfil: Button
    private var usuarioId: Int = -1
    private lateinit var musico: Musico


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        // Inflar el layout y obtener la vista
        val view = inflater.inflate(R.layout.fragment_perfil_musico, container, false)

        // Obtener el usuarioId desde los argumentos
        obtenerIDUsuarioIniciado()

        // Logs de comprobación
        Log.e("Usuario_ID", "Usuario Valido ID: " + usuarioId)
        if (usuarioId == -1) {
            Log.e("PerfilFragmentMusico", "Error: usuarioId inválido")
            return view
        }

        // Ejecución de funciones principales
        configurarBotonCerrarSesion(view)
        configurarBotonEditar(view)
        obtenerDatosMusico(view)

        return view
    }

    // Obtiene los datos del usuario que ha iniciado sesión desde la API
    private fun obtenerDatosMusico(view: View) {
        RetrofitClient.instance.getMusicos().enqueue(object : Callback<List<Musico>> {
            override fun onResponse(call: Call<List<Musico>>, response: Response<List<Musico>>) {
                if (response.isSuccessful) {
                    val listaMusicos = response.body()

                    // Buscar el músico cuyo idUsuario coincida con usuarioId
                    val musicoEncontrado = listaMusicos?.find { it.idUsuario == usuarioId }

                    if (musicoEncontrado != null) {
                        musico = musicoEncontrado
                        actualizarUI(view)
                    } else {
                        Log.e("PerfilFragmentMusico", "No se encontró músico para usuarioId: $usuarioId")
                        Toast.makeText(requireContext(), "No se encontró el músico", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("PerfilFragmentMusico", "Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<List<Musico>>, t: Throwable) {
                Log.e("PerfilFragmentMusico", "Error de conexión: ${t.message}")
            }
        })
    }

    // Confiugra la información del usuario en el View
    @SuppressLint("SetTextI18n")
    private fun actualizarUI(view: View) {
        view.findViewById<TextView>(R.id.txtNombreCompleto).text = "${musico.nombre} ${musico.apellido}"
        view.findViewById<TextView>(R.id.txtNombreArtistico).text = "Apodo: ${musico.apodo}"
        view.findViewById<TextView>(R.id.txtBiografia).text = "Biografía: ${musico.biografia}"
        view.findViewById<TextView>(R.id.txtCorreo).text = "Correo: ${musico.correo}"
        view.findViewById<TextView>(R.id.txtTelefono).text = "Teléfono: ${musico.telefono}"
        view.findViewById<TextView>(R.id.txtGenero).text = "Género: ${musico.generoMusical}"
        view.findViewById<TextView>(R.id.txtFechaRegistro).text = "Fecha de registro: ${musico.fechaRegistro}"
    }

    // Configuración del botón de cerrar sesión
    private fun configurarBotonCerrarSesion(view: View) {
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(requireActivity(), IniciarSesion::class.java)
            startActivity(intent)

            requireActivity().finish()
        }
    }

    //Configuración del botón de editar perfil
    private fun configurarBotonEditar(view: View) {
        btn_editarPerfil = view.findViewById(R.id.btn_editarPerfil)
        btn_editarPerfil.setOnClickListener {
            showEditDialog(requireContext())
        }
    }

    // Mostrar dialogo cuando editamos el perfil
    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(context: Context) {
        // Infla el diseño del diálogo
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_musico, null)

        // Inicializa los EditText y el botón del diálogo
        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombre)
        val edtApellido = dialogView.findViewById<EditText>(R.id.edtApellidos)
        val edtApodo = dialogView.findViewById<EditText>(R.id.edtApodo)
        val edtGenero = dialogView.findViewById<EditText>(R.id.edtGenero)
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreo)
        val edtTelefono = dialogView.findViewById<EditText>(R.id.edtTelefono)
        val edtBiografia = dialogView.findViewById<EditText>(R.id.edtBiografia)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val txtGenerosMusicales = dialogView.findViewById<TextView>(R.id.txtGenerosMusicales)

        // Rellenar EditText
        edtNombre.setText(musico.nombre)
        edtApellido.setText(musico.apellido)
        edtApodo.setText(musico.apodo)
        edtGenero.setText(musico.genero)
        edtCorreo.setText(musico.correo)
        edtTelefono.setText(musico.telefono)
        edtBiografia.setText(musico.biografia)
        txtGenerosMusicales.text = musico.generoMusical

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

    // Obtención de la ID del usuario que ha iniciado sesión
    private fun obtenerIDUsuarioIniciado() {
        arguments?.let {
            usuarioId = it.getInt("usuario", -1)
        }
    }
}
