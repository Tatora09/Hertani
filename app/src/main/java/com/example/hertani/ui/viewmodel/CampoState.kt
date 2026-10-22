package com.example.hertani.ui.viewmodel

import com.example.hertani.validation.ValidationResult

data class CampoState(
    val valor: String = "",
    val error: String? = null
) {
    val esValido: Boolean get() = error == null && valor.isNotBlank()

    fun con(valor: String, resultado: ValidationResult) = copy(valor = valor, error = resultado.mensaje)
}