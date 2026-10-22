package com.example.hertani.data.repository

import com.example.hertani.data.local.dao.CandidataDao
import com.example.hertani.model.Acciones
import com.example.hertani.model.Candidata
import com.example.hertani.model.EstadoCandidatura
import kotlinx.coroutines.flow.Flow

class CandidataRepository(
    private val dao: CandidataDao,
    private val historial: HistorialRepository
) {
    fun observarTodas(): Flow<List<Candidata>> = dao.observarTodas()

    fun observarPorId(id: Long): Flow<Candidata?> = dao.observarPorId(id)

    suspend fun obtener(id: Long): Candidata? = dao.obtenerPorId(id)

    suspend fun contar(): Int = dao.contar()

    suspend fun existeCorreo(correo: String): Boolean = dao.contarPorCorreo(correo.trim()) > 0

    suspend fun registrar(candidata: Candidata): Long {
        val id = dao.insertar(candidata)
        historial.registrar(
            candidataId = id,
            accion = Acciones.REGISTRO_CANDIDATA,
            detalle = "Se registró a ${candidata.nombre} · ${candidata.area}",
            usuario = candidata.registradaPor
        )
        return id
    }

    suspend fun resolver(id: Long, estado: EstadoCandidatura, usuario: String) {
        require(estado.decision) { "Solo se puede resolver como Seleccionada o No seleccionada" }
        val actual = dao.obtenerPorId(id) ?: return
        dao.actualizarEstado(id, estado)
        historial.registrar(
            candidataId = id,
            accion = Acciones.CANDIDATA_RESUELTA,
            detalle = "${actual.nombre}: ${actual.estado.etiqueta} → ${estado.etiqueta}",
            usuario = usuario
        )
    }
}