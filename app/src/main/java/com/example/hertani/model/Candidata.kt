package com.example.hertani.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "candidatas",
    indices = [Index(value = ["correo"], unique = true)]
)

data class Candidata(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val area: String,
    val aniosExperiencia: Int,
    val resumenExperiencia: String,
    val linkedinUrl: String,
    val disponibilidad: String,
    val certificaciones: String,
    val cvUri: String?,
    val cvNombre: String?,
    val estado: EstadoCandidatura = EstadoCandidatura.POSTULADA,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val registradaPor: String
) {
    companion object {
        val AREAS = listOf(
            "Ciberseguridad ofensiva",
            "Ciberseguridad defensiva",
            "Gobierno y cumplimiento",
            "Seguridad industrial",
            "Investigación y docencia"
        )
        val DISPONIBILIDADES = listOf("Inmediata", "1 mes", "3 meses")
    }
}