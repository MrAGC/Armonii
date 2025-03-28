package com.example.ermonii.clases

data class Mensaje(
    val id: Int,
    val idUsuarioLocal: String,
    val idUsuarioMusico: String,
    val fechaEnvio: String,
    val mensaje: String,
    val emisor: String
                  )