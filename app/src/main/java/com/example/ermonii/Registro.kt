package com.example.ermonii

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import android.util.Log
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import com.example.ermonii.fragmentMusico.MenuActivityMusico
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class Registro : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Declaraciones
        var musico = false
        var seleccionado = false
        val LLType = findViewById<LinearLayout>(R.id.LLType)
        val imgMusico = findViewById<ImageView>(R.id.imgMusico)
        val imgLocal = findViewById<ImageView>(R.id.imgLocal)
        val btnType = findViewById<Button>(R.id.btnContinuarType)
        val LLNombre = findViewById<LinearLayout>(R.id.LLNombre)
        val btnNombre = findViewById<Button>(R.id.btnContinuarNombre)
        val LLApodo = findViewById<LinearLayout>(R.id.LLApodo)
        val btnApodo = findViewById<Button>(R.id.btnContinuarApodo)
        val LLApellido = findViewById<LinearLayout>(R.id.LLApellido)
        val btnApellido = findViewById<Button>(R.id.btnContinuarApellido)
        val LLCorreo = findViewById<LinearLayout>(R.id.LLCorreo)
        val btnCorreo = findViewById<Button>(R.id.btnContinuarCorreo)
        val LLEdad = findViewById<LinearLayout>(R.id.LLEdad)
        val btnEdad = findViewById<Button>(R.id.btnContinuarEdad)

        // Obtén la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val LLTelefono = findViewById<LinearLayout>(R.id.LLTelefono)
        val btnTelefono = findViewById<Button>(R.id.btnContinuarTelefono)
        val LLGenero = findViewById<LinearLayout>(R.id.LLGenero)
        val btnGenero = findViewById<Button>(R.id.btnContinuarGenero)
        val LLContrasena = findViewById<LinearLayout>(R.id.LLContrasena)
        val btnContrasena = findViewById<Button>(R.id.btnContinuarContrasena)
        val rbAceptarTerminos = findViewById<RadioButton>(R.id.radioButtonAceptarTerminos)
        val txtTerminos = findViewById<TextView>(R.id.txtTerminosCondiciones)
        val LLTipoLocal = findViewById<LinearLayout>(R.id.LLTipoLocal)
        val btnTipoLocal = findViewById<Button>(R.id.btnContinuarTipoLocal)

        val btnVolverType = findViewById<Button>(R.id.btnVolverType)
        val btnNombreVolver = findViewById<Button>(R.id.btnVolverNombre)
        val btnApodoVolver = findViewById<Button>(R.id.btnVolverApodo)
        val btnApellidoVolver = findViewById<Button>(R.id.btnVolverApellido)
        val btnCorreoVolver = findViewById<Button>(R.id.btnVolverCorreo)
        val btnEdadVolver = findViewById<Button>(R.id.btnVolverEdad)
        val btnTelefonoVolver = findViewById<Button>(R.id.btnVolverTelefono)
        val btnGeneroVolver = findViewById<Button>(R.id.btnVolverGenero)
        val btnContrasenaVolver = findViewById<Button>(R.id.btnVolverContrasena)
        val btnTipoLocalVolver = findViewById<Button>(R.id.btnVolverTipoLocal)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtApodo = findViewById<EditText>(R.id.edtApodo)
        val edtApellido = findViewById<EditText>(R.id.edtApellido)
        val edtCorreo = findViewById<EditText>(R.id.edtCorreo)
        val edtEdad = findViewById<EditText>(R.id.edtEdad)
        val edtTelefono = findViewById<EditText>(R.id.edtTelefono)
        val edtContrasena = findViewById<EditText>(R.id.edtContrasena)
        val edtContrasenaConf = findViewById<EditText>(R.id.edtContrasenaConf)
        val txtTerminosCondiciones = findViewById<TextView>(R.id.txtTerminosCondiciones)


        // Configuramos el TextView del genero desplegable en pantalla
        val SpnGenero = findViewById<TextView>(R.id.SpnGenero)
        val opcionesGenero = arrayOf("Masculino", "Femenino", "Otro")

        SpnGenero.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Selecciona tu género")
            builder.setItems(opcionesGenero) { _, which ->
                SpnGenero.text = opcionesGenero[which]
            }
            builder.show()
        }


        // Configuramos el spinner del tipo de local
        val txtTipoLocal = findViewById<TextView>(R.id.txtTipoLocal) // Cambia Spinner por TextView
        val opcionesTipoLocal = arrayOf(
            "Restaurante", "Bar", "Cafetería", "Discoteca", "Librería",
            "Tienda de ropa", "Supermercado", "Tienda de electrónica",
            "Floristería", "Salón de belleza", "Gimnasio", "Hotel",
            "Centro de eventos", "Otro"
                                       )

        // Al hacer clic, muestra un AlertDialog con las opciones
        txtTipoLocal.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Selecciona tu tipo de local")
            builder.setItems(opcionesTipoLocal) { _, which ->
                txtTipoLocal.text = opcionesTipoLocal[which]
            }
            builder.show()
        }


        // Escogemos músico o local
        imgMusico.setOnClickListener {
            imgMusico.setBackgroundResource(R.drawable.redondear_registro_icons_presionado)
            imgLocal.setBackgroundResource(R.drawable.redondear_registro_icons)
            musico = true
            seleccionado = true
        }
        imgLocal.setOnClickListener {
            imgMusico.setBackgroundResource(R.drawable.redondear_registro_icons)
            imgLocal.setBackgroundResource(R.drawable.redondear_registro_icons_presionado)
            musico = false
            seleccionado = true
        }

        // Hacemos visible nuestra UI de registro
        LLType.visibility = View.VISIBLE

        // Función para animar la transición entre vistas
        fun animateTransition(fromView: View, toView: View) {
            val fadeOut = ObjectAnimator.ofFloat(fromView, "alpha", 1f, 0f).setDuration(300)
            val fadeIn = ObjectAnimator.ofFloat(toView, "alpha", 0f, 1f).setDuration(300)
            fadeOut.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    fromView.visibility = View.GONE
                    toView.visibility = View.VISIBLE
                    fadeIn.start()
                }
            })
            fadeOut.start()
        }

        // Función para configurar el botón de volver en las vistas
        fun setBackButtonListener(
            button: Button,
            fromView: View,
            toView: View,
            editText: EditText?
                                 ) {
            button.setOnClickListener {
                animateTransition(fromView, toView)
                editText?.setBackgroundResource(R.drawable.redondear_edittext)
            }
        }

        // Manejo del clic en los términos y condiciones
        txtTerminos.setOnClickListener {
            rbAceptarTerminos.isChecked = !rbAceptarTerminos.isChecked
        }

        // Mostrar el DatePickerDialog al hacer clic en el campo de edad
        edtEdad.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    edtEdad.setText(selectedDate)
                }, year, month, day)
            datePickerDialog.show()
        }

        // Manejo de la selección de tipo de usuario y navegación entre vistas
        btnType.setOnClickListener {
            if (!seleccionado) {
                Toast.makeText(
                    this, "Selecciona una opción por favor", Toast.LENGTH_SHORT
                              ).show()
            } else {
                animateTransition(LLType, LLNombre)
                if (musico) { // Musico
                    btnNombre.setOnClickListener {
                        if (edtNombre.text.isEmpty()) {
                            Toast.makeText(this, "Inserta un nombre", Toast.LENGTH_SHORT).show()
                            edtNombre.setBackgroundResource(R.drawable.redondear_edittext_error)
                        } else {
                            edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                            animateTransition(LLNombre, LLApodo)
                        }
                    }
                    btnApodo.setOnClickListener { animateTransition(LLApodo, LLApellido) }
                    btnApellido.setOnClickListener { animateTransition(LLApellido, LLCorreo) }
                    btnCorreo.setOnClickListener {
                        if (edtCorreo.text.isEmpty()) {
                            Toast.makeText(this, "Inserta un correo", Toast.LENGTH_SHORT).show()
                            edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                        } else {
                            edtCorreo.setBackgroundResource(R.drawable.redondear_edittext)
                            animateTransition(LLCorreo, LLEdad)
                        }
                    }
                    btnEdad.setOnClickListener {
                        if (edtEdad.text.isEmpty()) {
                            Toast.makeText(
                                this,
                                "Inserta una fecha de nacimiento",
                                Toast.LENGTH_SHORT
                                          ).show()
                            edtEdad.setBackgroundResource(R.drawable.redondear_edittext_error)
                        } else {
                            edtEdad.setBackgroundResource(R.drawable.redondear_edittext)
                            animateTransition(LLEdad, LLTelefono)
                        }
                    }
                    btnTelefono.setOnClickListener { animateTransition(LLTelefono, LLGenero) }
                    btnGenero.setOnClickListener {
                        if (SpnGenero.text.toString() == "Selecciona tu género") {
                            Toast.makeText(this, "Inserta una opción", Toast.LENGTH_SHORT).show()
                            SpnGenero.setBackgroundResource(R.drawable.redondear_edittext_error)
                        } else {
                            SpnGenero.setBackgroundResource(R.drawable.redondear_edittext)
                            animateTransition(LLGenero, LLContrasena)
                        }
                    }
                    btnContrasena.setOnClickListener {
                        if (edtContrasena.text.toString() == edtContrasenaConf.text.toString()) {
                            if (edtContrasena.text.length < 8) {
                                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                                edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext_error)
                                Toast.makeText(
                                    this,
                                    "La longitud de la contraseña es demasiado corta.",
                                    Toast.LENGTH_SHORT
                                              ).show()
                            } else {
                                if (!rbAceptarTerminos.isChecked) {
                                    edtContrasena.setBackgroundResource(R.drawable.redondear_edittext)
                                    edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext)
                                    Toast.makeText(
                                        this,
                                        "Debes aceptar los términos y condiciones",
                                        Toast.LENGTH_SHORT
                                                  ).show()
                                    txtTerminosCondiciones.setTextColor(Color.RED)
                                } else {
                                    val musicoNuevo = Musico(0, edtNombre.text.toString(), edtCorreo.text.toString(), edtContrasena.text.toString(), edtTelefono.text.toString(), 0.0, 0.0,
                                                        null.toString(), true, emptyList(), -1.0, edtApellido.text.toString(), edtApodo.text.toString(), calcularEdad(edtEdad.text.toString()), "Sin biografía", SpnGenero.text.toString(), emptyList(), emptyList(), null.toString(), 0)
                                    enviarMusico(musicoNuevo)

                                    // Llamamos API para recibir la ID del nuevo usuario
                                    RetrofitClient.instance.getMusicoByCorreo(musicoNuevo.correo).enqueue(object : Callback<Musico> {
                                        override fun onResponse(call: Call<Musico>, response: Response<Musico>) {

                                            Log.d("API", "Código de respuesta: ${response.code()}")

                                            if (response.isSuccessful) {
                                                val musicoAPI = response.body()
                                                Log.d("API", "Cuerpo de la respuesta: $musicoAPI")

                                                musicoAPI?.let {
                                                    Log.d("API", "Nombre: ${it.nombre}, Género: ${it.genero}")
                                                } ?: Log.e("API", "El cuerpo de la respuesta es null")
                                            } else {
                                                Log.e("API", "Error en la respuesta: ${response.code()} - ${response.errorBody()?.string()}")
                                            }

                                            if (response.isSuccessful) {
                                                val musicoAPI = response.body()
                                                musicoAPI?.let {
                                                    Log.d("API", "Nombre: ${it.nombre}, Genero: ${it.genero}")

                                                    val intent = Intent(
                                                        this@Registro, MenuActivityMusico::class.java
                                                                       )
                                                    intent.putExtra(
                                                        "usuarioId", musicoAPI.id
                                                                   )
                                                    startActivity(intent)
                                                    finish()
                                                    overridePendingTransition(
                                                        R.anim.slide_in_right, R.anim.slide_out_left
                                                                             )
                                                }
                                            } else {
                                                Log.e("API", "Error: ${response.code()}")
                                            }
                                        }

                                        override fun onFailure(call: Call<Musico>, t: Throwable) {
                                            Log.e("API", "Error en la conexión", t)
                                        }
                                    })
                                }
                            }
                        } else {
                            edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                            edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext_error)
                            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                } else { // Local
                    btnNombre.setOnClickListener {
                        btnNombre.setOnClickListener {
                            if (edtNombre.text.isEmpty()) {
                                Toast.makeText(this, "Inserta un nombre", Toast.LENGTH_SHORT).show()
                                edtNombre.setBackgroundResource(R.drawable.redondear_edittext_error)
                            } else {
                                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                                animateTransition(LLNombre, LLCorreo)
                            }
                        }
                        btnCorreo.setOnClickListener {
                            if (edtCorreo.text.isEmpty()) {
                                Toast.makeText(this, "Inserta un correo", Toast.LENGTH_SHORT).show()
                                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                            } else {
                                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext)
                            animateTransition(LLCorreo, LLTelefono)
                            }
                        }
                        btnTelefono.setOnClickListener {
                            animateTransition(
                                LLTelefono,
                                LLTipoLocal
                                             )
                        }
                        btnTipoLocal.setOnClickListener {
                            if (txtTipoLocal.text.toString() == "Selecciona tu tipo de local") {
                                Toast.makeText(this, "Inserta una opción", Toast.LENGTH_SHORT).show()
                                txtTipoLocal.setBackgroundResource(R.drawable.redondear_edittext_error)
                            } else {
                                txtTipoLocal.setBackgroundResource(R.drawable.redondear_edittext)
                                animateTransition(LLTipoLocal,LLContrasena)
                            }
                        }
                        btnContrasena.setOnClickListener {
                            if (edtContrasena.text.toString() == edtContrasenaConf.text.toString()) {
                                if (edtContrasena.text.length < 8) {
                                    edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                                    edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext_error)
                                    Toast.makeText(
                                        this,
                                        "La longitud de la contraseña es demasiado corta.",
                                        Toast.LENGTH_SHORT
                                                  ).show()
                                } else {
                                    if (!rbAceptarTerminos.isChecked) {
                                        edtContrasena.setBackgroundResource(R.drawable.redondear_edittext)
                                        edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext)
                                        Toast.makeText(
                                            this,
                                            "Debes aceptar los términos y condiciones",
                                            Toast.LENGTH_SHORT
                                                      ).show()
                                        txtTerminosCondiciones.setTextColor(Color.RED)
                                    } else {
                                        // Implementar registro LOCAL
                                    }
                                }
                            } else {
                                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                                edtContrasenaConf.setBackgroundResource(R.drawable.redondear_edittext_error)
                                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }

            // Configuración de los botones de volver
            setBackButtonListener(btnNombreVolver, LLNombre, LLType, edtNombre)
            setBackButtonListener(btnApodoVolver, LLApodo, LLNombre, edtApodo)
            setBackButtonListener(btnApellidoVolver, LLApellido, LLApodo, edtApellido)
            btnCorreoVolver.setOnClickListener {
                if (musico) {
                    animateTransition(LLCorreo, LLApellido)
                } else {
                    animateTransition(LLCorreo, LLNombre)
                }
                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext)
            }
            setBackButtonListener(btnEdadVolver, LLEdad, LLCorreo, edtEdad)
            setBackButtonListener(btnTelefonoVolver, LLTelefono, LLEdad, edtTelefono)
            setBackButtonListener(btnGeneroVolver, LLGenero, LLTelefono, null)
            btnContrasenaVolver.setOnClickListener {
                if (musico) {
                    animateTransition(LLContrasena, LLGenero)
                } else {
                    animateTransition(LLContrasena, LLTipoLocal)

                }
            }
            setBackButtonListener(btnTipoLocalVolver, LLTipoLocal, LLTelefono, null)

            // Configuración del botón volver a inicio de sesión
            btnVolverType.setOnClickListener {
                val intent = Intent(this, IniciarSesion::class.java)
                startActivity(intent)
                finish()
            }
        }
    }



    fun enviarMusico(musico: Musico) {
        val api = RetrofitClient.instance

        api.postMusico(musico).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", "Músico registrado correctamente: ${response.body()}")
                } else {
                    Log.e("API_ERROR", "Error al registrar músico: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("API_FAILURE", "Fallo en la conexión: ${t.message}")
            }
        })
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

    // Ejemplo de uso:
    fun main() {
        val edad = calcularEdad("04-04-2000")
        println("La edad es: $edad años")
    }


}
