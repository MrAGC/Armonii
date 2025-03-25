package com.example.ermonii.clases

import java.util.Date

class Musico(
    id: Int,
    nombre: String,
    correo: String,
    contrasenya: String,
    telefono: String,
    latitud: Double,
    longitud: Double,
    fechaRegistro: Date,
    estado: Boolean,
    chat: List<Int>,
    valoracion: Double,
    val apellidos: String,
    val apodo: String,
    val edad: Int,
    val biografia: String,
    val genero: String,
    val generoMusical: List<String>,
    val historialEventos: List<Int>,
    val image: Int
    ) : Usuario(id, nombre, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Musico")
