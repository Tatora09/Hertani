package com.example.hertani.data.network

import com.example.hertani.model.Candidata
import retrofit2.Response
import retrofit2.http.*

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val token: String?,
    val username: String,
    val nombre: String,
    val rol: String
)

data class EstadoRequestDto(
    val estado: String
)

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("api/postulantes")
    suspend fun listarPostulantes(): Response<List<Candidata>>

    @GET("api/postulantes/{id}")
    suspend fun obtenerPostulante(@Path("id") id: Long): Response<Candidata>

    @POST("api/postulantes")
    suspend fun registrarPostulante(@Body postulante: Candidata): Response<Candidata>

    @PUT("api/postulantes/{id}")
    suspend fun editarPostulante(@Path("id") id: Long, @Body postulante: Candidata): Response<Candidata>

    @PATCH("api/postulantes/{id}/estado")
    suspend fun cambiarEstado(@Path("id") id: Long, @Body request: EstadoRequestDto): Response<Candidata>

    @DELETE("api/postulantes/{id}")
    suspend fun eliminarPostulante(@Path("id") id: Long): Response<Unit>
}
