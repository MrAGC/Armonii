package com.example.ermonii.clases

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

abstract class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("correo") var correo: String,
    @SerializedName("contrasenya") var contrasenya: String,
    @SerializedName("telefono")var telefono: String? = null,
    @SerializedName("latitud") var latitud: Double,
    @SerializedName("longitud")var longitud: Double,
    @SerializedName("fechaRegistro") var fechaRegistro: String?,
    @SerializedName("estado") var estado: Boolean,
    @SerializedName("chat") var chat: List<Int>? = null,
    @SerializedName("valoracion")var valoracion: Double? = null,
    @SerializedName("tipo")var tipo: String?
                      ) : Serializable