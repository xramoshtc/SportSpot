package com.example.sportspot.data.remote

import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(
    val user: String,
    val password: String
)

data class LogoutRequest(
    val token: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val resultCode: Int,
    val sessionToken: String,
    val role: String,
    val permissions: String
)

interface AuthApi {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/logout")
    suspend fun logout(@Body request: LogoutRequest): String
}

