package com.example.ermonii.clases

import java.time.LocalDateTime

data class Mensaje(
    val mensaje: String,
    val fechaEnvio: LocalDateTime,
    val nombreUsuario: String
                  )
