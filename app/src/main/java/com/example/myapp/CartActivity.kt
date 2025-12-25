package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.util.CartManager
import com.example.myapp.util.TokenStore
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        CartManager.init(this)
        refreshCart()

        findViewById<Button>(R.id.btn_checkout).setOnClickListener {
            checkout()
        }

        findViewById<Button>(R.id.btn_continue_shopping).setOnClickListener {
            finish()
        }
    }

    private fun refreshCart() {
        val cartContainer = findViewById<LinearLayout>(R.id.cart_items_container)
        val tvEmpty = findViewById<TextView>(R.id.tv_cart_empty)
        val tvTotal = findViewById<TextView>(R.id.tv_total_price)
        val btnCheckout = findViewById<Button>(R.id.btn_checkout)

        val items = CartManager.getItems()

        if (items.isEmpty()) {
            tvEmpty.visibility = TextView.VISIBLE
            cartContainer.visibility = LinearLayout.GONE
            tvTotal.visibility = TextView.GONE
            btnCheckout.isEnabled = false
        } else {
            tvEmpty.visibility = TextView.GONE
            cartContainer.visibility = LinearLayout.VISIBLE
            tvTotal.visibility = TextView.VISIBLE
            btnCheckout.isEnabled = true

            cartContainer.removeAllViews()
            for (item in items) {
                val itemView = layoutInflater.inflate(R.layout.item_cart, cartContainer, false)
                val tvName = itemView.findViewById<TextView>(R.id.tv_item_name)
                val tvPrice = itemView.findViewById<TextView>(R.id.tv_item_price)
                val tvQuantity = itemView.findViewById<TextView>(R.id.tv_item_quantity)
                val btnRemove = itemView.findViewById<Button>(R.id.btn_remove_item)

                tvName.text = item.productName
                tvPrice.text = "$${item.price * item.quantity}"
                tvQuantity.text = "Qty: ${item.quantity}"

                btnRemove.setOnClickListener {
                    CartManager.removeItem(item.productId, item.size, item.color)
                    refreshCart()
                }

                cartContainer.addView(itemView)
            }

            tvTotal.text = "Total: $${String.format("%.2f", CartManager.getTotalPrice())}"
        }
    }

    private fun checkout() {
        val token = TokenStore.getToken(this)
        if (token == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        val cartItems = CartManager.getItems()
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
