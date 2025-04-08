package com.example.ermonii.clases

import java.util.*

data class DataTransferObjectUsuario(
    // Atributos de Usuario
    val id: Int,
    val nombre: String,
    val correo: String,
    val contrasenya: String,
    val telefono: String,
    val latitud: Double?,
    val longitud: Double?,
    val fechaRegistro: Date?,
    val estado: Boolean?,
    val valoracion: Double?,
    val tipo: String,

    // Atributos de Musico
    val apodo: String,
    val apellido: String,
    val genero: String,
    val edad: Int?,
    val biografia: String,
    val imagen: String,
    val idUsuario: Int,
    val generosMusicales: List<String>,

    // Atributos de Local
    val direccion: String,
    val tipo_local: String,
    val HorarioApertura: String?, // Tiempo como String en formato "HH:mm"
    val HorarioCierre: String?,   // Tiempo como String en formato "HH:mm"
    val descripcion: String
                                   )
