package com.example.smartlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.SignInStatus
import com.example.smartlabs.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreSignInStatus: SignInStatus
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
        lifecycleScope.launch { userData() }
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