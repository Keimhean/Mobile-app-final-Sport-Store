package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.data.model.CartItem
import com.example.myapp.util.CartManager

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Initialize CartManager
        CartManager.init(this)

        // Get product data from intent
        val productId = intent.getStringExtra("product_id") ?: "sample_id"
        val productName = intent.getStringExtra("product_name") ?: "Sample Product"
        val productPrice = intent.getDoubleExtra("product_price", 0.0)
        val productImage = intent.getStringExtra("product_image") ?: ""

        // UI Elements
        val tvName = findViewById<TextView>(R.id.tv_product_name)
        val tvPrice = findViewById<TextView>(R.id.tv_product_price)
        val etQuantity = findViewById<EditText>(R.id.et_quantity)
        val spinnerSize = findViewById<Spinner>(R.id.spinner_size)
        val spinnerColor = findViewById<Spinner>(R.id.spinner_color)
        val btnAddCart = findViewById<Button>(R.id.btn_add_cart)
        val btnViewCart = findViewById<Button>(R.id.btn_view_cart)

        // Set product details
        tvName.text = productName
        tvPrice.text = "$$productPrice"

        // Add to cart button
        btnAddCart.setOnClickListener {
            val quantity = etQuantity.text.toString().toIntOrNull() ?: 1
            val size = if (spinnerSize.selectedItem != null) spinnerSize.selectedItem.toString() else null
            val color = if (spinnerColor.selectedItem != null) spinnerColor.selectedItem.toString() else null

            if (quantity > 0) {
                val cartItem = CartItem(
                    productId = productId,
                    productName = productName,
                    price = productPrice,
                    quantity = quantity,
                    size = size,
                    color = color,
                    imageUrl = productImage
                )
                CartManager.addItem(cartItem)
                Toast.makeText(this, "Added $quantity item(s) to cart", Toast.LENGTH_SHORT).show()
                etQuantity.setText("1")
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        // View cart button
        btnViewCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
