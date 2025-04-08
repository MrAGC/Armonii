package com.example.ermonii

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.Musico
import com.example.ermonii.clases.RetrofitClient
import com.example.ermonii.clases.Usuario
import com.example.ermonii.fragmentLocal.MenuActivityLocal
import com.example.ermonii.fragmentMusico.MenuActivityMusico
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class IniciarSesion : AppCompatActivity() {

    companion object {
        private const val AES = "AES"
        private const val SECRET_KEY = "1234567890123456"
    }

    private lateinit var musico: Musico
    private lateinit var local: Local

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
                    override fun onResponse( call: Call<List<Usuario>>, response: Response<List<Usuario>>) {

                        Log.e("IniciarSesion", "Código de respuesta: ${response.body()}")


                        if (response.isSuccessful) {
                            val gson = Gson()
                            // Si la respuesta es exitosa, mostramos el cuerpo del JSON
                            val jsonResponse = response.body()?.let { gson.toJson(it) } // Convierte la respuesta a un JSON String
                            Log.d("IniciarSesion", "Cuerpo de respuesta: $jsonResponse")
                        } else {
                            Log.e("IniciarSesion", "Error en la respuesta: ${response.errorBody()?.string()}")
                        }

                        if (response.isSuccessful) {
                            val usuarios = response.body()
                            val usuarioValido =
                                usuarios?.find { it.correo == correo && decryptAES(it.contrasenya) == contrasena }

                            if (usuarioValido?.tipo != null) {
                                when (usuarioValido.tipo) {
                                    "Musico" -> {
                                        val intent = Intent(
                                            this@IniciarSesion, MenuActivityMusico::class.java
                                                           )
                                        intent.putExtra(
                                            "usuarioId", usuarioValido.id
                                                       )
                                        startActivity(intent)
                                        finish()
                                        overridePendingTransition(
                                            R.anim.slide_in_right, R.anim.slide_out_left
                                                                 )
                                    }

                                    "Local" -> {
                                        val intent = Intent(
                                            this@IniciarSesion, MenuActivityLocal::class.java
                                                           )
                                        intent.putExtra(
                                            "usuarioId", usuarioValido.id
                                                       )
                                        startActivity(intent)
                                        finish()
                                        overridePendingTransition(
                                            R.anim.slide_in_right, R.anim.slide_out_left
                                                                 )
                                    }
                                }
                            } else {
                                // Si no se encuentra un usuario válido
                                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                                Toast.makeText(
                                    this@IniciarSesion,
                                    "Correo o contraseña incorrectos",
                                    Toast.LENGTH_SHORT
                                              ).show()
                            }
                        } else {
                            Log.e("IniciarSesion", "Error al obtener usuarios: ${response.code()}")
                            Toast.makeText(
                                this@IniciarSesion,
                                "Error al conectar con el servidor",
                                Toast.LENGTH_SHORT
                                          ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                        Log.e("IniciarSesion", "Error de red: ${t.message}")
                        Toast.makeText(this@IniciarSesion, "Error de conexión", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else {
                // Si el correo o la contraseña están vacíos
                edtCorreo.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtContrasena.setBackgroundResource(R.drawable.redondear_edittext_error)
                Toast.makeText(
                    this@IniciarSesion,
                    "Por favor, ingrese correo y contraseña",
                    Toast.LENGTH_SHORT
                              ).show()
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

    fun decryptAES(encryptedData: String): String {
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), AES)
        val cipher = Cipher.getInstance(AES)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted)
    }
}
