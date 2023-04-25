package com.example.smartlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.OnBoardingStatus
import com.example.smartlabs.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreOnBoarding: OnBoardingStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreOnBoarding = OnBoardingStatus(this)
        val onBoardingStatus = dataStoreOnBoarding.getOnBoardingStatus().asLiveData()
        // Получаем данные из памяти
        onBoardingStatus.observe(this) {onBoardStat ->
            when(onBoardStat) {
                true -> {

                }
                false -> {
                    val onBoardingActivity = Intent(this@MainActivity, OnBoarding::class.java)
                    startActivity(onBoardingActivity)
                    lifecycleScope.launch { dataStoreOnBoarding.setOnBoardingStatus(true) }
                }
            }
        }


    }
}