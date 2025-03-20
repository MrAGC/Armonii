package com.example.ermonii.clases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/Usuario")
    fun getUsuarios(): Call<List<Usuario>>
}