package com.example.myapp.data.network

import com.example.myapp.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProducts(): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductResponse
}

data class ProductListResponse(
    val success: Boolean,
    val data: List<Product>,
    val count: Int,
    val total: Int
)

data class ProductResponse(
    val success: Boolean,
    val data: Product?
)
