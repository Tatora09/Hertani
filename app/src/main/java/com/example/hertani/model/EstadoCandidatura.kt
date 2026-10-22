package com.example.hertani.model

enum class EstadoCandidatura(val etiqueta: String) {
    POSTULADA("Postulada"),
    EN_EVALUACION("En Evaluación"),
    EVALUADA("Evaluada"),
    SELECCIONADA("Seleccionada"),
    NO_SELECCIONADA("No Seleccionada");

    val decision: Boolean
        get() = this == SELECCIONADA || this == NO_SELECCIONADA
}