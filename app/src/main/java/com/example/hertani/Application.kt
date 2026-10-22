package com.example.hertani

import android.app.Application
import com.example.hertani.data.local.DatosSemilla
import com.example.hertani.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Application : Application() {

    lateinit var container: AppContainer
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // En segundo plano: no bloquea la apertura de la app
        scope.launch { DatosSemilla.poblarSiVacia(container) }
    }
}