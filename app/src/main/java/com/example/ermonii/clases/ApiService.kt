package com.example.ermonii.clases

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/Local")
    fun getLocalByCorreo(@Query("correo") correo: String): Call<Local>

    @POST("api/Musico")
    fun postMusico(@Body musico: Musico): Call<Boolean>

    @POST("api/Local")
    fun postLocal(@Body local: Local): Call<Boolean>

    @POST("api/Evento")
    fun postEvento(@Body evento: Evento): Call<Boolean>

    @PUT("api/Musico/{id}")
    suspend fun actualizarMusico(
        @Path("id") id: Int,
        @Body musico: DataTransferObjectUsuario): Response<Unit>

    @DELETE("api/Evento/{id}")
    fun deleteEvento(@Path("id") id: Int): Call<Evento>
}


/*
    private fun deleteEvento(eventoId: Int) {
        val call = RetrofitInstance.apiService.deleteEvento(eventoId)

        call.enqueue(object : Callback<Evento> {
            override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
                if (response.isSuccessful) {
                    val evento = response.body()
                    evento?.let {
                        Log.d("MainActivity", "Evento eliminado: ${it.nombre}")
                    }
                } else {
                    Log.e("MainActivity", "Error al eliminar el evento: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventoResponse>, t: Throwable) {
                Log.e("MainActivity", "Fallo la solicitud: ${t.message}")
            }
        })
    }
 */