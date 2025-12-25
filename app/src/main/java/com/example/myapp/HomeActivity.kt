package com.example.myapp

import android.content.Intent
import android.os.Bundle
import  android.widget.TextView
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.CategoryAdapter.CategoryItem
import com.example.myapp.ProductAdapter.ProductItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Setup Top App Bar
        val toolbar = findViewById<MaterialToolbar>(R.id.top_app_bar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    // TODO: Implement search functionality
                    true
                }
                R.id.action_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_categories -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    false
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    false
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }

        // Categories grid (3 columns)
        val rvCat = findViewById<RecyclerView>(R.id.rv_categories)
        rvCat.layoutManager = GridLayoutManager(this, 2)
        val cats = listOf(
            CategoryItem("🏃", "Running", R.drawable.img_category_running),
            CategoryItem("⚽", "Football", R.drawable.img_category_football),
            CategoryItem("🏀", "Basketball", R.drawable.img_category_basketball),
            CategoryItem("🎾", "Tennis", R.drawable.img_category_tennis),
//            CategoryItem("💪", "Gym", R.drawable.img_category_running),
//            CategoryItem("🏊", "Swimming", R.drawable.img_category_tennis)
        )
        rvCat.adapter = CategoryAdapter(cats)

        // Products horizontal - New Arrivals
        val rvProd = findViewById<RecyclerView>(R.id.rv_products)
        rvProd.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val prods = listOf(
            ProductAdapter.ProductItem(R.drawable.img_product_nike_pegasus, "Nike", "Air Zoom Pegasus", 129.99, 159.99, 4.8, 342, isSale = true, isNew = true),
            ProductAdapter.ProductItem(R.drawable.img_product_nike_jersey, "Nike", "Basketball Elite Jersey", 79.99, null, 4.7, 89, isSale = false, isNew = true),
            ProductAdapter.ProductItem(R.drawable.img_product_soccer_cleats, "Adidas", "Soccer Cleats Pro", 149.99, 179.99, 4.6, 156, isSale = true, isNew = false),
            ProductAdapter.ProductItem(R.drawable.img_product_default, "Puma", "Training Shorts", 39.99, null, 4.5, 51, isSale = false, isNew = false)
        )
        rvProd.adapter = ProductAdapter(prods) { item ->
            // open detail (placeholder)
            val i = Intent(this, ProductDetailActivity::class.java)
            startActivity(i)
        }

        // Best Sellers grid (2 columns)
        val rvBestSellers = findViewById<RecyclerView>(R.id.rv_best_sellers)
        rvBestSellers.layoutManager = GridLayoutManager(this, 2)
        val bestSellers = listOf(
            ProductAdapter.ProductItem(R.drawable.img_product_nike_pegasus, "Nike", "Air Zoom Pegasus", 129.99, 159.99, 4.8, 342, isSale = true, isNew = true),
            ProductAdapter.ProductItem(R.drawable.img_product_default, "Puma", "Pro Training Shoes", 89.99, null, 4.5, 128, isSale = false, isNew = false),
            ProductAdapter.ProductItem(R.drawable.img_product_default, "Lululemon", "Yoga Mat Premium", 68.0, null, 4.6, 234, isSale = false, isNew = false),
            ProductAdapter.ProductItem(R.drawable.img_product_soccer_cleats, "Adidas", "Soccer Cleats Elite", 149.99, 179.99, 4.7, 267, isSale = true, isNew = false),
            ProductAdapter.ProductItem(R.drawable.img_product_nike_jersey, "Nike", "Basketball Jersey", 79.99, null, 4.7, 189, isSale = false, isNew = true),
            ProductAdapter.ProductItem(R.drawable.img_product_default, "Under Armour", "Compression Shirt", 49.99, 59.99, 4.4, 95, isSale = true, isNew = false)
        )
        rvBestSellers.adapter = ProductAdapter(bestSellers) { item ->
            val i = Intent(this, ProductDetailActivity::class.java)
            startActivity(i)
        }
//
        findViewById<TextView>(R.id.tv_see_all).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
//            finish()
        }
        // Banner shop button
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_banner_shop).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
//        findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_help).setOnClickListener {
//            startActivity(Intent(this, H::class.java))
//        }
    }
}
