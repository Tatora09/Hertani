package com.example.hertani.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hertani.Application

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { SesionViewModel(app().container.historialRepository) }
        initializer { CandidatasViewModel(app().container.candidataRepository) }
        initializer { CandidataFormViewModel(app().container.candidataRepository) }
        initializer {
            DetalleCandidataViewModel(
                createSavedStateHandle(),
                app().container.candidataRepository,
                app().container.evaluacionRepository,
                app().container.historialRepository
            )
        }
        initializer {
            EvaluacionViewModel(
                createSavedStateHandle(),
                app().container.candidataRepository,
                app().container.evaluacionRepository
            )
        }
        initializer { ResultadosViewModel(app().container.evaluacionRepository) }
        initializer { HistorialViewModel(app().container.historialRepository) }
    }
}

private fun CreationExtras.app(): Application =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application