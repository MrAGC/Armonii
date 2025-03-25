package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

abstract class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo")val correo: String,
    @SerializedName("contrasenya") val contrasenya: String,
    @SerializedName("telefono")val telefono: String? = null,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud")val longitud: Double,
    @SerializedName("fechaRegistro") @Transient val fechaRegistro: Date?,
    @SerializedName("estado") val estado: Boolean,
    @SerializedName("chat") val chat: List<Int>? = null,
    @SerializedName("valoracion")val valoracion: Double? = null,
    @SerializedName("tipo")val tipo: String
                      ) : Serializable