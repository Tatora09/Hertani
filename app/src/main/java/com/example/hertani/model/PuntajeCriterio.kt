package com.example.hertani.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "puntajes_criterio",
    foreignKeys = [
        ForeignKey(entity = Evaluacion::class, parentColumns = ["id"],
            childColumns = ["evaluacionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Criterio::class, parentColumns = ["id"],
            childColumns = ["criterioId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["evaluacionId"]), Index(value = ["criterioId"])]
)
data class PuntajeCriterio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val evaluacionId: Long,
    val criterioId: Long,
    val puntaje: Int,
    val observacion: String
)

data class DetallePuntaje(
    val evaluacionId: Long,
    val criterioNombre: String,
    val peso: Int,
    val puntaje: Int,
    val observacion: String
)