package com.example.ermonii.clases

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/Usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @GET("api/Local")
    suspend fun getLocales(): List<Local>

    @GET("api/Evento")
    suspend fun getEventos(): List<Evento>

    @GET("api/Musico")
    fun getMusicos(): Call<List<Musico>>

    @GET("api/Musico")
    fun getMusicoByCorreo(@Query("correo") correo: String): Call<Musico>

    @GET("/api/Local/{id}")
    fun getLocalById(@Path("id") id: Int): Call<Local>

    @GET("Usuario/{id}")
    fun getUsuarioById(@Path("id") id: Int): Call<Usuario>

    @POST("api/Musico") // Ruta de tu API
    fun postMusico(@Body musico: Musico): Call<Boolean>
}