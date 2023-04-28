package com.example.smartlabs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartlabs.Adapter.NewsViewAdapter
import com.example.smartlabs.databinding.FragmentMainBinding
import okhttp3.OkHttpClient

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding

    // Для работы с сервером
    private val client = OkHttpClient()

    // News
    //private var newsViewAdapter = NewsViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}