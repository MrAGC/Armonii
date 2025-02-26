package com.example.ermonii.Clases

import java.io.Serializable
import java.util.Date

class Local(
    id: Int,
    name: String,
    correo: String,
    contrasenya: String,
    telefono: Long,
    latitud: Float,
    longitud: Float,
    fechaRegistro: Date,
    estado: Boolean,
    chat: List<Chat>,
    valoracion: Float,
    val description: String,
    val image: Int,
    val PZCAT_LATITUDE: Double,
    val PZCAT_LONGITUDE: Double,
    val rating: Int
           ) : Usuario(id, name, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion), Serializable
