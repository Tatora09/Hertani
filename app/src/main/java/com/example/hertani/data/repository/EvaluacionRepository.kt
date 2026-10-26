package com.example.hertani.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import com.example.hertani.data.local.HertaniDatabase
import com.example.hertani.model.Acciones
import com.example.hertani.model.CalculadoraPuntaje
import com.example.hertani.model.Criterio
import com.example.hertani.model.DetallePuntaje
import com.example.hertani.model.EstadoCandidatura
import com.example.hertani.model.Evaluacion
import com.example.hertani.model.PuntajeCriterio
import com.example.hertani.model.ResultadoConsolidado
import com.example.hertani.util.formatearPuntaje
import kotlinx.coroutines.flow.Flow

class EvaluacionDuplicadaException(mensaje: String) : Exception(mensaje)

class EvaluacionRepository(
    private val db: HertaniDatabase,
    private val historial: HistorialRepository
) {
    private val evaluacionDao = db.evaluacionDao()
    private val criterioDao = db.criterioDao()
    private val candidataDao = db.candidataDao()

    fun observarCriterios(): Flow<List<Criterio>> = criterioDao.observarTodos()

    suspend fun obtenerCriterios(): List<Criterio> = criterioDao.obtenerTodos()

    suspend fun asegurarCriterios(criterios: List<Criterio>) {
        if (criterioDao.contar() == 0) criterioDao.insertarTodos(criterios)
    }

    fun observarPorCandidata(candidataId: Long): Flow<List<Evaluacion>> =
        evaluacionDao.observarPorCandidata(candidataId)

    fun observarDetallePuntajes(candidataId: Long): Flow<List<DetallePuntaje>> =
        evaluacionDao.observarDetallePuntajes(candidataId)

    fun observarConsolidado(): Flow<List<ResultadoConsolidado>> =
        evaluacionDao.observarConsolidado()

    suspend fun yaEvaluo(candidataId: Long, evaluadora: String): Boolean =
        evaluacionDao.contarPorEvaluadora(candidataId, Evaluacion.normalizarClave(evaluadora)) > 0

    suspend fun registrar(
        candidataId: Long,
        evaluadora: String,
        puntajes: Map<Long, Int>,
        observaciones: Map<Long, String>,
        observacionGeneral: String
    ): Double {
        val clave = Evaluacion.normalizarClave(evaluadora)
        try {
            return db.withTransaction {
                val candidata = candidataDao.obtenerPorId(candidataId)
                    ?: throw IllegalStateException("La candidatura no existe")
                if (candidata.estado.decision) {
                    throw IllegalStateException("La candidatura ya fue resuelta por Dirección")
                }
                if (evaluacionDao.contarPorEvaluadora(candidataId, clave) > 0) {
                    throw EvaluacionDuplicadaException("Ya registraste una evaluación para ${candidata.nombre}")
                }

                val criterios = criterioDao.obtenerTodos()
                val total = CalculadoraPuntaje.calcular(puntajes, criterios)

                val evaluacionId = evaluacionDao.insertar(
                    Evaluacion(
                        candidataId = candidataId,
                        evaluadora = evaluadora.trim(),
                        evaluadoraClave = clave,
                        puntajeTotal = total,
                        observacionGeneral = observacionGeneral.trim()
                    )
                )
                evaluacionDao.insertarPuntajes(
                    criterios.map { criterio ->
                        PuntajeCriterio(
                            evaluacionId = evaluacionId,
                            criterioId = criterio.id,
                            puntaje = puntajes.getValue(criterio.id),
                            observacion = observaciones[criterio.id].orEmpty().trim()
                        )
                    }
                )

                val cantidad = evaluacionDao.contarPorCandidata(candidataId)
                val nuevoEstado = if (cantidad >= Evaluacion.MINIMO_PARA_CONSOLIDAR) {
                    EstadoCandidatura.EVALUADA
                } else {
                    EstadoCandidatura.EN_EVALUACION
                }
                candidataDao.actualizarEstado(candidataId, nuevoEstado)

                historial.registrar(
                    candidataId = candidataId,
                    accion = Acciones.EVALUACION_REGISTRADA,
                    detalle = "${candidata.nombre}: ${formatearPuntaje(total)} pts · ${nuevoEstado.etiqueta}",
                    usuario = evaluadora.trim()
                )
                total
            }
        } catch (e: EvaluacionDuplicadaException) {
            registrarIntentoDuplicado(candidataId, evaluadora)
            throw e
        } catch (e: SQLiteConstraintException) {
            registrarIntentoDuplicado(candidataId, evaluadora)
            throw EvaluacionDuplicadaException("Ya existe una evaluación tuya para esta candidata")
        }
    }

    private suspend fun registrarIntentoDuplicado(candidataId: Long, evaluadora: String) {
        historial.registrar(
            candidataId = candidataId,
            accion = Acciones.EVALUACION_DUPLICADA,
            detalle = "El sistema bloqueó una segunda evaluación",
            usuario = evaluadora.trim()
        )
    }
}