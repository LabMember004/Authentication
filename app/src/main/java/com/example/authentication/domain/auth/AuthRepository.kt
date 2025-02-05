package com.example.authentication.domain.auth

interface AuthRepository {
    suspend fun register (email: String, password: String): Result<Unit>
    suspend fun login( email: String, password: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
}