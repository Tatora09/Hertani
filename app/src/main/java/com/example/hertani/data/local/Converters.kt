package com.example.hertani.data.local

import androidx.room.TypeConverter
import com.example.hertani.model.EstadoCandidatura

class Converters {
    @TypeConverter
    fun desdeEstado(estado: EstadoCandidatura): String = estado.name

    @TypeConverter
    fun haciaEstado(valor: String): EstadoCandidatura = EstadoCandidatura.valueOf(valor)
}