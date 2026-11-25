package com.example.hertani.data.repository

import com.example.hertani.data.local.dao.CandidataDao
import com.example.hertani.data.network.RetrofitClient
import com.example.hertani.model.Acciones
import com.example.hertani.model.Candidata
import com.example.hertani.model.EstadoCandidatura
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class CandidataRepository(
    private val dao: CandidataDao,
    private val historial: HistorialRepository,
    private val api: com.example.hertani.data.network.ApiService = RetrofitClient.apiService
) {
    fun observarTodas(): Flow<List<Candidata>> = dao.observarTodas()

    fun observarPorId(id: Long): Flow<Candidata?> = dao.observarPorId(id)

    suspend fun obtener(id: Long): Candidata? = dao.obtenerPorId(id)

    suspend fun contar(): Int = dao.contar()

    suspend fun existeCorreo(correo: String): Boolean = dao.contarPorCorreo(correo.trim()) > 0

    suspend fun sincronizarConServidor() {
        try {
            val response = api.listarPostulantes()
            if (response.isSuccessful && response.body() != null) {
                val remotas = response.body()!!
                for (c in remotas) {
                    if (dao.obtenerPorId(c.id) == null) {
                        dao.insertar(c)
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback offline
        }
    }

    suspend fun registrar(candidata: Candidata): Long {
        try {
            val response = api.registrarPostulante(candidata)
            if (response.isSuccessful && response.body() != null) {
                val creada = response.body()!!
                val id = dao.insertar(creada)
                historial.registrar(
                    candidataId = id,
                    accion = Acciones.REGISTRO_CANDIDATA,
                    detalle = "Se registró a ${creada.nombre} · ${creada.area} (MySQL / Backend)",
                    usuario = candidata.registradaPor
                )
                return id
            } else if (response.code() == 409) {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 409) throw e
        }

        // Fallback local Room si falla la red
        val id = dao.insertar(candidata)
        historial.registrar(
            candidataId = id,
            accion = Acciones.REGISTRO_CANDIDATA,
            detalle = "Se registró a ${candidata.nombre} · ${candidata.area} (Local Room)",
            usuario = candidata.registradaPor
        )
        return id
    }

    suspend fun resolver(id: Long, estado: EstadoCandidatura, usuario: String) {
        require(estado.decision) { "Solo se puede resolver como Seleccionada o No seleccionada" }
        val actual = dao.obtenerPorId(id) ?: return
        dao.actualizarEstado(id, estado)
        try {
            api.cambiarEstado(id, com.example.hertani.data.network.EstadoRequestDto(estado.name))
        } catch (e: Exception) {
            // Fallback offline
        }
        historial.registrar(
            candidataId = id,
            accion = Acciones.CANDIDATA_RESUELTA,
            detalle = "${actual.nombre}: ${actual.estado.etiqueta} → ${estado.etiqueta}",
            usuario = usuario
        )
    }
}
