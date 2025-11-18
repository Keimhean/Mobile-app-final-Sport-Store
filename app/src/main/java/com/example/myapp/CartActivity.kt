package com.example.myapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        // sample: update empty cart text if there were items
        val empty = findViewById<TextView>(R.id.tv_cart_empty)
        // keep empty message for now; in future, wire real cart items
        empty.text = getString(R.string.no_items)
    }
}
