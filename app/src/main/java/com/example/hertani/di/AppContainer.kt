package com.example.hertani.di

import android.content.Context
import com.example.hertani.data.local.HertaniDatabase
import com.example.hertani.data.network.RetrofitClient
import com.example.hertani.data.repository.CandidataRepository
import com.example.hertani.data.repository.EvaluacionRepository
import com.example.hertani.data.repository.HistorialRepository

class AppContainer(context: Context) {
    private val db = HertaniDatabase.obtener(context)

    val historialRepository = HistorialRepository(db.historialDao())
    val candidataRepository = CandidataRepository(db.candidataDao(), historialRepository, RetrofitClient.apiService)
    val evaluacionRepository = EvaluacionRepository(db, historialRepository)
}
