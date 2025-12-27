package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.data.model.CreateOrderRequest
import com.example.myapp.data.model.OrderItemRequest
import com.example.myapp.data.model.ShippingAddress
import com.example.myapp.data.repository.OrderRepository
import com.example.myapp.util.CartManager
import com.example.myapp.util.TokenStore
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {
    private lateinit var orderRepository: OrderRepository
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        orderRepository = OrderRepository()
        progressBar = findViewById(R.id.progress_checkout)

        val etStreet = findViewById<EditText>(R.id.et_street)
        val etCity = findViewById<EditText>(R.id.et_city)
        val etState = findViewById<EditText>(R.id.et_state)
        val etZipCode = findViewById<EditText>(R.id.et_zipcode)
        val etCountry = findViewById<EditText>(R.id.et_country)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)

        btnConfirm.setOnClickListener {
            val street = etStreet.text.toString().trim()
            val city = etCity.text.toString().trim()
            val state = etState.text.toString().trim()
            val zipCode = etZipCode.text.toString().trim()
            val country = etCountry.text.toString().trim()

            // Validate inputs
            if (street.isEmpty() || city.isEmpty() || state.isEmpty() || 
                zipCode.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Please fill all shipping address fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            placeOrder(street, city, state, zipCode, country)
        }
    }

    private fun placeOrder(street: String, city: String, state: String, zipCode: String, country: String) {
        val token = TokenStore.getToken(this)
        if (token == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val cartItems = CartManager.getItems()
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Convert cart items to order items
        val orderItems = cartItems.map { cartItem ->
            OrderItemRequest(
                product = cartItem.productId,
                quantity = cartItem.quantity,
                size = cartItem.size,
                color = cartItem.color,
                price = cartItem.price
            )
        }

        val shippingAddress = ShippingAddress(
            street = street,
            city = city,
            state = state,
            zipCode = zipCode,
            country = country
        )

        val orderRequest = CreateOrderRequest(
            items = orderItems,
            shippingAddress = shippingAddress,
            paymentMethod = "credit_card" // Default payment method
        )

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = orderRepository.createOrder(token, orderRequest)
                progressBar.visibility = View.GONE

                if (response.success && response.data != null) {
                    Toast.makeText(
                        this@CheckoutActivity, 
                        "Order placed successfully! Order ID: ${response.data._id}", 
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Clear cart after successful order
                    CartManager.clearCart()
                    
                    // Navigate to home
                    val intent = Intent(this@CheckoutActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@CheckoutActivity, 
                        "Order failed: ${response.error ?: "Unknown error"}", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@CheckoutActivity, 
                    "Error placing order: ${e.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
