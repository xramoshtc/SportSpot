package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.domain.model.User

class LoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(user: String, password: String): User {
        return repo.login(user, password)
    }
}