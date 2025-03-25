package com.example.ermonii.clases

import java.io.Serializable
import java.util.Date

class Local(
    id: Int,
    nombre: String,
    correo: String,
    contrasenya: String,
    telefono: String?,
    latitud: Double,
    longitud: Double,
    fechaRegistro: String,
    estado: Boolean,
    chat: List<Int>?,
    valoracion: Double?,
    val image: Int?,
    val rating: Int?
           ) : Usuario(id, nombre, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Local"), Serializable
