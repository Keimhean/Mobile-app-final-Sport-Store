package com.example.myapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.util.TokenStore
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirm: EditText
    private lateinit var progress: ProgressBar
    private lateinit var repo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPhone = findViewById(R.id.et_phone)
        etPassword = findViewById(R.id.et_password)
        etConfirm = findViewById(R.id.et_confirm_password)
        progress = findViewById(R.id.progress_register)
        repo = AuthRepository()

        // register button (now a MaterialButton in layout)
        findViewById<MaterialButton>(R.id.btn_register).setOnClickListener {
            performRegister()
        }

        // 'Already have an account? Login' -> go to LoginActivity
        val tvAlready = findViewById<TextView>(R.id.tv_already)
        val text = "Already have an account? Login"
        val spannableString = SpannableString(text)
        
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#C86A3F")
                ds.isUnderlineText = false
            }
        }
        
        val loginStart = text.indexOf("Login")
        spannableString.setSpan(clickableSpan, loginStart, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAlready.text = spannableString
        tvAlready.movementMethod = LinkMovementMethod.getInstance()
    }

    // onClick fallback for XML attribute
    fun onRegisterClicked(view: View) {
        try {
            performRegister()
        } catch (e: Exception) {
            android.util.Log.e("RegisterActivity", "onRegisterClicked error", e)
            Toast.makeText(this, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performRegister() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirm = etConfirm.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirm) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val resp = repo.register(name, email, password, phone)
                if (resp.success && resp.data?.token != null) {
                    TokenStore.saveToken(this@RegisterActivity, resp.data.token)
                    Toast.makeText(this@RegisterActivity, "Registered successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, resp.error ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                android.util.Log.e("RegisterActivity", "register coroutine error", e)
                Toast.makeText(this@RegisterActivity, e.message ?: "Error", Toast.LENGTH_SHORT).show()
            } finally {
                progress.visibility = View.GONE
            }
        }
    }
}
