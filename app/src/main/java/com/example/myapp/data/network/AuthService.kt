package com.example.myapp.data.network

import com.example.myapp.data.model.AuthResponse
import com.example.myapp.data.model.LoginRequest
import com.example.myapp.data.model.RegisterRequest
import com.example.myapp.data.model.SocialLoginRequest
import com.example.myapp.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/social")
    suspend fun socialLogin(@Body body: SocialLoginRequest): AuthResponse

    @GET("auth/me")
    suspend fun me(@Header("Authorization") token: String): UserResponse
}
