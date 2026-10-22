package com.example.hertani.data.local

import com.example.hertani.di.AppContainer
import com.example.hertani.model.Candidata
import com.example.hertani.model.Criterio

object DatosSemilla {

    val criterios = listOf(
        Criterio(nombre = "Experiencia técnica", descripcion = "Profundidad y años de trabajo en el área.", peso = 30, orden = 1),
        Criterio(nombre = "Liderazgo", descripcion = "Capacidad de guiar equipos o iniciativas.", peso = 25, orden = 2),
        Criterio(nombre = "Impacto en la comunidad", descripcion = "Mentorías, charlas y apoyo a otras mujeres del rubro.", peso = 25, orden = 3),
        Criterio(nombre = "Trayectoria y logros", descripcion = "Reconocimientos, certificaciones y resultados destacados.", peso = 20, orden = 4)
    )

    private data class Demo(
        val nombre: String, val correo: String, val area: String, val anios: Int,
        val resumen: String, val linkedin: String, val disponibilidad: String,
        val certificaciones: String
    )

    // Hay q agregar mas datos de candidatas, unas 3 más
    private val candidatas = listOf(
        Demo("Camila Rojas Vega", "camila.rojas@hertani.test", Candidata.AREAS[0], 8,
            "Pentester en una consultora ficticia. Lidera ejercicios de Red Team y capacita a analistas junior.",
            "https://www.linkedin.com/in/camila-rojas-demo", Candidata.DISPONIBILIDADES[0],
            "Certificación ofensiva ficticia"),
        Demo("Paula Araya Muñoz", "paula.araya@hertani.test", Candidata.AREAS[1], 6,
            "Jefa de un SOC ficticio. Diseñó guías de respuesta que redujeron los tiempos de atención.",
            "https://www.linkedin.com/in/paula-araya-demo", Candidata.DISPONIBILIDADES[1],
            "Certificación defensiva ficticia"),
        Demo("Sofía Lagos Pérez", "sofia.lagos@hertani.test", Candidata.AREAS[2], 11,
            "Especialista en gobierno de seguridad de una empresa ficticia. Mentora en comunidades técnicas.",
            "https://www.linkedin.com/in/sofia-lagos-demo", Candidata.DISPONIBILIDADES[2],
            "Norma ISO ficticia")
    )

    suspend fun poblarSiVacia(container: AppContainer) {
        container.evaluacionRepository.asegurarCriterios(criterios)
        if (container.candidataRepository.contar() > 0) return

        val ids = candidatas.map { d ->
            container.candidataRepository.registrar(
                Candidata(
                    nombre = d.nombre, correo = d.correo, area = d.area,
                    aniosExperiencia = d.anios, resumenExperiencia = d.resumen,
                    linkedinUrl = d.linkedin, disponibilidad = d.disponibilidad,
                    certificaciones = d.certificaciones,
                    cvUri = null, cvNombre = null,
                    registradaPor = "Sistema (datos semilla)"
                )
            )
        }

        val c = container.evaluacionRepository.obtenerCriterios().map { it.id }
        fun p(vararg v: Int): Map<Long, Int> = c.zip(v.toList()).toMap()
        val ev = container.evaluacionRepository

        ev.registrar(ids[0], "Evaluadora Demo Uno", p(4, 4, 3, 4), emptyMap(), "Perfil técnico muy sólido.")
        ev.registrar(ids[0], "Evaluadora Demo Dos", p(4, 3, 3, 4), emptyMap(), "Destaca como formadora.")
        ev.registrar(ids[1], "Evaluadora Demo Uno", p(3, 4, 2, 3), emptyMap(), "Gran gestión; aporte a la comunidad en desarrollo.")
    }
}