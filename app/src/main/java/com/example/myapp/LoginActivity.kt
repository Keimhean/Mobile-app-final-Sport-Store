package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.util.TokenStore
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

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

        val etEmail = findViewById<EditText>(R.id.et_email)
        val progress = findViewById<ProgressBar>(R.id.progress_login)
        val tvRegisterLink = findViewById<TextView>(R.id.tv_register_link)
        val repo = AuthRepository()

        // Sign in button (perform real login)
        findViewById<MaterialCardView>(R.id.btn_signin).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email & password required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val resp = repo.login(email, password)
                    if (resp.success && resp.data?.token != null) {
                        TokenStore.saveToken(this@LoginActivity, resp.data.token)
                        Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, resp.error ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                } finally {
                    progress.visibility = View.GONE
                }
            }
        }

        // Navigate to register
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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
