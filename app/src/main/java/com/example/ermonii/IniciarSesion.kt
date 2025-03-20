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

class IniciarSesion: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)


        //_________________________
        val call = RetrofitClient.instance.getUsuarios()
        call.enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach { usuario ->
                        try {
                            val tipoLog = when (usuario.tipo) {
                                "Musico" -> "Músico: ${usuario.nombre} ID: ${usuario.id} Valoración: ${usuario.valoracion}"
                                "Local" -> "Local: ${usuario.nombre} ID: ${usuario.id} Teléfono: ${usuario.telefono}"
                                else -> "Tipo desconocido: ${usuario.tipo}"
                            }
                            Log.d("MainActivity", tipoLog)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error procesando usuario: ${e.message}")
                        }
                    } ?: Log.e("MainActivity", "Lista de usuarios vacía")
                } else {
                    Log.e("MainActivity", "Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Log.e("MainActivity", "Error de red: ${t.message}")
            }
        })

        //________________________


        // Inicia la actividad de inicio de sesion (FALTA COMPROBACION DE USUARIO Y CONTRASEÑA)
        val btn_iniciar_sesion = findViewById<Button>(R.id.Btn_iniciar_sesion)
        val edtCorreo = findViewById<EditText>(R.id.edtCorreo)
        val edtContrasena = findViewById<EditText>(R.id.edtContrasena)

        btn_iniciar_sesion.setOnClickListener {
            val correo = edtCorreo.text.toString().trim()
            val contrasena = edtContrasena.text.toString().trim()

            if (correo == "musico") {
                val intent = Intent(this, MenuActivityMusico::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(correo == "local") {
                val intent = Intent(this, MenuActivityLocal::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
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