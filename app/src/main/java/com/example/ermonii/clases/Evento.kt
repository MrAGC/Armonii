package com.example.ermonii.clases

import java.io.Serializable

class Evento (
    val id: Int,
    val name: String,
    val date: String,
    val description: String,
    val local: Local,
    val music: Musico?,
    val estado: Boolean,
    val duration: Int
             ) : Serializable