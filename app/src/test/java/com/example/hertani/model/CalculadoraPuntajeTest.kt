package com.example.hertani.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraPuntajeTest {

    private val criterios = listOf(
        Criterio(id = 1, nombre = "A", descripcion = "", peso = 30, orden = 1),
        Criterio(id = 2, nombre = "B", descripcion = "", peso = 25, orden = 2),
        Criterio(id = 3, nombre = "C", descripcion = "", peso = 25, orden = 3),
        Criterio(id = 4, nombre = "D", descripcion = "", peso = 20, orden = 4)
    )

    private fun todos(valor: Int) = criterios.associate { it.id to valor }

    @Test fun puntaje_maximo_en_todo_da_100() =
        assertEquals(100.0, CalculadoraPuntaje.calcular(todos(CalculadoraPuntaje.PUNTAJE_MAX), criterios), 0.001)

    @Test fun puntaje_minimo_en_todo_no_da_cero() =
        // 1 de 4 en todo = 25%: el mínimo posible no es 0
        assertEquals(25.0, CalculadoraPuntaje.calcular(todos(CalculadoraPuntaje.PUNTAJE_MIN), criterios), 0.001)

    @Test fun promedio_ponderado_mixto() {
        // 4*30 + 3*25 + 3*25 + 4*20 = 350  →  350 * 100 / (100 * 4) = 87.5
        val puntajes = mapOf(1L to 4, 2L to 3, 3L to 3, 4L to 4)
        assertEquals(87.5, CalculadoraPuntaje.calcular(puntajes, criterios), 0.001)
    }

    @Test fun sin_puntajes_da_cero() =
        assertEquals(0.0, CalculadoraPuntaje.calcular(emptyMap(), criterios), 0.001)

    @Test fun sin_criterios_da_cero() =
        assertEquals(0.0, CalculadoraPuntaje.calcular(todos(4), emptyList()), 0.001)

    @Test fun categorias_en_los_cortes() {
        assertEquals("Destacada", CalculadoraPuntaje.categoria(80.0))
        assertEquals("Competente", CalculadoraPuntaje.categoria(79.9))
        assertEquals("Competente", CalculadoraPuntaje.categoria(60.0))
        assertEquals("En desarrollo", CalculadoraPuntaje.categoria(40.0))
        assertEquals("Insuficiente", CalculadoraPuntaje.categoria(39.9))
    }

    @Test fun niveles_de_la_escala() {
        assertEquals("Insuficiente", CalculadoraPuntaje.nivel(1))
        assertEquals("Destacado", CalculadoraPuntaje.nivel(4))
        assertEquals("Sin asignar", CalculadoraPuntaje.nivel(null))
    }
}