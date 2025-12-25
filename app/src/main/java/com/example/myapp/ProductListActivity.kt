package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.data.network.ApiClient
import com.example.myapp.data.network.ProductService
import com.example.myapp.util.CartManager
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tvTitle: TextView
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        CartManager.init(this)

        category = intent.getStringExtra("category")
        
        recyclerView = findViewById(R.id.rv_products)
        progressBar = findViewById(R.id.progress_products)
        tvEmpty = findViewById(R.id.tv_empty_products)
        tvTitle = findViewById(R.id.tv_category_title)

        tvTitle.text = category ?: "All Products"

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        findViewById<MaterialCardView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        loadProducts()
    }

    private fun loadProducts() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val service = ApiClient.retrofit.create(ProductService::class.java)
                val response = service.getProducts()
                
                if (response.success) {
                    val filteredProducts = if (category != null) {
                        response.data.filter { it.category.equals(category, ignoreCase = true) }
                    } else {
                        response.data
                    }

                    if (filteredProducts.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmpty.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        
                        recyclerView.adapter = ProductAdapter.forProducts(filteredProducts) { product ->
                            val intent = Intent(this@ProductListActivity, ProductDetailActivity::class.java)
                            intent.putExtra("product_id", product._id)
                            intent.putExtra("product_name", product.name)
                            intent.putExtra("product_price", product.price)
                            intent.putExtra("product_image", product.imageUrl)
                            intent.putExtra("product_description", product.description)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this@ProductListActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProductListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = "Error loading products: ${e.message}"
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
