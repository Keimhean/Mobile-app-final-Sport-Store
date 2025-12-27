package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var tvAvatar: TextView
    private lateinit var ivCameraIcon: ImageView
    private lateinit var ivSettingsProfilePhoto: ImageView
    private lateinit var tvSettingsAvatar: TextView
    private lateinit var sharedPreferences: SharedPreferences
    
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        sharedPreferences = getSharedPreferences("profile_prefs", MODE_PRIVATE)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_profile)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Initialize views
        ivProfilePhoto = findViewById(R.id.iv_profile_photo)
        tvAvatar = findViewById(R.id.tv_avatar)
        ivCameraIcon = findViewById(R.id.iv_camera_icon)
        ivSettingsProfilePhoto = findViewById(R.id.iv_settings_profile_photo)
        tvSettingsAvatar = findViewById(R.id.tv_settings_avatar)
        
        // Setup image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    loadImageFromUri(uri)
                }
            }
        }
        
        // Setup camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    displayProfilePhoto(it)
                    saveProfilePhoto(it)
                }
            }
        }
        
        // Setup user info
        findViewById<TextView>(R.id.tv_username).text = "John Doe"
        findViewById<TextView>(R.id.tv_email).text = "john.doe@email.com"
        findViewById<TextView>(R.id.tv_settings_username).text = "John Doe"
        findViewById<TextView>(R.id.tv_settings_email).text = "john.doe@email.com"
        
        // Setup stats
        findViewById<TextView>(R.id.tv_completed_count).text = "1"
        findViewById<TextView>(R.id.tv_active_count).text = "1"
        findViewById<TextView>(R.id.tv_wishlist_count).text = "12"
        
        // Load saved profile photo
        loadSavedProfilePhoto()
        
        // Setup camera icon click
        ivCameraIcon.setOnClickListener {
            showImagePickerDialog()
        }
        
        // Setup profile photo click
        ivProfilePhoto.setOnClickListener {
            showImagePickerDialog()
        }
    }
    
    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Remove Photo")
        AlertDialog.Builder(this)
            .setTitle("Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                    2 -> removeProfilePhoto()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }
    
    private fun loadImageFromUri(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            displayProfilePhoto(bitmap)
            saveProfilePhoto(bitmap)
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun displayProfilePhoto(bitmap: Bitmap) {
        // Update main profile section
        ivProfilePhoto.setImageBitmap(bitmap)
        ivProfilePhoto.visibility = View.VISIBLE
        tvAvatar.visibility = View.GONE
        
        // Update settings section
        ivSettingsProfilePhoto.setImageBitmap(bitmap)
        ivSettingsProfilePhoto.visibility = View.VISIBLE
        tvSettingsAvatar.visibility = View.GONE
    }
    
    private fun saveProfilePhoto(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        
        sharedPreferences.edit()
            .putString("profile_photo", encodedImage)
            .apply()
    }
    
    private fun loadSavedProfilePhoto() {
        val encodedImage = sharedPreferences.getString("profile_photo", null)
        if (encodedImage != null) {
            try {
                val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                displayProfilePhoto(bitmap)
            } catch (e: Exception) {
                // If decoding fails, show initials
                showInitials()
            }
        } else {
            showInitials()
        }
    }
    
    private fun removeProfilePhoto() {
        sharedPreferences.edit()
            .remove("profile_photo")
            .apply()
        showInitials()
        Toast.makeText(this, "Profile photo removed", Toast.LENGTH_SHORT).show()
    }
    
    private fun showInitials() {
        // Update main profile section
        ivProfilePhoto.visibility = View.GONE
        tvAvatar.visibility = View.VISIBLE
        tvAvatar.text = "JD"
        
        // Update settings section
        ivSettingsProfilePhoto.visibility = View.GONE
        tvSettingsAvatar.visibility = View.VISIBLE
        tvSettingsAvatar.text = "JD"
    }
}
