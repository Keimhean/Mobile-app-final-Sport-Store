package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.data.model.Product

class ProductAdapter private constructor(
    private val items: List<Any>,
    private val clickListener: (Any) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    companion object {
        fun forProductItems(items: List<ProductItem>, click: (ProductItem) -> Unit): ProductAdapter {
            return ProductAdapter(items) { click(it as ProductItem) }
        }

        fun forProducts(products: List<Product>, onClick: (Product) -> Unit): ProductAdapter {
            return ProductAdapter(products) { onClick(it as Product) }
        }
    }

    data class ProductItem(
        val imageRes: Int,
        val brand: String,
        val title: String,
        val price: Double,
        val oldPrice: Double? = null,
        val rating: Double,
        val reviews: Int,
        val isSale: Boolean = false,
        val isNew: Boolean = false
    )

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.findViewById(R.id.iv_product)
        val brand: TextView = view.findViewById(R.id.tv_brand)
        val title: TextView = view.findViewById(R.id.tv_title)
        val price: TextView = view.findViewById(R.id.tv_price)
        val oldPrice: TextView = view.findViewById(R.id.tv_old_price)
        val rating: TextView = view.findViewById(R.id.tv_rating)
        val reviews: TextView = view.findViewById(R.id.tv_reviews)
        val badgeSale: TextView = view.findViewById(R.id.badge_sale)
        val badgeNew: TextView = view.findViewById(R.id.badge_new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        
        when (item) {
            is ProductItem -> {
                holder.iv.setImageResource(item.imageRes)
                holder.brand.text = item.brand
                holder.title.text = item.title
                holder.price.text = String.format("$%.2f", item.price)
                if (item.oldPrice != null) {
                    holder.oldPrice.visibility = View.VISIBLE
                    holder.oldPrice.text = String.format("$%.2f", item.oldPrice)
                    holder.oldPrice.paintFlags = holder.oldPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    holder.oldPrice.visibility = View.GONE
                }
                holder.rating.text = String.format("%.1f", item.rating)
                holder.reviews.text = "(${item.reviews})"
                holder.badgeSale.visibility = if (item.isSale) View.VISIBLE else View.GONE
                holder.badgeNew.visibility = if (item.isNew) View.VISIBLE else View.GONE
            }
            is Product -> {
                holder.iv.setImageResource(android.R.drawable.ic_menu_gallery)
                holder.brand.text = item.brand
                holder.title.text = item.name
                holder.price.text = String.format("$%.2f", item.price)
                holder.oldPrice.visibility = View.GONE
                holder.rating.text = String.format("%.1f", item.ratings?.average ?: 0.0)
                holder.reviews.text = "(${item.ratings?.count ?: 0})"
                holder.badgeSale.visibility = View.GONE
                holder.badgeNew.visibility = if (item.featured == true) View.VISIBLE else View.GONE
            }
        }
        
        holder.itemView.setOnClickListener { clickListener.invoke(item) }
    }

    override fun getItemCount(): Int = items.size
}
