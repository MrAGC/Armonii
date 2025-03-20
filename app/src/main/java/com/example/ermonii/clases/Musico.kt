package com.example.ermonii.clases

import com.google.type.DateTime
import java.util.Date

class Musico(
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
    val apellidos: String,
    val apodo: String,
    val edad: Int,
    val biografia: String,
    val genero: String,
    val generoMusical: List<String>,
    val historialEventos: List<Evento>,
    val image: Int
    ) : Usuario(id, name, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Musico")
