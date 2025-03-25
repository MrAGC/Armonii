package com.example.ermonii.clases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/Usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @GET("api/Local")
    fun getLocales(): Call<List<Local>>

    @GET("api/Evento")
    fun getEventos(): Call<List<Evento>>

    @GET("api/Musico")
    fun getMusicos(): Call<List<Musico>>

    @GET("/api/Musico/{id}")
    fun getMusicoById(@Path("id") id: Int): Call<Musico>

    @GET("Usuario/{id}")
    fun getUsuarioById(@Path("id") id: Int): Call<Usuario>
}