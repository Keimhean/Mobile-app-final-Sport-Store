package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etPassword = findViewById<EditText>(R.id.et_password)
        val ivPasswordToggle = findViewById<ImageView>(R.id.iv_password_toggle)

        // Back button
        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Password toggle
        ivPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // Sign in button
        findViewById<MaterialCardView>(R.id.btn_signin).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Google button
        findViewById<MaterialCardView>(R.id.btn_google).setOnClickListener {
            // TODO: Implement Google sign-in
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Facebook button
        findViewById<MaterialCardView>(R.id.btn_facebook).setOnClickListener {
            // TODO: Implement Facebook sign-in
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Continue as guest
        findViewById<MaterialCardView>(R.id.chip_continue_guest).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
