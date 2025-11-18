package com.example.myapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        // sample data
        val name = findViewById<TextView>(R.id.tv_product_name)
        val price = findViewById<TextView>(R.id.tv_product_price)
        name.text = getString(R.string.product_name)
        price.text = getString(R.string.product_price)
        findViewById<Button>(R.id.btn_add_cart).setOnClickListener {
            // simple feedback placeholder
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        }
    }
}
