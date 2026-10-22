package com.example.hertani.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CandidataValidatorTest {


    @Test fun nombre_valido() =
        assertTrue(CandidataValidator.validarNombre("Camila Rojas").esValido)

    @Test fun nombre_con_tilde_y_guion_es_valido() =
        assertTrue(CandidataValidator.validarNombre("Ana-Sofía Núñez").esValido)

    @Test fun nombre_vacio_es_invalido() {
        val r = CandidataValidator.validarNombre("  ")
        assertFalse(r.esValido)
        assertEquals("El nombre es obligatorio", r.mensaje)
    }

    @Test fun nombre_sin_apellido_es_invalido() {
        val r = CandidataValidator.validarNombre("Camila")
        assertFalse(r.esValido)
        assertEquals("Ingresa nombre y apellido", r.mensaje)
    }

    @Test fun nombre_con_numeros_es_invalido() =
        assertFalse(CandidataValidator.validarNombre("Camila R0jas").esValido)



    @Test fun correo_con_dominio_ficticio_es_valido() =
        assertTrue(CandidataValidator.validarCorreo("camila.rojas@hertani.test").esValido)

    @Test fun correo_real_es_rechazado_por_el_caso() =
        assertFalse(CandidataValidator.validarCorreo("camila@gmail.com").esValido)

    @Test fun correo_sin_arroba_es_invalido() =
        assertFalse(CandidataValidator.validarCorreo("camila.hertani.test").esValido)

    @Test fun correo_vacio_es_invalido() =
        assertFalse(CandidataValidator.validarCorreo("").esValido)



    @Test fun area_null_es_invalida() =
        assertFalse(CandidataValidator.validarArea(null).esValido)

    @Test fun area_fuera_de_la_lista_es_invalida() =
        assertFalse(CandidataValidator.validarArea("Cocina").esValido)

    @Test fun area_de_la_lista_es_valida() =
        assertTrue(CandidataValidator.validarArea("Ciberseguridad ofensiva").esValido)

    @Test fun disponibilidad_de_la_lista_es_valida() =
        assertTrue(CandidataValidator.validarDisponibilidad("Inmediata").esValido)

    @Test fun disponibilidad_vacia_es_invalida() =
        assertFalse(CandidataValidator.validarDisponibilidad("").esValido)


    @Test fun anios_validos() {
        assertTrue(CandidataValidator.validarAnios("8").esValido)
        assertTrue(CandidataValidator.validarAnios(" 8 ").esValido)
        assertTrue(CandidataValidator.validarAnios("0").esValido)
    }

    @Test fun anios_en_el_limite() {
        assertTrue(CandidataValidator.validarAnios("${CandidataValidator.ANIOS_MAX}").esValido)
        assertFalse(CandidataValidator.validarAnios("${CandidataValidator.ANIOS_MAX + 1}").esValido)
    }

    @Test fun anios_negativo_es_invalido() =
        assertFalse(CandidataValidator.validarAnios("-1").esValido)

    @Test fun anios_con_texto_es_invalido() =
        assertFalse(CandidataValidator.validarAnios("abc").esValido)

    @Test fun anios_vacio_es_invalido() =
        assertFalse(CandidataValidator.validarAnios("").esValido)


    @Test fun resumen_en_el_limite_inferior() {
        assertFalse(CandidataValidator.validarResumen("a".repeat(CandidataValidator.RESUMEN_MIN - 1)).esValido)
        assertTrue(CandidataValidator.validarResumen("a".repeat(CandidataValidator.RESUMEN_MIN)).esValido)
    }

    @Test fun resumen_en_el_limite_superior() {
        assertTrue(CandidataValidator.validarResumen("a".repeat(CandidataValidator.RESUMEN_MAX)).esValido)
        assertFalse(CandidataValidator.validarResumen("a".repeat(CandidataValidator.RESUMEN_MAX + 1)).esValido)
    }


    @Test fun linkedin_valido() =
        assertTrue(CandidataValidator.validarLinkedin("https://www.linkedin.com/in/camila-rojas-demo").esValido)

    @Test fun linkedin_sin_https_es_invalido() {
        val r = CandidataValidator.validarLinkedin("http://www.linkedin.com/in/camila-rojas-demo")
        assertFalse(r.esValido)
        assertEquals("Debe comenzar con https://", r.mensaje)
    }

    @Test fun linkedin_de_otro_sitio_es_invalido() =
        assertFalse(CandidataValidator.validarLinkedin("https://twitter.com/camila").esValido)

    @Test fun linkedin_con_usuario_muy_corto_es_invalido() =
        assertFalse(CandidataValidator.validarLinkedin("https://www.linkedin.com/in/ab").esValido)


    @Test fun certificaciones_vacias_son_validas() =
        assertTrue(CandidataValidator.validarCertificaciones("").esValido)

    @Test fun certificaciones_demasiado_largas_son_invalidas() =
        assertFalse(CandidataValidator.validarCertificaciones("a".repeat(CandidataValidator.CERTIFICACIONES_MAX + 1)).esValido)


    @Test fun cv_null_es_invalido() =
        assertFalse(CandidataValidator.validarCv(null, null).esValido)

    @Test fun cv_que_no_es_pdf_es_invalido() =
        assertFalse(CandidataValidator.validarCv("cv.docx", 1000L).esValido)

    @Test fun cv_muy_pesado_es_invalido() =
        assertFalse(CandidataValidator.validarCv("cv.pdf", CandidataValidator.CV_MAX_BYTES + 1).esValido)

    @Test fun cv_pdf_normal_es_valido() =
        assertTrue(CandidataValidator.validarCv("cv.PDF", 1000L).esValido)
}