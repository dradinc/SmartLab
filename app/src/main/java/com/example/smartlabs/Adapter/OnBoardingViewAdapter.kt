package com.example.smartlabs.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlabs.Model.OnBoardingItem
import com.example.smartlabs.R
import com.example.smartlabs.databinding.OnBoardingSliderLayoutBinding

class OnBoardingViewAdapter(private val onBoardingItems: List<OnBoardingItem>)
    : RecyclerView.Adapter<OnBoardingViewAdapter.OnBoardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.on_boarding_slider_layout, parent, false)
        )
    }

    // Вызывает функцию для отображения элементов текущего положения на слой
    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) = holder.bind(onBoardingItems[position])

    // Возвращает длину(размер) списка
    override fun getItemCount() = onBoardingItems.size

    inner class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = OnBoardingSliderLayoutBinding.bind(itemView)  // Переменая для использования биндинга слоя для отображения слайдера
        fun bind(onBoardingItem: OnBoardingItem) = with(binding){
            image.setImageResource(onBoardingItem.image)  // Устанавливаем изображение
            textTitle.text = binding.root.resources.getString(onBoardingItem.title)  // Устанавливаем заголовок
            textDescription.text = binding.root.resources.getString(onBoardingItem.description)  // Устанавливаем описание
        }
    }
}