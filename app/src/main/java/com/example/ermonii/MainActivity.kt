package com.example.ermonii

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    private var textViewAutomatico: TextView? = null
    private var text = ""
    private var index = 0
    private var seleccionado = false
    private lateinit var mediaPlayer: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewAutomatico = findViewById(R.id.TextViewAutomatico);
        if (!seleccionado) {
            text = random()
            playAudio()
            seleccionado = true
        }
        animateText();

        val btn_comenzar = findViewById<TextView>(R.id.Btn_comenzar)
        btn_comenzar.setOnClickListener {
            mediaPlayer.release()
            val intent = Intent(this, IniciarSesionActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    private fun animateText() {

        if (index < text.length) {
            textViewAutomatico?.append(text[index].toString())
            index++
            Handler().postDelayed(
                { this.animateText() },
                150
                                 ) // Cambia el tiempo para ajustar la velocidad
        } else {
            mediaPlayer.release()
        }
    }
    private fun random(): String {
        val welcomeMessages = listOf(
            "¡Bienvenidos! Aquí podréis encontrar a vuestro cantante o local favorito. Esta aplicación está diseñada para facilitar la conexión entre cantantes y locales.",
            "¡Hola! En esta app, los cantantes pueden buscar locales y los locales pueden encontrar a sus artistas favoritos. ¡Conectemos a través de la música!",
            "¡Hola! Descubre la mejor manera de encontrar tu cantante o local ideal. Únete a nuestra comunidad musical y disfruta de grandes momentos.",
            "¡Bienvenidos a la música! Aquí, los cantantes y los locales se unen para crear experiencias inolvidables. ¡Explora y conecta!"
                                    )

        val indexRandom = Random.nextInt(3)
        return welcomeMessages[indexRandom]
    }
    fun playAudio() {
        mediaPlayer = MediaPlayer.create(this, R.raw.teclear)
        mediaPlayer.start()
    }
}