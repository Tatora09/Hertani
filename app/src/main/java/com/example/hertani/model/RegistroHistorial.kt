package com.example.hertani.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial")
data class RegistroHistorial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val candidataId: Long?,
    val usuario: String,
    val accion: String,
    val detalle: String,
    val fecha: Long = System.currentTimeMillis()
)

object Acciones {
    const val INICIO_SESION = "Inicio de sesión"
    const val CIERRE_SESION = "Cierre de sesión"
    const val REGISTRO_CANDIDATA = "Registro de candidatura"
    const val EVALUACION_REGISTRADA = "Evaluación registrada"
    const val EVALUACION_DUPLICADA = "Intento de evaluación duplicada"
    const val CANDIDATA_RESUELTA = "Candidatura resuelta"
    const val CONSULTA_CV = "Consulta de CV"
    const val CONSULTA_PERFIL = "Consulta de perfil profesional"
    const val COMPARTIR = "Resumen compartido"
}