package com.example.smartlabs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartlabs.Adapter.CatalogCategoryViewAdapter
import com.example.smartlabs.Adapter.CatalogViewAdapter
import com.example.smartlabs.Adapter.NewsViewAdapter
import com.example.smartlabs.Common.ApiData
import com.example.smartlabs.Model.CatalogItem
import com.example.smartlabs.Model.NewsItem
import com.example.smartlabs.databinding.FragmentMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class MainFragment : Fragment() {
    companion object {
        var newsList = ArrayList<NewsItem>()  // Список новостей
        var catalogList = ArrayList<CatalogItem>()  // Список товаров
        var categoryList = ArrayList<String>()  // Список категорий
    }

    private lateinit var binding : FragmentMainBinding

    // Для работы с сервером
    private val client = OkHttpClient()

    // News
    private var newsViewAdapter = NewsViewAdapter(newsList)
    private var catalogViewAdapter = CatalogViewAdapter(catalogList)
    private var categoryViewAdapter = CatalogCategoryViewAdapter(categoryList)
    private
    var callbackNewsViewAdapter: (()->Unit)?=null
    var callbackCatalogViewAdapter: (()->Unit)?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обратные вызовы
        callbackNewsViewAdapter = { // Обратный вызов назначение адаптера для отображения новостей
            binding.recyclerViewNews.adapter = newsViewAdapter
            binding.indicatorNewsLoad.visibility = View.INVISIBLE
        }
        callbackCatalogViewAdapter = {
            binding.recyclerViewCatalogTovar.adapter = catalogViewAdapter
            binding.recyclerViewCatalogType.adapter = categoryViewAdapter
            binding.indicatorCatalogLoad.visibility = View.INVISIBLE
        }

        //
        if (newsList.isEmpty()) {
            binding.indicatorNewsLoad.visibility = View.VISIBLE
            val requestNews = Request.Builder().url(ApiData.serverUrl + ApiData.getNews).build()
            client.newCall(requestNews).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        val jsonArray = JSONArray(response.body.string())
                        for (i in 0 until jsonArray.length()) {
                            newsList.add(
                                NewsItem(
                                    id = jsonArray.getJSONObject(i).getInt("id"),
                                    name = jsonArray.getJSONObject(i).getString("name"),
                                    description = jsonArray.getJSONObject(i)
                                        .getString("description"),
                                    price = jsonArray.getJSONObject(i).getString("price"),
                                    image = jsonArray.getJSONObject(i).getString("image")
                                )
                            )
                        }
                        Handler(Looper.getMainLooper()).post {
                            callbackNewsViewAdapter!!.invoke()
                        }
                        Log.i("LOAD_NEW", jsonArray.toString())
                    }
                }
            })
        } else {
            callbackNewsViewAdapter!!.invoke()
        }

        //
        if (catalogList.isEmpty()) {
            binding.indicatorCatalogLoad.visibility = View.VISIBLE
            val requestCatalog =
                Request.Builder().url(ApiData.serverUrl + ApiData.getCatalog).build()
            client.newCall(requestCatalog).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {
                    val jsonArray = JSONArray(response.body.string())
                    for (i in 0 until jsonArray.length()) {
                        catalogList.add(
                            CatalogItem(
                                id = jsonArray.getJSONObject(i).getInt("id"),
                                name = jsonArray.getJSONObject(i).getString("name"),
                                description = jsonArray.getJSONObject(i).getString("description"),
                                price = jsonArray.getJSONObject(i).getString("price"),
                                category = jsonArray.getJSONObject(i).getString("category"),
                                time_result = jsonArray.getJSONObject(i).getString("time_result"),
                                preparation = jsonArray.getJSONObject(i).getString("preparation"),
                                bio = jsonArray.getJSONObject(i).getString("bio"),
                            )
                        )
                        if (jsonArray.getJSONObject(i)
                                .getString("category") !in categoryList
                        ) categoryList.add(jsonArray.getJSONObject(i).getString("category"))
                    }
                    Handler(Looper.getMainLooper()).post {
                        callbackCatalogViewAdapter!!.invoke()
                    }
                    Log.i("LOAD_NEW", jsonArray.toString())
                }

            })
        } else {
            callbackCatalogViewAdapter!!.invoke()
        }
    }
}