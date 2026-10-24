package com.example.hertani.model

enum class Rol(val etiqueta: String, val descripcion: String) {
    EVALUADORA("Evaluadora", "Revisa antecedentes y aplica la matriz de criterios."),
    DIRECCION("Dirección", "Consulta los resultados y cierra las candidaturas.")
}
data class Sesion(val nombre: String, val rol: Rol)