package com.example.myapp.data.repository

import com.example.myapp.data.model.CreateOrderRequest
import com.example.myapp.data.model.OrderResponse
import com.example.myapp.data.network.ApiClient

class OrderRepository {
    private val orderService = ApiClient.orderService

    suspend fun createOrder(token: String, request: CreateOrderRequest): OrderResponse {
        return orderService.createOrder("Bearer $token", request)
    }

    suspend fun getOrders(token: String): OrderResponse {
        return orderService.getOrders("Bearer $token")
    }
}
