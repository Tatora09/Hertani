package com.example.hertani.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "evaluaciones",
    foreignKeys = [
        ForeignKey(
            entity = Candidata::class,
            parentColumns = ["id"],
            childColumns = ["candidataId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["candidataId", "evaluadoraClave"], unique = true)]
)
data class Evaluacion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val candidataId: Long,
    val evaluadora: String,
    val evaluadoraClave: String,
    val puntajeTotal: Double,
    val observacionGeneral: String,
    val fecha: Long = System.currentTimeMillis()
) {
    companion object {
        const val MINIMO_PARA_CONSOLIDAR = 2

        fun normalizarClave(nombre: String): String =
            nombre.trim().lowercase().replace(Regex("\\s+"), " ")
    }
}