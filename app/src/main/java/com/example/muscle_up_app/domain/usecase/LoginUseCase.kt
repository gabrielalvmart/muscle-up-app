package com.example.muscle_up_app.domain.usecase

import com.example.muscle_up_app.data.repository.UserRepository

interface LoginUseCase {
    suspend fun login(username: String, password: String): Boolean
}

class DefaultLoginUseCase(private val repository: UserRepository) : LoginUseCase {
    override suspend fun login(username: String, password: String): Boolean {
        // Replace with your actual authentication logic (e.g., network call)
        return username == "admin" && password == "password123"
    }
}