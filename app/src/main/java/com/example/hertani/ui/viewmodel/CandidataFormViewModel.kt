package com.example.hertani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hertani.data.repository.CandidataRepository
import com.example.hertani.model.Candidata
import com.example.hertani.validation.CandidataValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CandidataFormUiState(
    val nombre: CampoState = CampoState(),
    val correo: CampoState = CampoState(),
    val anios: CampoState = CampoState(),
    val resumen: CampoState = CampoState(),
    val linkedin: CampoState = CampoState(),
    val certificaciones: CampoState = CampoState(),
    val area: String? = null,
    val errorArea: String? = null,
    val disponibilidad: String? = null,
    val errorDisponibilidad: String? = null,
    val cvUri: String? = null,
    val cvNombre: String? = null,
    val cvTamano: Long? = null,
    val errorCv: String? = null,
    val intentosFallidos: Int = 0,
    val guardando: Boolean = false,
    val guardado: Boolean = false,
    val mensaje: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, correo, anios, resumen, linkedin, certificaciones).any { it.error != null } ||
                errorArea != null || errorDisponibilidad != null || errorCv != null
}

class CandidataFormViewModel(private val repository: CandidataRepository) : ViewModel() {

    private val _ui = MutableStateFlow(CandidataFormUiState())
    val uiState: StateFlow<CandidataFormUiState> = _ui.asStateFlow()

    fun onNombre(v: String) = _ui.update { it.copy(nombre = it.nombre.con(v, CandidataValidator.validarNombre(v))) }
    fun onCorreo(v: String) = _ui.update { it.copy(correo = it.correo.con(v, CandidataValidator.validarCorreo(v))) }
    fun onAnios(v: String) = _ui.update { it.copy(anios = it.anios.con(v, CandidataValidator.validarAnios(v))) }
    fun onResumen(v: String) = _ui.update { it.copy(resumen = it.resumen.con(v, CandidataValidator.validarResumen(v))) }
    fun onLinkedin(v: String) = _ui.update { it.copy(linkedin = it.linkedin.con(v, CandidataValidator.validarLinkedin(v))) }
    fun onCertificaciones(v: String) = _ui.update {
        it.copy(certificaciones = it.certificaciones.con(v, CandidataValidator.validarCertificaciones(v)))
    }

    fun onArea(area: String) = _ui.update {
        it.copy(area = area, errorArea = CandidataValidator.validarArea(area).mensaje)
    }

    fun onDisponibilidad(valor: String) = _ui.update {
        it.copy(disponibilidad = valor, errorDisponibilidad = CandidataValidator.validarDisponibilidad(valor).mensaje)
    }

    fun onCv(uri: String, nombre: String?, tamano: Long?) = _ui.update {
        it.copy(cvUri = uri, cvNombre = nombre, cvTamano = tamano, errorCv = CandidataValidator.validarCv(nombre, tamano).mensaje)
    }

    fun mensajeMostrado() = _ui.update { it.copy(mensaje = null) }

    private fun validarTodo(): Boolean {
        val s = _ui.value
        val validado = s.copy(
            nombre = s.nombre.con(s.nombre.valor, CandidataValidator.validarNombre(s.nombre.valor)),
            correo = s.correo.con(s.correo.valor, CandidataValidator.validarCorreo(s.correo.valor)),
            anios = s.anios.con(s.anios.valor, CandidataValidator.validarAnios(s.anios.valor)),
            resumen = s.resumen.con(s.resumen.valor, CandidataValidator.validarResumen(s.resumen.valor)),
            linkedin = s.linkedin.con(s.linkedin.valor, CandidataValidator.validarLinkedin(s.linkedin.valor)),
            certificaciones = s.certificaciones.con(s.certificaciones.valor, CandidataValidator.validarCertificaciones(s.certificaciones.valor)),
            errorArea = CandidataValidator.validarArea(s.area).mensaje,
            errorDisponibilidad = CandidataValidator.validarDisponibilidad(s.disponibilidad).mensaje,
            errorCv = CandidataValidator.validarCv(s.cvNombre, s.cvTamano).mensaje
        )
        val esValido = !validado.hayErrores
        _ui.value = if (esValido) validado else validado.copy(intentosFallidos = s.intentosFallidos + 1)
        return esValido
    }

    fun guardar(registradaPor: String) {
        if (_ui.value.guardando || !validarTodo()) return
        val s = _ui.value
        _ui.update { it.copy(guardando = true) }
        viewModelScope.launch {
            try {
                if (repository.existeCorreo(s.correo.valor)) {
                    _ui.update {
                        it.copy(
                            guardando = false,
                            correo = it.correo.copy(error = "Ya existe una candidatura con este correo"),
                            intentosFallidos = it.intentosFallidos + 1
                        )
                    }
                    return@launch
                }
                repository.registrar(
                    Candidata(
                        nombre = s.nombre.valor.trim().replace(Regex("\\s+"), " "),
                        correo = s.correo.valor.trim().lowercase(),
                        area = s.area.orEmpty(),
                        aniosExperiencia = s.anios.valor.trim().toInt(),
                        resumenExperiencia = s.resumen.valor.trim(),
                        linkedinUrl = s.linkedin.valor.trim(),
                        disponibilidad = s.disponibilidad.orEmpty(),
                        certificaciones = s.certificaciones.valor.trim(),
                        cvUri = s.cvUri,
                        cvNombre = s.cvNombre,
                        registradaPor = registradaPor
                    )
                )
                _ui.update { it.copy(guardando = false, guardado = true) }
            } catch (e: Exception) {
                _ui.update { it.copy(guardando = false, mensaje = "No se pudo guardar: ${e.message}") }
            }
        }
    }
}