package com.example.myapp.data.model

data class Product(
    val _id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val brand: String,
    val imageUrl: String,
    val stock: Int,
    val sizes: List<String>? = null,
    val colors: List<String>? = null,
    val ratings: Ratings? = null,
    val featured: Boolean? = false
)

data class Ratings(
    val average: Double = 0.0,
    val count: Int = 0
)

data class CartItem(
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val size: String? = null,
    val color: String? = null,
    val imageUrl: String = ""
)

data class Cart(
    val items: MutableList<CartItem> = mutableListOf()
) {
    fun getTotalPrice(): Double = items.sumOf { it.price * it.quantity }
    fun getTotalItems(): Int = items.sumOf { it.quantity }
}

data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    val shippingAddress: ShippingAddress,
    val paymentMethod: String
)

data class OrderItemRequest(
    val product: String,
    val quantity: Int,
    val size: String? = null,
    val color: String? = null,
    val price: Double
)

data class ShippingAddress(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)

data class OrderResponse(
    val success: Boolean,
    val data: OrderData? = null,
    val error: String? = null
)

data class OrderData(
    val _id: String,
    val user: String,
    val items: List<Map<String, Any>>,
    val totalAmount: Double,
    val status: String,
    val createdAt: String
)
