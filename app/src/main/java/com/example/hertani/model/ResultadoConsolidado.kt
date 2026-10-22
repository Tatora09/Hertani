package com.example.hertani.model

data class ResultadoConsolidado(
    val candidataId: Long,
    val nombre: String,
    val area: String,
    val estado: EstadoCandidatura,
    val cantidadEvaluaciones: Int,
    val promedio: Double
)