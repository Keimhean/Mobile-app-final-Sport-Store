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
            CategoryAdapter.CategoryItem("Running & Fitness", R.drawable.category_running, "Running"),
            CategoryAdapter.CategoryItem("Football / Soccer", R.drawable.category_football, "Football"),
            CategoryAdapter.CategoryItem("Basketball", R.drawable.category_basketball, "Basketball"),
            CategoryAdapter.CategoryItem("Tennis & Racket Sports", R.drawable.category_tennis, "Tennis"),
            CategoryAdapter.CategoryItem("Gym & Training", R.drawable.category_gym, "Gym"),
    //            CategoryAdapter.CategoryItem("Swimming & Water Sports", R.drawable.img_category_swimming, "Swimming"),
            CategoryAdapter.CategoryItem("Cycling", R.drawable.category_cycling, "Cycling"),
    //            CategoryAdapter.CategoryItem("Cricket / Baseball", R.drawable.img_category_cricket, "Cricket"),
    //            CategoryAdapter.CategoryItem("Golf", R.drawable.img_category_golf, "Golf")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rv_categories)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoryAdapter(categories)
    }
}
