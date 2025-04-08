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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ermonii.IniciarSesion
import com.example.ermonii.R
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragmentLocal : Fragment() {
    private lateinit var btnCerrarSesion: Button
    private lateinit var btn_editarPerfil: Button
    private var usuarioId: Int = -1
    private var localUsuario: Local? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId") ?: -1
    }

    companion object {
        fun newInstance(usuarioId: Int): PerfilFragmentLocal {
            val fragment = PerfilFragmentLocal()
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

        Log.d("PerfilLocal", "Usuario ID: $usuarioId")

        // Llamamos la API dentro de una coroutine
        GlobalScope.launch(Dispatchers.Main) {
            val locales = withContext(Dispatchers.IO) {
                llamarAPILocales()
            }

            Log.d("PerfilLocal", "Locales obtenidos: ${locales}")

            val localEncontrado = locales.find { it.idUsuario == usuarioId }

            if (locales.isNotEmpty()) {
                localUsuario = localEncontrado

                Log.d("PerfilLocal", """
                ===== LOCAL CARGADO =====
                ID: ${localUsuario?.id}
                Nombre: ${localUsuario?.nombre}
                ID Usuario: ${localUsuario?.idUsuario}
            """.trimIndent())

                actualizarUI(view)
            } else {
                Log.e("PerfilLocal", "No se encontró local para usuarioId: $usuarioId")
                Toast.makeText(requireContext(), "No se encontró el local", Toast.LENGTH_SHORT).show()
            }
        }

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



    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun showEditDialog(context: Context) {
        // Infla el diseño del diálogo
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_local, null)

        // Inicializa los EditText y el botón del diálogo
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)

        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombre)
        val edtTipoLocal = dialogView.findViewById<EditText>(R.id.edtTipoLocal)
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreo)
        val edtTelefono = dialogView.findViewById<EditText>(R.id.edtTelefono)
        val edtBiografia = dialogView.findViewById<EditText>(R.id.edtBiografia)

        edtNombre.setText(localUsuario?.nombre)
        edtTipoLocal.setText(localUsuario?.tipoLocal)
        edtCorreo.setText(localUsuario?.correo)
        edtTelefono.setText(localUsuario?.telefono)
        edtBiografia.setText(localUsuario?.descripcion)


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


    @SuppressLint("SetTextI18n")
    private fun actualizarUI(view: View) {
        val txtNombreLocal = view.findViewById<TextView>(R.id.txtNombreLocal)
        val txtBiografia = view.findViewById<TextView>(R.id.txtBiografia)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreo)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefono)
        val txtTipoLocal = view.findViewById<TextView>(R.id.txtTipoLocal)
        val txtFechaRegistro = view.findViewById<TextView>(R.id.txtFechaRegistro)



        txtNombreLocal.text = localUsuario?.nombre
        txtBiografia.text = "Biografía: " + localUsuario?.descripcion
        txtCorreo.text = "Correo: " + localUsuario?.correo
        txtTelefono.text = "Telefono: " + localUsuario?.telefono
        txtTipoLocal.text = "Tipo de local: " + localUsuario?.tipoLocal
        txtFechaRegistro.text = "Fecha de registro: " + localUsuario?.fechaRegistro
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