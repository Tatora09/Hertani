package com.example.hertani.model

enum class Rol(val etiqueta: String) {
    EVALUADORA("Evaluadora"),
    DIRECCION("Dirección")
}

data class Sesion(val nombre: String, val rol: Rol)