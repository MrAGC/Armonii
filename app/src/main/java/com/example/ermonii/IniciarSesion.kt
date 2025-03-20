package com.example.ermonii

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ermonii.clases.RetrofitClient
import com.example.ermonii.clases.Usuario
import com.example.ermonii.fragmentLocal.MenuActivityLocal
import com.example.ermonii.fragmentMusico.MenuActivityMusico
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IniciarSesion : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        // Referencias de las vistas
        val btn_iniciar_sesion = findViewById<Button>(R.id.Btn_iniciar_sesion)
        val edtCorreo = findViewById<EditText>(R.id.edtCorreo)
        val edtContrasena = findViewById<EditText>(R.id.edtContrasena)

        // Evento de inicio de sesión
        btn_iniciar_sesion.setOnClickListener {
            val correo = edtCorreo.text.toString().trim()
            val contrasena = edtContrasena.text.toString().trim()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                // Realizar la consulta a la base de datos para comprobar el correo y la contraseña
                val call = RetrofitClient.instance.getUsuarios()
                call.enqueue(object : Callback<List<Usuario>> {
                    override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                        if (response.isSuccessful) {
                            val usuarios = response.body()
                            val usuarioValido = usuarios?.find { it.correo == correo && it.contrasenya == contrasena }

                            if (usuarioValido != null) {
                                // Si el usuario es válido, verifica su tipo y redirige a la actividad correspondiente
                                when (usuarioValido.tipo) {
                                    "Musico" -> {
                                        val intent = Intent(this@IniciarSesion, MenuActivityMusico::class.java)
                                        startActivity(intent)
                                        finish()
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    }
                                    "Local" -> {
                                        val intent = Intent(this@IniciarSesion, MenuActivityLocal::class.java)
                                        startActivity(intent)
                                        finish()
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    }
                                    else -> {
                                        Toast.makeText(this@IniciarSesion, "Tipo de usuario desconocido", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                // Si no se encuentra un usuario válido
                                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                                Toast.makeText(this@IniciarSesion, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("IniciarSesion", "Error al obtener usuarios: ${response.code()}")
                            Toast.makeText(this@IniciarSesion, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                        Log.e("IniciarSesion", "Error de red: ${t.message}")
                        Toast.makeText(this@IniciarSesion, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Si el correo o la contraseña están vacíos
                Toast.makeText(this@IniciarSesion, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Inicia la actividad de registro de nuevo usuario
        val txtRegistro = findViewById<TextView>(R.id.txtRegistro)
        txtRegistro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
            finish()
        }
    }
}
