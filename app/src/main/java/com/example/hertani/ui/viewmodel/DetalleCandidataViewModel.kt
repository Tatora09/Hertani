package com.example.hertani.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hertani.data.repository.CandidataRepository
import com.example.hertani.data.repository.EvaluacionRepository
import com.example.hertani.data.repository.HistorialRepository
import com.example.hertani.model.Candidata
import com.example.hertani.model.DetallePuntaje
import com.example.hertani.model.EstadoCandidatura
import com.example.hertani.model.Evaluacion
import com.example.hertani.model.RegistroHistorial
import com.example.hertani.navigation.Rutas
import com.example.hertani.util.formatearPuntaje
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUiState(
    val candidata: Candidata? = null,
    val evaluaciones: List<Evaluacion> = emptyList(),
    val puntajes: List<DetallePuntaje> = emptyList(),
    val historial: List<RegistroHistorial> = emptyList(),
    val cargando: Boolean = true
) {
    val promedio: Double?
        get() = evaluaciones.takeIf { it.isNotEmpty() }?.map { it.puntajeTotal }?.average()

    fun yaEvaluo(evaluadora: String): Boolean {
        val clave = Evaluacion.normalizarClave(evaluadora)
        return evaluaciones.any { it.evaluadoraClave == clave }
    }
}

class DetalleCandidataViewModel(
    savedStateHandle: SavedStateHandle,
    private val candidataRepository: CandidataRepository,
    evaluacionRepository: EvaluacionRepository,
    private val historialRepository: HistorialRepository
) : ViewModel() {

    private val candidataId: Long = checkNotNull(savedStateHandle[Rutas.ARG_ID])

    val uiState: StateFlow<DetalleUiState> = combine(
        candidataRepository.observarPorId(candidataId),
        evaluacionRepository.observarPorCandidata(candidataId),
        evaluacionRepository.observarDetallePuntajes(candidataId),
        historialRepository.observarPorCandidata(candidataId)
    ) { candidata, evaluaciones, puntajes, historial ->
        DetalleUiState(candidata, evaluaciones, puntajes, historial, cargando = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetalleUiState())

    fun registrarAccion(accion: String, detalle: String, usuario: String) {
        viewModelScope.launch { historialRepository.registrar(candidataId, accion, detalle, usuario) }
    }

    fun resolver(estado: EstadoCandidatura, usuario: String) {
        viewModelScope.launch { candidataRepository.resolver(candidataId, estado, usuario) }
    }

    fun textoResumen(): String {
        val s = uiState.value
        val c = s.candidata ?: return ""
        return buildString {
            appendLine("Hertani · Resumen de candidatura (datos ficticios)")
            appendLine("Candidata: ${c.nombre}")
            appendLine("Área: ${c.area} · ${c.aniosExperiencia} años · Disponibilidad: ${c.disponibilidad}")
            appendLine("Estado: ${c.estado.etiqueta}")
            appendLine("Evaluaciones: ${s.evaluaciones.size} · Promedio: ${formatearPuntaje(s.promedio)} pts")
            s.evaluaciones.forEach { appendLine("• ${it.evaluadora}: ${formatearPuntaje(it.puntajeTotal)} pts") }
        }
    }
}