package com.example.hertani.validation

import com.example.hertani.model.CalculadoraPuntaje
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluacionValidatorTest {

    @Test fun puntaje_dentro_de_la_escala_es_valido() {
        for (p in CalculadoraPuntaje.PUNTAJE_MIN..CalculadoraPuntaje.PUNTAJE_MAX) {
            assertTrue(EvaluacionValidator.validarPuntaje(p).esValido)
        }
    }

    @Test fun puntaje_fuera_de_la_escala_es_invalido() {
        assertFalse(EvaluacionValidator.validarPuntaje(CalculadoraPuntaje.PUNTAJE_MIN - 1).esValido)
        assertFalse(EvaluacionValidator.validarPuntaje(CalculadoraPuntaje.PUNTAJE_MAX + 1).esValido)
    }

    @Test fun puntaje_sin_asignar_es_invalido() =
        assertFalse(EvaluacionValidator.validarPuntaje(null).esValido)

    @Test fun puntaje_bajo_sin_justificacion_es_invalido() =
        assertFalse(EvaluacionValidator.validarObservacionCriterio(EvaluacionValidator.UMBRAL_JUSTIFICACION, "").esValido)

    @Test fun puntaje_bajo_con_justificacion_corta_es_invalido() {
        val corta = "a".repeat(EvaluacionValidator.JUSTIFICACION_MIN - 1)
        assertFalse(EvaluacionValidator.validarObservacionCriterio(1, corta).esValido)
    }

    @Test fun puntaje_bajo_con_justificacion_suficiente_es_valido() {
        val texto = "a".repeat(EvaluacionValidator.JUSTIFICACION_MIN)
        assertTrue(EvaluacionValidator.validarObservacionCriterio(1, texto).esValido)
    }

    @Test fun puntaje_sobre_el_umbral_no_exige_justificacion() =
        assertTrue(EvaluacionValidator.validarObservacionCriterio(EvaluacionValidator.UMBRAL_JUSTIFICACION + 1, "").esValido)

    @Test fun observacion_de_criterio_muy_larga_es_invalida() {
        val larga = "a".repeat(EvaluacionValidator.OBSERVACION_CRITERIO_MAX + 1)
        assertFalse(EvaluacionValidator.validarObservacionCriterio(4, larga).esValido)
    }

    @Test fun observacion_general_vacia_es_invalida() =
        assertFalse(EvaluacionValidator.validarObservacionGeneral("   ").esValido)

    @Test fun observacion_general_en_los_limites() {
        assertFalse(EvaluacionValidator.validarObservacionGeneral("a".repeat(EvaluacionValidator.OBSERVACION_GENERAL_MIN - 1)).esValido)
        assertTrue(EvaluacionValidator.validarObservacionGeneral("a".repeat(EvaluacionValidator.OBSERVACION_GENERAL_MIN)).esValido)
        assertTrue(EvaluacionValidator.validarObservacionGeneral("a".repeat(EvaluacionValidator.OBSERVACION_GENERAL_MAX)).esValido)
        assertFalse(EvaluacionValidator.validarObservacionGeneral("a".repeat(EvaluacionValidator.OBSERVACION_GENERAL_MAX + 1)).esValido)
    }
}