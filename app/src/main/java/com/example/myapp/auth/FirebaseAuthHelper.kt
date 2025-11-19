package com.example.myapp.auth

import android.app.Activity
import android.content.Intent
import com.example.myapp.data.repository.AuthRepository
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseAuthHelper(private val activity: Activity) {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val authRepository = AuthRepository()
    
    companion object {
        const val RC_GOOGLE_SIGN_IN = 9001
    }
    
    init {
        configureGoogleSignIn()
    }
    
    private fun configureGoogleSignIn() {
        // TODO: Replace with your actual Web Client ID from Firebase Console
        // Get it from google-services.json after enabling Google Sign-In
        // Look for "client_type": 3 in oauth_client array
        val webClientId = "411738770294-YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
        
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }
    
    // Google Sign-In
    fun startGoogleSignIn(): Intent {
        return googleSignInClient.signInIntent
    }
    
    fun handleGoogleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account, onSuccess, onError)
        } catch (e: ApiException) {
            onError("Google sign-in failed: ${e.message}")
        }
    }
    
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        signInWithCredential(credential, onSuccess, onError)
    }
    
    // Facebook Sign-In
    fun startFacebookSignIn(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
        
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken.token, onSuccess, onError)
            }
            
            override fun onCancel() {
                onError("Facebook sign-in cancelled")
            }
            
            override fun onError(error: FacebookException) {
                onError("Facebook sign-in error: ${error.message}")
            }
        })
    }
    
    private fun handleFacebookAccessToken(token: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val credential = FacebookAuthProvider.getCredential(token)
        signInWithCredential(credential, onSuccess, onError)
    }
    
    // Common sign-in with credential
    private fun signInWithCredential(credential: AuthCredential, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Get user info from Firebase
                        val email = user.email ?: ""
                        val name = user.displayName ?: "User"
                        val firebaseUid = user.uid
                        val provider = when {
                            user.providerData.any { it.providerId == "google.com" } -> "google"
                            user.providerData.any { it.providerId == "facebook.com" } -> "facebook"
                            else -> "email"
                        }
                        
                        // Exchange Firebase auth for backend token
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val response = withContext(Dispatchers.IO) {
                                    authRepository.socialLogin(email, name, provider, firebaseUid)
                                }
                                if (response.success && response.data?.token != null) {
                                    onSuccess(response.data.token)
                                } else {
                                    onError(response.error ?: "Backend authentication failed")
                                }
                            } catch (e: Exception) {
                                onError("Backend error: ${e.message}")
                            }
                        }
                    } else {
                        onError("Failed to get Firebase user")
                    }
                } else {
                    onError("Authentication failed: ${task.exception?.message}")
                }
            }
    }
    
    // Handle activity result
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            handleGoogleSignInResult(data, onSuccess, onError)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    // Get current user
    fun getCurrentUser() = auth.currentUser
    
    // Sign out
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
        LoginManager.getInstance().logOut()
    }
}
