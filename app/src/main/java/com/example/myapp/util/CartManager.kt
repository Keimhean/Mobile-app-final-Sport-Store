package com.example.myapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.myapp.data.model.Cart
import com.example.myapp.data.model.CartItem
import com.google.gson.Gson

object CartManager {
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private var cart: Cart? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        loadCart()
    }

    fun addItem(item: CartItem) {
        if (cart == null) {
            cart = Cart()
        }
        val existingItem = cart!!.items.find { it.productId == item.productId && it.size == item.size && it.color == item.color }
        if (existingItem != null) {
            // Update quantity if item already exists
            cart!!.items.remove(existingItem)
            cart!!.items.add(existingItem.copy(quantity = existingItem.quantity + item.quantity))
        } else {
            cart!!.items.add(item)
        }
        saveCart()
    }

    fun removeItem(productId: String, size: String? = null, color: String? = null) {
        if (cart == null) return
        cart!!.items.removeAll { it.productId == productId && it.size == size && it.color == color }
        saveCart()
    }

    fun updateQuantity(productId: String, quantity: Int, size: String? = null, color: String? = null) {
        if (cart == null) return
        val item = cart!!.items.find { it.productId == productId && it.size == size && it.color == color }
        if (item != null) {
            if (quantity > 0) {
                cart!!.items.remove(item)
                cart!!.items.add(item.copy(quantity = quantity))
            } else {
                removeItem(productId, size, color)
            }
            saveCart()
        }
    }

    fun getCart(): Cart {
        return cart ?: Cart()
    }

    fun getItems(): List<CartItem> {
        return cart?.items ?: emptyList()
    }

    fun getTotalPrice(): Double {
        return cart?.getTotalPrice() ?: 0.0
    }

    fun getTotalItems(): Int {
        return cart?.getTotalItems() ?: 0
    }

    fun clearCart() {
        cart = Cart()
        saveCart()
    }

    private fun saveCart() {
        if (cart != null) {
            val json = gson.toJson(cart)
            sharedPreferences.edit().putString("cart", json).apply()
        }
    }

    private fun loadCart() {
        val json = sharedPreferences.getString("cart", null)
        cart = if (json != null) {
            try {
                gson.fromJson(json, Cart::class.java)
            } catch (e: Exception) {
                Cart()
            }
        } else {
            Cart()
        }
    }
}
