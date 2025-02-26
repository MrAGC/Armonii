package com.example.ermonii

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
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

class Registro : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Declaraciones
        var musico = false
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
        val edtEdad = findViewById<EditText>(R.id.edtEdad)

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

        val btnNombreVolver = findViewById<Button>(R.id.btnVolverNombre)
        val btnApodoVolver = findViewById<Button>(R.id.btnVolverApodo)
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
        fun setBackButtonListener(button: Button, fromView: View, toView: View) {
            button.setOnClickListener { animateTransition(fromView, toView) }
        }

        // Manejo del clic en los términos y condiciones
        txtTerminos.setOnClickListener {
            rbAceptarTerminos.isChecked = !rbAceptarTerminos.isChecked
        }

        // Mostrar el DatePickerDialog al hacer clic en el campo de edad
        edtEdad.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                edtEdad.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        // Manejo de la selección de tipo de usuario y navegación entre vistas
        btnType.setOnClickListener {
            animateTransition(LLType, LLNombre)
            if (musico) {
                btnNombre.setOnClickListener { animateTransition(LLNombre, LLApodo) }
                btnApodo.setOnClickListener { animateTransition(LLApodo, LLApellido) }
                btnApellido.setOnClickListener { animateTransition(LLApellido, LLCorreo) }
                btnCorreo.setOnClickListener { animateTransition(LLCorreo, LLEdad) }
                btnEdad.setOnClickListener { animateTransition(LLEdad, LLTelefono) }
                btnTelefono.setOnClickListener { animateTransition(LLTelefono, LLGenero) }
                btnGenero.setOnClickListener { animateTransition(LLGenero, LLContrasena) }
                btnContrasena.setOnClickListener {  } // Implementar Home Fragment
            } else {
                btnNombre.setOnClickListener { animateTransition(LLNombre, LLCorreo) }
                btnCorreo.setOnClickListener { animateTransition(LLCorreo, LLTelefono) }
                btnTelefono.setOnClickListener { animateTransition(LLTelefono, LLTipoLocal) }
                btnTipoLocal.setOnClickListener { animateTransition(LLTipoLocal, LLContrasena) }
                btnContrasena.setOnClickListener {  } // Implementar Home Fragment
            }
        }

        // Configuración de los botones de volver
        setBackButtonListener(btnNombreVolver, LLNombre, LLType)
        setBackButtonListener(btnApodoVolver, LLApodo, LLNombre)
        setBackButtonListener(btnApellidoVolver, LLApellido, LLApodo)
        setBackButtonListener(btnCorreoVolver, LLCorreo, LLApellido)
        setBackButtonListener(btnEdadVolver, LLEdad, LLCorreo)
        setBackButtonListener(btnTelefonoVolver, LLTelefono, LLEdad)
        setBackButtonListener(btnGeneroVolver, LLGenero, LLTelefono)
        setBackButtonListener(btnContrasenaVolver, LLContrasena, LLGenero)
        setBackButtonListener(btnTipoLocalVolver, LLTipoLocal, LLTelefono)
    }
}
