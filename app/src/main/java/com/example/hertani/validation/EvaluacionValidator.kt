package com.example.hertani.validation

import com.example.hertani.model.CalculadoraPuntaje

object EvaluacionValidator {

    const val UMBRAL_JUSTIFICACION = 2
    const val JUSTIFICACION_MIN = 10
    const val OBSERVACION_CRITERIO_MAX = 300
    const val OBSERVACION_GENERAL_MIN = 15
    const val OBSERVACION_GENERAL_MAX = 300

    fun validarPuntaje(puntaje: Int?): ValidationResult = when {
        puntaje == null ->
            ValidationResult.error("Asigna un puntaje de ${CalculadoraPuntaje.PUNTAJE_MIN} a ${CalculadoraPuntaje.PUNTAJE_MAX}")
        puntaje !in CalculadoraPuntaje.PUNTAJE_MIN..CalculadoraPuntaje.PUNTAJE_MAX ->
            ValidationResult.error("El puntaje debe estar entre ${CalculadoraPuntaje.PUNTAJE_MIN} y ${CalculadoraPuntaje.PUNTAJE_MAX}")
        else -> ValidationResult.Valido
    }

    fun validarObservacionCriterio(puntaje: Int?, observacion: String): ValidationResult {
        val largo = observacion.trim().length
        return when {
            largo > OBSERVACION_CRITERIO_MAX -> ValidationResult.error("Máximo $OBSERVACION_CRITERIO_MAX caracteres")
            puntaje != null && puntaje <= UMBRAL_JUSTIFICACION && largo < JUSTIFICACION_MIN ->
                ValidationResult.error("Justifica el puntaje bajo (mín. $JUSTIFICACION_MIN caracteres)")
            else -> ValidationResult.Valido
        }
    }

    fun validarObservacionGeneral(texto: String): ValidationResult {
        val largo = texto.trim().length
        return when {
            largo == 0 -> ValidationResult.error("La observación general es obligatoria")
            largo < OBSERVACION_GENERAL_MIN ->
                ValidationResult.error("Mínimo $OBSERVACION_GENERAL_MIN caracteres ($largo/$OBSERVACION_GENERAL_MIN)")
            largo > OBSERVACION_GENERAL_MAX -> ValidationResult.error("Máximo $OBSERVACION_GENERAL_MAX caracteres")
            else -> ValidationResult.Valido
        }
    }
}