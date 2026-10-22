package com.example.hertani.validation

data class ValidationResult(
    val esValido: Boolean,
    val mensaje: String? = null
) {
    companion object {
        val Valido = ValidationResult(true)
        fun error(mensaje: String) = ValidationResult(false, mensaje)
    }
}