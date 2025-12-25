package com.example.myapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.util.TokenStore
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val progress = findViewById<ProgressBar>(R.id.progress_register)
        val repo = AuthRepository()

        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<MaterialCardView>(R.id.btn_register).setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val resp = repo.register(name, email, password, phone)
                    if (resp.success && resp.data?.token != null) {
                        TokenStore.saveToken(this@RegisterActivity, resp.data.token)
                        Toast.makeText(this@RegisterActivity, "Registered successfully", Toast.LENGTH_SHORT).show()
                        finish() // go back to login, token stored
                    } else {
                        Toast.makeText(this@RegisterActivity, resp.error ?: "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                } finally {
                    progress.visibility = View.GONE
                }
            }
        }
    }
}
