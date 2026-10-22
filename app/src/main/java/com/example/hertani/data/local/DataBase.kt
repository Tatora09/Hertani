package com.example.hertani.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hertani.data.local.dao.CandidataDao
import com.example.hertani.data.local.dao.CriterioDao
import com.example.hertani.data.local.dao.EvaluacionDao
import com.example.hertani.data.local.dao.HistorialDao
import com.example.hertani.model.Candidata
import com.example.hertani.model.Criterio
import com.example.hertani.model.Evaluacion
import com.example.hertani.model.PuntajeCriterio
import com.example.hertani.model.RegistroHistorial

@Database(
    entities = [
        Candidata::class,
        Criterio::class,
        Evaluacion::class,
        PuntajeCriterio::class,
        RegistroHistorial::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HertaniDatabase : RoomDatabase() {

    abstract fun candidataDao(): CandidataDao
    abstract fun criterioDao(): CriterioDao
    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun historialDao(): HistorialDao

    companion object {
        @Volatile
        private var instancia: HertaniDatabase? = null

        fun obtener(context: Context): HertaniDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    HertaniDatabase::class.java,
                    "hertani.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instancia = it }
            }
    }
}