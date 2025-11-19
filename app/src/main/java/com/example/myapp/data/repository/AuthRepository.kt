package com.example.myapp.data.repository

import com.example.myapp.data.model.*
import com.example.myapp.data.network.ApiClient
import com.example.myapp.data.network.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val service: AuthService = ApiClient.retrofit.create(AuthService::class.java)

    suspend fun login(email: String, password: String): AuthResponse = withContext(Dispatchers.IO) {
        service.login(LoginRequest(email, password))
    }

    suspend fun register(name: String, email: String, password: String, phone: String): AuthResponse = withContext(Dispatchers.IO) {
        service.register(RegisterRequest(name, email, password, phone))
    }

    suspend fun me(token: String): UserResponse = withContext(Dispatchers.IO) {
        service.me("Bearer $token")
    }

    suspend fun socialLogin(email: String, name: String, provider: String, firebaseUid: String): AuthResponse = withContext(Dispatchers.IO) {
        service.socialLogin(SocialLoginRequest(email, name, provider, firebaseUid))
    }
}
