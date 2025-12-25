package com.example.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val categories = listOf(
            CategoryAdapter.CategoryItem("🏃", "Running & Fitness", R.drawable.img_category_running),
            CategoryAdapter.CategoryItem("⚽", "Football / Soccer", R.drawable.img_category_football),
            CategoryAdapter.CategoryItem("🏀", "Basketball", R.drawable.img_category_basketball),
            CategoryAdapter.CategoryItem("🎾", "Tennis & Racket Sports", R.drawable.img_category_tennis),
            CategoryAdapter.CategoryItem("💪", "Gym & Training", R.drawable.img_category_gym),
//            CategoryAdapter.CategoryItem("🏊", "Swimming & Water Sports", R.drawable.img_category_swimming),
            CategoryAdapter.CategoryItem("🚴", "Cycling", R.drawable.img_category_cycling),
//            CategoryAdapter.CategoryItem("🏏", "Cricket / Baseball", R.drawable.img_category_cricket),
//            CategoryAdapter.CategoryItem("⛳", "Golf", R.drawable.img_category_golf)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rv_categories)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoryAdapter(categories)
        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
