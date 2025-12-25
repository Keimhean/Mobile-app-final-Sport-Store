//package com.example.myapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.InputType
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.myapp.auth.FirebaseAuthHelper
//import com.example.myapp.data.repository.AuthRepository
//import com.example.myapp.util.TokenStore
//import com.google.android.material.card.MaterialCardView
//import kotlinx.coroutines.launch
//
//class LoginActivity : AppCompatActivity() {
//    private var isPasswordVisible = false
//    private var firebaseAuthHelper: FirebaseAuthHelper? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        // Initialize Firebase helper with try-catch to handle missing configuration
//        try {
//            firebaseAuthHelper = FirebaseAuthHelper(this)
//        } catch (e: Exception) {
//            // Firebase not fully configured, social login will be disabled
//            android.util.Log.w("LoginActivity", "Firebase not configured: ${e.message}")
//        }
//        val ivPasswordToggle = findViewById<ImageView>(R.id.iv_password_toggle)
//        val etEmail = findViewById<EditText>(R.id.et_email)
//        val etPassword = findViewById<EditText>(R.id.et_password)
//        val progress = findViewById<ProgressBar>(R.id.progress_login)
//        val tvRegisterLink = findViewById<TextView>(R.id.tv_register_link)
//        val repo = AuthRepository()
//
//        // Back button
//        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener {
//            finish()
//        }
//
//        // Password toggle
//        ivPasswordToggle.setOnClickListener {
//            isPasswordVisible = !isPasswordVisible
//            if (isPasswordVisible) {
//                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//            } else {
//                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//            }
//            etPassword.setSelection(etPassword.text.length)
//        }
//
//        // Sign in button (perform real login)
//        findViewById<MaterialCardView>(R.id.btn_signin).setOnClickListener {
//            val email = etEmail.text.toString().trim()
//            val password = etPassword.text.toString().trim()
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Email & password required", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            progress.visibility = View.VISIBLE
//            lifecycleScope.launch {
//                try {
//                    val resp = repo.login(email, password)
//                    if (resp.success && resp.data?.token != null) {
//                        TokenStore.saveToken(this@LoginActivity, resp.data.token)
//                        Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//                        finish()
//                    } else {
//                        Toast.makeText(this@LoginActivity, resp.error ?: "Login failed", Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@LoginActivity, e.message ?: "Error", Toast.LENGTH_SHORT).show()
//                } finally {
//                    progress.visibility = View.GONE
//                }
//            }
//        }
//
//        // Google button
//        findViewById<MaterialCardView>(R.id.btn_google).setOnClickListener {
//            if (firebaseAuthHelper == null) {
//                Toast.makeText(this, "Firebase not configured. Please use email login.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            progress.visibility = View.VISIBLE
//            val signInIntent = firebaseAuthHelper?.startGoogleSignIn()
//            signInIntent?.let { startActivityForResult(it, FirebaseAuthHelper.RC_GOOGLE_SIGN_IN) }
//        }
//
//        // Facebook button
//        findViewById<MaterialCardView>(R.id.btn_facebook).setOnClickListener {
//            if (firebaseAuthHelper == null) {
//                Toast.makeText(this, "Firebase not configured. Please use email login.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            progress.visibility = View.VISIBLE
//            firebaseAuthHelper?.startFacebookSignIn(
//                onSuccess = { token ->
//                    TokenStore.saveToken(this, token)
//                    Toast.makeText(this, "Facebook login success", Toast.LENGTH_SHORT).show()
//                    progress.visibility = View.GONE
//                    startActivity(Intent(this, HomeActivity::class.java))
//                    finish()
//                },
//                onError = { error ->
//                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//                    progress.visibility = View.GONE
//                }
//            )
//        }
//
//        // Continue as guest
//        findViewById<MaterialCardView>(R.id.chip_continue_guest).setOnClickListener {
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }
//
//        // Navigate to register
//        tvRegisterLink.setOnClickListener {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (firebaseAuthHelper == null) return
//
//        val progress = findViewById<ProgressBar>(R.id.progress_login)
//
//        firebaseAuthHelper?.onActivityResult(requestCode, resultCode, data,
//            onSuccess = { token ->
//                TokenStore.saveToken(this, token)
//                Toast.makeText(this, "Google login success", Toast.LENGTH_SHORT).show()
//                progress.visibility = View.GONE
//                startActivity(Intent(this, HomeActivity::class.java))
//                finish()
//            },
//            onError = { error ->
//                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//                progress.visibility = View.GONE
//            }
//        )
//    }
//}
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
import com.example.myapp.auth.FirebaseAuthHelper
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.util.TokenStore
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    private var firebaseAuthHelper: FirebaseAuthHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase helper with try-catch to handle missing configuration
        try {
            firebaseAuthHelper = FirebaseAuthHelper(this)
        } catch (e: Exception) {
            // Firebase not fully configured, social login will be disabled
            android.util.Log.w("LoginActivity", "Firebase not configured: ${e.message}")
        }
        val ivPasswordToggle = findViewById<ImageView>(R.id.iv_password_toggle)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val progress = findViewById<ProgressBar>(R.id.progress_login)
        val tvRegisterLink = findViewById<TextView>(R.id.tv_register_link)
        val repo = AuthRepository()

        // Back button
        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, OnboardingActivity::class.java))
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

        // Google button
        findViewById<MaterialCardView>(R.id.btn_google).setOnClickListener {
            if (firebaseAuthHelper == null) {
                Toast.makeText(this, "Firebase not configured. Please use email login.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            val signInIntent = firebaseAuthHelper?.startGoogleSignIn()
            signInIntent?.let { startActivityForResult(it, FirebaseAuthHelper.RC_GOOGLE_SIGN_IN) }
        }

        // Facebook button
        findViewById<MaterialCardView>(R.id.btn_facebook).setOnClickListener {
            if (firebaseAuthHelper == null) {
                Toast.makeText(this, "Firebase not configured. Please use email login.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            firebaseAuthHelper?.startFacebookSignIn(
                onSuccess = { token ->
                    TokenStore.saveToken(this, token)
                    Toast.makeText(this, "Facebook login success", Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                }
            )
        }

        // Continue as guest
        findViewById<MaterialCardView>(R.id.chip_continue_guest).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Navigate to register
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (firebaseAuthHelper == null) return

        val progress = findViewById<ProgressBar>(R.id.progress_login)

        firebaseAuthHelper?.onActivityResult(requestCode, resultCode, data,
            onSuccess = { token ->
                TokenStore.saveToken(this, token)
                Toast.makeText(this, "Google login success", Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            },
            onError = { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }
        )
    }
}
