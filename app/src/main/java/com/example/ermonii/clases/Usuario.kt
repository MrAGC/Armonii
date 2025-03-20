package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName
import com.google.type.DateTime
import java.io.Serializable
import java.util.Date

abstract class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo")val correo: String,
    val contrasenya: String,
    @SerializedName("telefono")val telefono: String? = null,
    val latitud: Double,
    val longitud: Double,
    @SerializedName("fechaRegistro") @Transient val fechaRegistro: Date,
    val estado: Boolean,
    val chat: List<Chat>? = null,
    @SerializedName("valoracion")val valoracion: Double? = null,
    @SerializedName("tipo")val tipo: String
                      ) : Serializable