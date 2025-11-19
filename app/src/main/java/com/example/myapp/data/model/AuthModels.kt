package com.example.myapp.data.model

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)

data class SocialLoginRequest(
    val email: String,
    val name: String,
    val provider: String,
    val firebaseUid: String
)

data class AuthData(
    val id: String?,
    val name: String?,
    val email: String?,
    val role: String?,
    val token: String?
)

data class AuthResponse(
    val success: Boolean,
    val data: AuthData?,
    val error: String? = null
)

data class UserData(
    val _id: String?,
    val name: String?,
    val email: String?,
    val phone: String?
)

data class UserResponse(
    val success: Boolean,
    val data: UserData?
)
