package com.example.myapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    // Handle Home navigation
                    true
                }
                R.id.nav_categories -> {
                    // Handle Categories navigation
                    true
                }
                R.id.nav_cart -> {
                    // Handle Cart navigation
                    true
                }
                R.id.nav_profile -> {
                    // Handle Profile navigation
                    true
                }
                R.id.nav_search -> {
                    // Handle Search navigation
                    true
                }
                else -> false
            }
        }
    }
}
