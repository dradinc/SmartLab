package com.example.smartlabs.Model


// Информационный класс для хранения элементов приветсвенного экрана для использования в RecyclerViewAdapter
data class OnBoardingItem(
    val image: Int,  // Хранение ссылки на ресурс изображения страницы
    val title: Int,  // Хранение ссылки на ресурс заголовка страницы
    val description: Int  // Хранение ссылки на ресурс описания страницы
)
