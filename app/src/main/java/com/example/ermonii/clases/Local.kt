package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName
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
    @SerializedName("tipo_local") val tipoLocal: String,
    val direccion: String,
    val descripcion: String,
    val image: Int?,
    var idUsuario: Int
           ) : Usuario(id, nombre, correo, contrasenya, telefono, latitud, longitud, fechaRegistro, estado, chat, valoracion, "Local"), Serializable
