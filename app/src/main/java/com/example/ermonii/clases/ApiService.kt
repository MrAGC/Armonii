package com.example.ermonii.clases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/Usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @GET("api/Local")
    suspend fun getLocales(): List<Local>

    @GET("api/Evento")
    suspend fun getEventos(): List<Evento>

    @GET("api/Musico")
    fun getMusicos(): Call<List<Musico>>

    @GET("/api/Musico/{id}")
    fun getMusicoById(@Path("id") id: Int): Call<Musico>

    @GET("/api/Local/{id}")
    fun getLocalById(@Path("id") id: Int): Call<Local>

    @GET("Usuario/{id}")
    fun getUsuarioById(@Path("id") id: Int): Call<Usuario>
}