package com.example.hertani.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hertani.model.Sesion
import com.example.hertani.ui.screens.LoginScreen
import com.example.hertani.ui.viewmodel.SesionViewModel

private const val DURACION = 320


@Composable
fun NavHost(
    navController: NavHostController,
    sesionViewModel: SesionViewModel,
    sesion: Sesion?,
    mostrarMensaje: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(DURACION)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(DURACION))
        },
        exitTransition = { fadeOut(tween(DURACION / 2)) },
        popEnterTransition = { fadeIn(tween(DURACION)) },
        popExitTransition = {
            fadeOut(tween(DURACION / 2)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(DURACION))
        }
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = sesionViewModel,
                onIngreso = {
                    navController.navigate(Rutas.CANDIDATAS) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        DestinoPrincipal.entries.forEach { destino ->
            composable(destino.ruta, enterTransition = { fadeIn(tween(DURACION)) }) {
                Text("${destino.etiqueta} (pantalla pendiente)")
            }
        }
    }
}