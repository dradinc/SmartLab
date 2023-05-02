package com.example.smartlabs.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlabs.R
import com.example.smartlabs.databinding.TypeCatalogItemBinding

class CatalogCategoryViewAdapter (private val categoryList: List<String>) : RecyclerView.Adapter<CatalogCategoryViewAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return  CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.type_catalog_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) = holder.bind(categoryList[position])

    override fun getItemCount() = categoryList.size

    inner class CategoryViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val binding = TypeCatalogItemBinding.bind(itemView)

        fun bind(category: String) = with(binding) {
            title.text = category
        }
    }
}