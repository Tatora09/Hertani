package com.example.hertani.data.repository

import com.example.hertani.data.local.dao.HistorialDao
import com.example.hertani.model.RegistroHistorial
import kotlinx.coroutines.flow.Flow

class HistorialRepository(private val dao: HistorialDao) {

    fun observarTodo(): Flow<List<RegistroHistorial>> = dao.observarTodo()

    fun observarPorCandidata(candidataId: Long): Flow<List<RegistroHistorial>> =
        dao.observarPorCandidata(candidataId)

    suspend fun registrar(candidataId: Long?, accion: String, detalle: String, usuario: String) {
        dao.insertar(
            RegistroHistorial(
                candidataId = candidataId,
                usuario = usuario,
                accion = accion,
                detalle = detalle
            )
        )
    }
}