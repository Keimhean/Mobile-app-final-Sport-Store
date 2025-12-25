package com.example.myapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_profile)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Setup user info
        findViewById<TextView>(R.id.tv_username).text = "John Doe"
        findViewById<TextView>(R.id.tv_email).text = "john.doe@email.com"
        findViewById<TextView>(R.id.tv_avatar).text = "JD"
        
        // Setup stats
        findViewById<TextView>(R.id.tv_completed_count).text = "1"
        findViewById<TextView>(R.id.tv_active_count).text = "1"
        findViewById<TextView>(R.id.tv_wishlist_count).text = "12"
    }
}
