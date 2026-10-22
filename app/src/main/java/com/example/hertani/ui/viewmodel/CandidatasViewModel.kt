package com.example.hertani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hertani.data.repository.CandidataRepository
import com.example.hertani.model.Candidata
import com.example.hertani.model.EstadoCandidatura
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CandidatasUiState(
    val candidatas: List<Candidata> = emptyList(),
    val filtroEstado: EstadoCandidatura? = null,
    val filtroArea: String? = null,
    val total: Int = 0,
    val cargando: Boolean = true
)

class CandidatasViewModel(repository: CandidataRepository) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val filtroEstado = MutableStateFlow<EstadoCandidatura?>(null)
    private val filtroArea = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CandidatasUiState> =
        combine(repository.observarTodas(), _busqueda, filtroEstado, filtroArea) { lista, texto, estado, area ->
            val t = texto.trim()
            val filtradas = lista.filter { c ->
                (estado == null || c.estado == estado) &&
                        (area == null || c.area == area) &&
                        (t.isBlank() || c.nombre.contains(t, ignoreCase = true))
            }
            CandidatasUiState(filtradas, estado, area, lista.size, cargando = false)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CandidatasUiState())

    fun onBusqueda(texto: String) { _busqueda.value = texto }
    fun onFiltroEstado(estado: EstadoCandidatura?) { filtroEstado.value = estado }
    fun onFiltroArea(area: String?) { filtroArea.value = area }
}