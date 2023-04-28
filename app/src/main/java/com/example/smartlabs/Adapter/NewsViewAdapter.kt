package com.example.smartlabs.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlabs.Model.NewsItem
import com.example.smartlabs.R
import com.example.smartlabs.databinding.NewsItemBinding
import com.squareup.picasso.Picasso

class NewsViewAdapter (private val NewsList : List<NewsItem>)
    : RecyclerView.Adapter<NewsViewAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind(NewsList[position])

    override fun getItemCount() = NewsList.size

    inner class NewsViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = NewsItemBinding.bind(itemView)

        fun bind(newsItem: NewsItem) = with(binding) {
            title.text = newsItem.name
            description.text = newsItem.description
            price.text = newsItem.price
            Picasso.get().load(newsItem.image).into(image)
        }
    }
}