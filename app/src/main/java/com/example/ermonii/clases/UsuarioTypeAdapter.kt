package com.example.ermonii.clases

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.*

class UsuarioTypeAdapter : TypeAdapter<Usuario>() {

    override fun write(out: JsonWriter?, value: Usuario?) {
        // Implementación de serialización si es necesario
    }

    override fun read(`in`: JsonReader?): Usuario {
        val jsonObject = JsonParser.parseReader(`in`!!).asJsonObject
        val tipo = jsonObject.get("tipo").asString

        return when (tipo) {
            "Musico" -> Gson().fromJson(jsonObject, Musico::class.java)
            "Local" -> Gson().fromJson(jsonObject, Local::class.java)
            else -> throw JsonParseException("Tipo de usuario desconocido")
        }
    }
}

