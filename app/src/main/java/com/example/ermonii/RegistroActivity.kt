package com.example.ermonii

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Declaraciones
        var musico = false
        var LLType = findViewById<LinearLayout>(R.id.LLType)
        var imgMusico = findViewById<ImageView>(R.id.imgMusico)
        var imgLocal = findViewById<ImageView>(R.id.imgLocal)
        var btnType = findViewById<Button>(R.id.btnContinuarType)
        var LLNombre = findViewById<LinearLayout>(R.id.LLNombre)
        var btnNombre = findViewById<Button>(R.id.btnContinuarNombre)
        var LLApellido = findViewById<LinearLayout>(R.id.LLApellido)
        var btnApellido = findViewById<Button>(R.id.btnContinuarApellido)
        var LLCorreo = findViewById<LinearLayout>(R.id.LLCorreo)
        var btnCorreo = findViewById<Button>(R.id.btnContinuarCorreo)
        var LLEdad = findViewById<LinearLayout>(R.id.LLEdad)
        var btnEdad = findViewById<Button>(R.id.btnContinuarEdad)
        val edtEdad = findViewById<EditText>(R.id.edtEdad)

        // Obtén la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var LLTelefono = findViewById<LinearLayout>(R.id.LLTelefono)
        var btnTelefono = findViewById<Button>(R.id.btnContinuarTelefono)
        var LLGenero = findViewById<LinearLayout>(R.id.LLGenero)
        var btnGenero = findViewById<Button>(R.id.btnContinuarGenero)
        var LLContrasena = findViewById<LinearLayout>(R.id.LLContrasena)
        var btnContrasena = findViewById<Button>(R.id.btnContinuarContrasena)
        val rbAceptarTerminos = findViewById<RadioButton>(R.id.radioButtonAceptarTerminos)
        val txtTerminos = findViewById<TextView>(R.id.txtTerminosCondiciones)
        val LLTipoLocal = findViewById<LinearLayout>(R.id.LLTipoLocal)
        val btnTipoLocal = findViewById<Button>(R.id.btnContinuarTipoLocal)

        val btnNombreVolver = findViewById<Button>(R.id.btnVolverNombre)
        val btnApellidoVolver = findViewById<Button>(R.id.btnVolverApellido)
        val btnCorreoVolver = findViewById<Button>(R.id.btnVolverCorreo)
        val btnEdadVolver = findViewById<Button>(R.id.btnVolverEdad)
        val btnTelefonoVolver = findViewById<Button>(R.id.btnVolverTelefono)
        val btnGeneroVolver = findViewById<Button>(R.id.btnVolverGenero)
        val btnContrasenaVolver = findViewById<Button>(R.id.btnVolverContrasena)
        val btnTipoLocalVolver = findViewById<Button>(R.id.btnVolverTipoLocal)

        // Configuramos el spinner del genero
        val SpnGenero = findViewById<Spinner>(R.id.SpnGenero)
        val opcionesGenero = arrayOf("Seleccionar tu genero", "Masculino", "Femenino", "Otro")
        val adapterGenero = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesGenero)
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_item)
        SpnGenero.adapter = adapterGenero

        // Configuramos el spinner del tipo de local
        val SpnTipoLocal = findViewById<Spinner>(R.id.SpnTipoLocal)
        val opcionesTipoLocal = arrayOf(
            "Seleccionar tu tipo de local",
            "Restaurante",
            "Bar",
            "Cafetería",
            "Discoteca",
            "Librería",
            "Tienda de ropa",
            "Supermercado",
            "Tienda de electrónica",
            "Floristería",
            "Salón de belleza",
            "Gimnasio",
            "Hotel",
            "Centro de eventos",
            "Otro"
                                       )
        val adapterTipoLocal =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipoLocal)
        adapterTipoLocal.setDropDownViewResource(android.R.layout.simple_spinner_item)
        SpnTipoLocal.adapter = adapterTipoLocal


        // Escogemos músico
        imgMusico.setOnClickListener {
            imgMusico.setBackgroundResource(R.drawable.redondear_registro_icons_presionado)
            imgLocal.setBackgroundResource(R.drawable.redondear_registro_icons)
            musico = true
        }

        // Escogemos local
        imgLocal.setOnClickListener {
            imgMusico.setBackgroundResource(R.drawable.redondear_registro_icons)
            imgLocal.setBackgroundResource(R.drawable.redondear_registro_icons_presionado)
            musico = false
        }

        LLType.visibility = View.VISIBLE

        // Manejar el clic del botón btnContinuarType
        btnType.setOnClickListener {
            LLType.visibility = View.GONE

            // Comprobar el tipo de usuario y mostrar el layout correspondiente
            if (musico) {
                // Camino para el músico
                LLNombre.visibility = View.VISIBLE

                btnNombre.setOnClickListener {
                    LLNombre.visibility = View.GONE

                    LLApellido.visibility = View.VISIBLE
                }
                btnNombreVolver.setOnClickListener {
                    LLNombre.visibility = View.GONE

                    LLType.visibility = View.VISIBLE
                }

                btnApellido.setOnClickListener {
                    LLApellido.visibility = View.GONE

                    LLCorreo.visibility = View.VISIBLE
                }
                btnApellidoVolver.setOnClickListener {
                    LLApellido.visibility = View.GONE

                    LLNombre.visibility = View.VISIBLE
                }

                btnCorreo.setOnClickListener {
                    LLCorreo.visibility = View.GONE

                    LLEdad.visibility = View.VISIBLE
                }
                btnCorreoVolver.setOnClickListener {
                    LLCorreo.visibility = View.GONE

                    LLApellido.visibility = View.VISIBLE
                }

                btnEdad.setOnClickListener {
                    LLEdad.visibility = View.GONE

                    LLTelefono.visibility = View.VISIBLE
                }
                btnEdadVolver.setOnClickListener {
                    LLEdad.visibility = View.GONE

                    LLCorreo.visibility = View.VISIBLE
                }

                // Mostrar calendario para seleccionar fecha de nacimiento
                edtEdad.setOnClickListener {
                    // Crea un DatePickerDialog
                    val datePickerDialog =
                        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                            // Formatea la fecha seleccionada y la establece en el EditText
                            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                            edtEdad.setText(selectedDate)
                        }, year, month, day)

                    // Muestra el DatePickerDialog
                    datePickerDialog.show()
                }

                btnTelefono.setOnClickListener {
                    LLTelefono.visibility = View.GONE

                    LLGenero.visibility = View.VISIBLE
                }
                btnTelefonoVolver.setOnClickListener {
                    LLTelefono.visibility = View.GONE

                    LLEdad.visibility = View.VISIBLE
                }

                btnGenero.setOnClickListener {
                    // FALTA COMPROBACIÓN DE GENERO SELECCIONADO
                    LLGenero.visibility = View.GONE

                    LLContrasena.visibility = View.VISIBLE
                }
                btnGeneroVolver.setOnClickListener {
                    LLGenero.visibility = View.GONE

                    LLTelefono.visibility = View.VISIBLE
                }

                txtTerminos.setOnClickListener {
                    // Cambiar el estado del RadioButton
                    rbAceptarTerminos.isChecked = !rbAceptarTerminos.isChecked
                }

                btnContrasena.setOnClickListener {
                    LLContrasena.visibility = View.GONE

                    // FALTA COMPROBACIÓN DE CONTRASEÑAS

                    // Saltamos al home
                }
                btnContrasenaVolver.setOnClickListener {
                    LLContrasena.visibility = View.GONE

                    LLGenero.visibility = View.VISIBLE
                }


            } else {
                // Camino para el local
                LLNombre.visibility = View.VISIBLE

                btnNombre.setOnClickListener {
                    LLNombre.visibility = View.GONE

                    LLCorreo.visibility = View.VISIBLE
                }
                btnNombreVolver.setOnClickListener {
                    LLNombre.visibility = View.GONE

                    LLType.visibility = View.VISIBLE
                }

                btnCorreo.setOnClickListener {
                    LLCorreo.visibility = View.GONE

                    LLTelefono.visibility = View.VISIBLE
                }
                btnCorreoVolver.setOnClickListener {
                    LLCorreo.visibility = View.GONE

                    LLNombre.visibility = View.VISIBLE
                }

                btnTelefono.setOnClickListener {
                    LLTelefono.visibility = View.GONE

                    LLTipoLocal.visibility = View.VISIBLE
                }
                btnTelefonoVolver.setOnClickListener {
                    LLTelefono.visibility = View.GONE

                    LLCorreo.visibility = View.VISIBLE
                }

                btnTipoLocal.setOnClickListener {
                    LLTipoLocal.visibility = View.GONE

                    LLContrasena.visibility = View.VISIBLE
                }
                btnTipoLocalVolver.setOnClickListener {
                    LLTipoLocal.visibility = View.GONE

                    LLTelefono.visibility = View.VISIBLE
                }

                txtTerminos.setOnClickListener {
                    // Cambiar el estado del RadioButton
                    rbAceptarTerminos.isChecked = !rbAceptarTerminos.isChecked
                }

                btnContrasena.setOnClickListener {
                    LLContrasena.visibility = View.GONE

                    // FALTA COMPROBACIÓN DE CONTRASEÑAS

                    // Saltamos al home
                }
                btnContrasenaVolver.setOnClickListener {
                    LLContrasena.visibility = View.GONE

                    LLTipoLocal.visibility = View.VISIBLE
                }
            }
        }
    }
}
