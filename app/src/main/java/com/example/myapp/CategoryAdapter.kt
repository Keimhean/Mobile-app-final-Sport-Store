package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val items: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    data class CategoryItem(val emoji: String, val label: String, val imageRes: Int)

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val emojiView: TextView = view.findViewById(R.id.tv_emoji)
        val labelView: TextView = view.findViewById(R.id.tv_label)
        val imageView: ImageView = view.findViewById(R.id.iv_category_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.emojiView.text = it.emoji
        holder.labelView.text = it.label
        holder.imageView.setImageResource(it.imageRes)
    }

    override fun getItemCount(): Int = items.size
}
