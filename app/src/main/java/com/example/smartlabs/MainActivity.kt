package com.example.smartlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.example.smartlabs.Common.SignInStatus
import com.example.smartlabs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreSignInStatus: SignInStatus
    private var signInStatus = false
    private lateinit var signInToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreSignInStatus = SignInStatus(this)
        dataStoreSignInStatus.getSignInStatus().asLiveData().observe(this) {
            signInStatus = it
        }
        dataStoreSignInStatus.getSignInToken().asLiveData().observe(this) {
            signInToken = it
            binding.simpleText.text = signInToken
        }
    }
}