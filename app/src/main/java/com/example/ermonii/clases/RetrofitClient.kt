package com.example.ermonii.clases

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

object RetrofitClient {
    private const val BASE_URL = "http://10.0.0.99/dam02/"

    val instance: ApiService by lazy {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Usuario::class.java, UsuarioTypeAdapter())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS")
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(ApiService::class.java)
    }
}
