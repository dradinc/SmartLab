package com.example.smartlabs.Common

class ApiData {

    companion object{
        // Ссылка на API
        const val serverUrl = "https://medic.madskill.ru/api"

        // Ссылки внутри API
        // Данные для авторизации отправляются в header
        const val sendCode = "/sendCode"  // Для получения кода на почту
        const val signIn = "/signin"  // Для авторизации пользователя

        // Получение новостей
        const val getNews = "/news"
        const val getCatalog = "/catalog"
    }
}