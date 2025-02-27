package com.example.ermonii.clases

import java.util.Date

class Musico(
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
    val apellidos: String,
    val apodo: String,
    val edad: Int,
    val biografia: String,
    val genero: String,
    val generoMusical: List<String>,
    val historialEventos: List<Evento>,
    val image: Int
    ) : Usuario(id, name, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion)
