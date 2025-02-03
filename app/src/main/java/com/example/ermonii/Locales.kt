package com.example.ermonii

import java.io.Serializable

class Locales (
    val id: Int,
    val name: String,
    val description: String,
    val image: Int,
    val PZCAT_LATITUDE : Double,
    val PZCAT_LONGITUDE : Double,
    val rating: Int // Nueva propiedad para la puntuación
) : Serializable

