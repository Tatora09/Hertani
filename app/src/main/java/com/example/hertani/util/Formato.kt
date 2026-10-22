package com.example.hertani.util

import java.util.Locale

fun formatearPuntaje(valor: Double?): String =
    if (valor == null) "—" else String.format(Locale.forLanguageTag("es-CL"), "%.1f", valor)