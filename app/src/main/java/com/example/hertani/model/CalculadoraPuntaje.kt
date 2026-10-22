package com.example.hertani.model

object CalculadoraPuntaje {
    const val PUNTAJE_MIN = 1
    const val PUNTAJE_MAX = 7

    fun calcular(puntajes: Map<Long, Int>, criterios: List<Criterio>): Double {
        val pesoTotal = criterios.sumOf { it.peso }
        if (pesoTotal == 0) return 0.0
        val suma = criterios.sumOf { (puntajes[it.id] ?: 0) * it.peso }
        return suma * 100.0 / (pesoTotal * PUNTAJE_MAX)
    }

    fun categoria(total: Double): String = when {
        total >= 80 -> "Destacada"
        total >= 60 -> "Competente"
        total >= 40 -> "En desarrollo"
        else -> "Insuficiente"
    }
}