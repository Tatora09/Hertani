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
import com.example.hertani.ui.screens.CandidatasScreen
import com.example.hertani.ui.screens.CandidataFormScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.hertani.ui.screens.DetalleCandidataScreen
import com.example.hertani.ui.screens.EvaluacionScreen

private const val DURACION = 320


@Composable
fun NavHost(
    navController: NavHostController,
    sesionViewModel: SesionViewModel,
    sesion: Sesion?,
    mostrarMensaje: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val argumentoId = listOf(navArgument(Rutas.ARG_ID) { type = NavType.LongType })
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
        composable(Rutas.CANDIDATAS, enterTransition = { fadeIn(tween(DURACION)) }) {
            if (sesion != null) {
                CandidatasScreen(
                    sesion = sesion,
                    onNueva = { navController.navigate(Rutas.NUEVA_CANDIDATA) },
                    onAbrir = { id -> navController.navigate(Rutas.detalle(id)) }
                )
            }
        }

        composable(Rutas.NUEVA_CANDIDATA) {
            if (sesion != null) {
                CandidataFormScreen(
                    sesion = sesion,
                    onVolver = { navController.popBackStack() },
                    mostrarMensaje = mostrarMensaje
                )
            }
        }

        composable(Rutas.DETALLE, arguments = argumentoId) {
            if (sesion != null) {
                DetalleCandidataScreen(
                    sesion = sesion,
                    onVolver = { navController.popBackStack() },
                    onEvaluar = { id -> navController.navigate(Rutas.evaluar(id)) },
                    mostrarMensaje = mostrarMensaje
                )
            }
        }

        composable(Rutas.EVALUAR, arguments = argumentoId) {
            if (sesion != null) {
                EvaluacionScreen(
                    sesion = sesion,
                    onVolver = { navController.popBackStack() },
                    mostrarMensaje = mostrarMensaje
                )
            }
        }

        DestinoPrincipal.entries.filter { it != DestinoPrincipal.CANDIDATAS }.forEach { destino ->
            composable(destino.ruta, enterTransition = { fadeIn(tween(DURACION)) }) {
                Text("${destino.etiqueta} (pantalla pendiente)")
            }
        }
    }
}