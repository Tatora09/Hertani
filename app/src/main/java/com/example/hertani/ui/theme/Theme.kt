package com.example.hertani.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val EsquemaClaro = lightColorScheme(
    primary = Cobre40, onPrimary = Color.White,
    primaryContainer = Cobre90, onPrimaryContainer = Cobre10,
    secondary = Verde40, onSecondary = Color.White,
    secondaryContainer = Verde90, onSecondaryContainer = Verde10,
    tertiary = Ocre40, onTertiary = Color.White,
    tertiaryContainer = Ocre90, onTertiaryContainer = Ocre10,
    background = Fondo, surface = Fondo
)

private val EsquemaOscuro = darkColorScheme(
    primary = Cobre80, onPrimary = Cobre10,
    primaryContainer = Cobre30, onPrimaryContainer = Cobre90,
    secondary = Verde80, onSecondary = Verde10,
    secondaryContainer = Verde30, onSecondaryContainer = Verde90,
    tertiary = Ocre80, onTertiary = Ocre10,
    tertiaryContainer = Ocre30, onTertiaryContainer = Ocre90,
    background = FondoOscuro, surface = FondoOscuro
)

@Composable
fun HertaniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> EsquemaOscuro
        else -> EsquemaClaro
    }
    MaterialTheme(colorScheme = colorScheme, typography = Tipografia, content = content)
}