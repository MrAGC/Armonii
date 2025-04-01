package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName

class Musico(
    id: Int,
    nombre: String,
    correo: String,
    contrasenya: String,
    telefono: String,
    latitud: Double,
    longitud: Double,
    fechaRegistro: String,
    estado: Boolean,
    chat: List<Int>,
    valoracion: Double,
    var apellido: String,
    var apodo: String,
    var edad: Int,
    var biografia: String,
    var genero: String,
    @SerializedName("generosMusicales") var generoMusical: List<String>,
    var historialEventos: List<Int>,
    var image: Int,
    var idUsuario: Int
    ) : Usuario(id, nombre, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Musico")
