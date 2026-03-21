package com.example.sportspot.data.repository

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.remote.LoginRequest
import com.example.sportspot.data.remote.LogoutRequest
import com.example.sportspot.data.remote.RetrofitInstance
import com.example.sportspot.domain.model.User

class AuthRepository(
    private val dataStore: DataStoreManager
) {

    private val api = RetrofitInstance.authApi

    suspend fun login(user: String, password: String): User {
        val response = api.login(LoginRequest(user, password))

        if (!response.success || response.resultCode != 200) {
            throw Exception("Credencials incorrectes")
        }

        dataStore.saveToken(response.sessionToken)

        return User(
            role = response.role,
            token = response.sessionToken
        )
    }

    suspend fun logout(token: String) {
        api.logout(LogoutRequest(token))   // ← llamada al servidor
        dataStore.clearToken()             // ← borramos token local
    }


}