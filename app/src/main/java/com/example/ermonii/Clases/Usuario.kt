package com.example.ermonii.Clases

import java.io.Serializable
import java.util.Date

abstract class Usuario(
    val id: Int,
    val name: String,
    val correo: String,
    val contrasenya: String,
    val telefono: Long,
    val latitud: Float,
    val longitud: Float,
    val fechaRegistro: Date,
    val estado: Boolean,
    val chat: List<Chat>,
    val valoracion: Float
                      ) : Serializable
