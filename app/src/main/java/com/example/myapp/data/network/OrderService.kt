package com.example.myapp.data.network

import com.example.myapp.data.model.CreateOrderRequest
import com.example.myapp.data.model.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderService {
    @GET("orders")
    suspend fun getOrders(@Header("Authorization") token: String): OrderResponse

    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: CreateOrderRequest
    ): OrderResponse
}
