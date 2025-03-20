package com.example.ermonii.clases

import com.google.type.DateTime
import java.io.Serializable
import java.util.Date

class Local(
    id: Int,
    name: String,
    correo: String,
    contrasenya: String,
    telefono: String,
    latitud: Double,
    longitud: Double,
    fechaRegistro: Date,
    estado: Boolean,
    chat: List<Chat>,
    valoracion: Double,
    val description: String,
    val image: Int,
    val PZCAT_LATITUDE: Double,
    val PZCAT_LONGITUDE: Double,
    val rating: Int
           ) : Usuario(id, name, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Local"), Serializable
