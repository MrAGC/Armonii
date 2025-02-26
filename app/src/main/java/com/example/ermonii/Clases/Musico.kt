package com.example.ermonii.Clases

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
    val edad: Int,
    val biografia: String,
    val genero: String,
    val generoMusical: List<String>,
    val historialEventos: List<Evento>,
    val nombreArtistico: String
            ) : Usuario(id, name, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion)
