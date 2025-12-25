package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

data class OnboardingPage(
    val icon: Int,
    val title: String,
    val description: String,
    val buttonText: String
)

class OnboardingPagerAdapter(private val pages: List<OnboardingPage>) :
    RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder>() {

    var onButtonClick: ((Int) -> Unit)? = null
    var onPageChanged: ((Int) -> Unit)? = null

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_icon)
        val title: TextView = view.findViewById(R.id.tv_title)
        val description: TextView = view.findViewById(R.id.tv_description)
        val button: MaterialButton = view.findViewById(R.id.btn_action)
        val indicator0: View = view.findViewById(R.id.indicator_0)
        val indicator1: View = view.findViewById(R.id.indicator_1)
        val indicator2: View = view.findViewById(R.id.indicator_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val page = pages[position]
        holder.icon.setImageResource(page.icon)
        holder.title.text = page.title
        holder.description.text = page.description
        holder.button.text = page.buttonText

        // Update indicators
        updateIndicator(holder.indicator0, position == 0)
        updateIndicator(holder.indicator1, position == 1)
        updateIndicator(holder.indicator2, position == 2)

        holder.button.setOnClickListener {
            onButtonClick?.invoke(position)
        }
    }

    private fun updateIndicator(view: View, isActive: Boolean) {
        val params = view.layoutParams
        params.width = if (isActive) {
            view.context.resources.displayMetrics.density.toInt() * 32
        } else {
            view.context.resources.displayMetrics.density.toInt() * 8
        }
        view.layoutParams = params
        view.setBackgroundResource(
            if (isActive) R.drawable.bg_indicator_active
            else R.drawable.bg_indicator_inactive
        )
    }

    override fun getItemCount() = pages.size
}

class OnboardingActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_new)

        val pages = listOf(
            OnboardingPage(
                R.drawable.ic_cart,
                "Shop Sports Gear Easily",
                "Browse thousands of premium\nsports products from top brands",
                "Next"
            ),
            OnboardingPage(
                R.drawable.ic_flash,
                "Fast & Secure Checkout",
                "Quick checkout process with\nmultiple payment options",
                "Next"
            ),
            OnboardingPage(
                R.drawable.ic_shield,
                "Track Your Orders",
                "Real-time order tracking from\nwarehouse to your doorstep",
                "Get Started"
            )
        )

        viewPager = findViewById(R.id.viewpager_onboarding)
        adapter = OnboardingPagerAdapter(pages)
        viewPager.adapter = adapter

        adapter.onButtonClick = { position ->
            if (position < pages.size - 1) {
                viewPager.currentItem = position + 1
            } else {
                // Last page - show auth options or navigate
                // For now, just go to home
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        val btnSignUp = findViewById<MaterialButton>(R.id.btn_signup)
        val btnLogin = findViewById<MaterialButton>(R.id.btn_login)
        val tvGuest = findViewById<TextView>(R.id.tv_guest)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        tvGuest.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
