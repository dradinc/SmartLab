package com.example.smartlabs.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlabs.Model.CatalogItem
import com.example.smartlabs.R
import com.example.smartlabs.databinding.CatalogTovarItemBinding

class CatalogViewAdapter (private val catalogList: List<CatalogItem>) : RecyclerView.Adapter<CatalogViewAdapter.CatalogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.catalog_tovar_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) = holder.bind(catalogList[position])

    override fun getItemCount() = catalogList.size

    inner class CatalogViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val binding = CatalogTovarItemBinding.bind(itemView)

        fun bind(catalogItem: CatalogItem) = with(binding) {
            title.text = catalogItem.name
            description.text = catalogItem.time_result
            price.text = catalogItem.price

        }
    }
}