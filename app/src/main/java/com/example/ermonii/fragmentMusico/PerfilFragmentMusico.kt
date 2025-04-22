package com.example.ermonii.fragmentMusico

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.ermonii.clases.ApiService
import com.example.ermonii.clases.DataTransferObjectUsuario
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        view.findViewById<TextView>(R.id.txtNombreArtistico).text = musico.apodo
        view.findViewById<TextView>(R.id.txtBiografia).text = musico.biografia
        view.findViewById<TextView>(R.id.txtCorreo).text = musico.correo
        view.findViewById<TextView>(R.id.txtTelefono).text = musico.telefono
        view.findViewById<TextView>(R.id.txtGenero).text = musico.genero
        view.findViewById<TextView>(R.id.txtFechaRegistro).text = musico.fechaRegistro
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
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreo)
        val edtTelefono = dialogView.findViewById<EditText>(R.id.edtTelefono)
        val edtBiografia = dialogView.findViewById<EditText>(R.id.edtBiografia)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)
        val txtGenerosMusicales = dialogView.findViewById<TextView>(R.id.txtGenerosMusicales)
        val edtFechaNacimiento = dialogView.findViewById<EditText>(R.id.edtFechaNacimiento)

        // Configuramos el TextView del genero desplegable en pantalla
        val edtGenero = dialogView.findViewById<TextView>(R.id.edtGenero)
        val opcionesGenero = arrayOf("Masculino", "Femenino", "Otro")

        edtGenero.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Selecciona tu género")
            builder.setItems(opcionesGenero) { _, which ->
                edtGenero.text = opcionesGenero[which]
            }
            builder.show()
        }

        // Fecha
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        edtFechaNacimiento.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                edtFechaNacimiento.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        // Rellenar EditText
        edtNombre.setText(musico.nombre)
        edtApellido.setText(musico.apellido)
        edtApodo.setText(musico.apodo)
        edtGenero.setText(musico.genero)
        edtCorreo.setText(musico.correo)
        edtTelefono.setText(musico.telefono)
        edtBiografia.setText(musico.biografia)
        txtGenerosMusicales.text = musico.generoMusical.toString().replace("[", "").replace("]", "").replace(", ", " - ")

        // Crea el AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)

        // Crea el AlertDialog
        val dialog = dialogBuilder.create()

        btnConfirmar.setOnClickListener {
            val edadCalculada = calcularEdad(edtFechaNacimiento.text.toString())

            val musicoEditado = musico.valoracion?.let { valoracion ->
                DataTransferObjectUsuario(
                    id = musico.id,
                    nombre = edtNombre.text.toString(),
                    correo = edtCorreo.text.toString(),
                    contrasenya = musico.contrasenya,
                    telefono = edtTelefono.text.toString(),
                    latitud = 0.0,
                    longitud = 0.0,
                    fechaRegistro = null,
                    estado = true,
                    valoracion = valoracion,
                    tipo = "",
                    apodo = edtApodo.text.toString(),
                    apellido = edtApellido.text.toString(),
                    genero = edtGenero.text.toString(),
                    edad = edadCalculada,
                    biografia = edtBiografia.text.toString(),
                    imagen = "",
                    idUsuario = musico.idUsuario,
                    generosMusicales = musico.generoMusical,
                    direccion = "",
                    tipo_local = "",
                    HorarioApertura = null,
                    HorarioCierre = null,
                    descripcion = ""
                                        )
            }

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (musicoEditado != null) {
                        val response = RetrofitClient.instance.actualizarMusico(musico.id, musicoEditado)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Log.d("Retrofit", "Músico actualizado correctamente")
                            } else {
                                Log.e("Retrofit", "Error al actualizar el músico: ${response.message()}")
                            }
                        }
                    } else {
                        Log.e("Retrofit", "El musicoEditado es nulo")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("Retrofit", "Error de red: ${e.message}")
                    }
                }
            }


        }

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

    fun calcularEdad(fechaNacimiento: String): Int {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false // Para evitar que acepte fechas inválidas

        return try {
            val fechaNac = formato.parse(fechaNacimiento) ?: return -1
            val hoy = Calendar.getInstance()
            val nacimiento = Calendar.getInstance().apply { time = fechaNac }

            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

            // Verifica si aún no ha cumplido años este año
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
                edad--
            }

            edad
        } catch (e: Exception) {
            e.printStackTrace()
            -1 // Retorna -1 en caso de error
        }
    }
}
