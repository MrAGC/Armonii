package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Evento (
    val id: Int,
    val nombre: String,
    val fecha: String,
    val descripcion: String,
    @SerializedName("idLocal") val local: Int,
    val music: Int?,
    val estado: Boolean,
    val duracion: Int
             ) : Serializable