package com.example.smartlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.OnBoardingStatus
import com.example.smartlabs.Common.SignInStatus
import com.example.smartlabs.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreSignInStatus: SignInStatus
    private lateinit var dataStoreOnBoarding: OnBoardingStatus
    private var signInStatus = false
    private lateinit var signInToken: String

    private var callbackWriteToken: (()->Unit)?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callbackWriteToken = {
            binding.simpleText.text = signInToken
        }

        dataStoreSignInStatus = SignInStatus(this)
        dataStoreOnBoarding = OnBoardingStatus(this)
        lifecycleScope.launch { userData() }

        binding.clearSignInStatus.setOnClickListener {
            lifecycleScope.launch {
                dataStoreSignInStatus.setSignInStatus(false, "")
            }
        }

        binding.clearOnBoardingStatus.setOnClickListener {
            lifecycleScope.launch {
                dataStoreOnBoarding.setOnBoardingStatus(false)
            }
        }
    }

    private suspend fun userData() {
        signInStatus = dataStoreSignInStatus.getSignInStatus().first()
        when(signInStatus){
            true -> {}
            false -> {}
        }
        signInToken = dataStoreSignInStatus.getSignInToken().first()
        Handler(Looper.getMainLooper()).post {
            callbackWriteToken!!.invoke()
        }
    }
}