package com.example.smartlabs

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.UserAppCode
import com.example.smartlabs.databinding.ActivityCodeScreenBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CodeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCodeScreenBinding
    private lateinit var dataStoreAppCode: UserAppCode

    private var code = ""

    private var callbackAppCodeScreenCreate: (()->Unit)?=null
    private var callbackAppCodeScreenInput: (()->Unit)?=null
    private var callbackAppCodeScreenSuccess: (()->Unit)?=null
    private var callbackAppCodeScreenError: (()->Unit)?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callbackAppCodeScreenCreate = {
            binding.skipBtn.visibility = View.VISIBLE
            binding.titleScreen.text = binding.root.resources.getString(R.string.title_code_screen_create)
            binding.descriptionScreen.text = binding.root.resources.getString(R.string.description_code_screen)
        }
        callbackAppCodeScreenInput = {
            binding.skipBtn.visibility = View.INVISIBLE
            binding.titleScreen.text = binding.root.resources.getString(R.string.title_code_screen)
            binding.descriptionScreen.visibility = View.INVISIBLE
        }
        callbackAppCodeScreenSuccess = {
            binding.code1.setImageResource(R.drawable.indicator_succes)
            binding.code2.setImageResource(R.drawable.indicator_succes)
            binding.code3.setImageResource(R.drawable.indicator_succes)
            binding.code4.setImageResource(R.drawable.indicator_succes)
            finish()
        }
        callbackAppCodeScreenError = {
            binding.code1.setImageResource(R.drawable.indicator_error)
            binding.code2.setImageResource(R.drawable.indicator_error)
            binding.code3.setImageResource(R.drawable.indicator_error)
            binding.code4.setImageResource(R.drawable.indicator_error)
        }

        dataStoreAppCode = UserAppCode(this)
        lifecycleScope.launch {
            checkAppCodeStatus()
        }

        binding.keyCode1.setOnClickListener { keyPress(1) }
        binding.keyCode2.setOnClickListener { keyPress(2) }
        binding.keyCode3.setOnClickListener { keyPress(3) }
        binding.keyCode4.setOnClickListener { keyPress(4) }
        binding.keyCode5.setOnClickListener { keyPress(5) }
        binding.keyCode6.setOnClickListener { keyPress(6) }
        binding.keyCode7.setOnClickListener { keyPress(7) }
        binding.keyCode8.setOnClickListener { keyPress(8) }
        binding.keyCode9.setOnClickListener { keyPress(9) }
        binding.keyCode0.setOnClickListener { keyPress(0) }
        binding.keyCodeDel.setOnClickListener { keyPress(-1) }
        binding.skipBtn.setOnClickListener { finish() }
    }

    private fun keyPress(number: Int) {
        when(number) {
            1 -> code += "1"
            2 -> code += "2"
            3 -> code += "3"
            4 -> code += "4"
            5 -> code += "5"
            6 -> code += "6"
            7 -> code += "7"
            8 -> code += "8"
            9 -> code += "9"
            0 -> code += "0"
            -1 -> code = code.dropLast(1)
        }
        when(code.length) {
            0 -> {
                binding.code1.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code2.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code3.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code4.setImageResource(R.drawable.onboardong_indicator_inactive)
            }
            1 -> {
                binding.code1.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code2.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code3.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code4.setImageResource(R.drawable.onboardong_indicator_inactive)
            }
            2 -> {
                binding.code1.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code2.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code3.setImageResource(R.drawable.onboardong_indicator_inactive)
                binding.code4.setImageResource(R.drawable.onboardong_indicator_inactive)
            }
            3 -> {
                binding.code1.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code2.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code3.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code4.setImageResource(R.drawable.onboardong_indicator_inactive)
            }
            4 -> {
                binding.code1.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code2.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code3.setImageResource(R.drawable.onboardong_indicator_active)
                binding.code4.setImageResource(R.drawable.onboardong_indicator_active)
                Handler(Looper.getMainLooper()).post {
                    lifecycleScope.launch {
                        checkAppCode()
                    }
                }
            }
            5 -> code = code.dropLast(1)
        }
        Log.i("CODE_INPUT", code)
    }

    private suspend fun checkAppCode() {
        if (dataStoreAppCode.getAppCodeStatus().first()) {
            if (dataStoreAppCode.getAppCode().first() == code.hashCode()) {
                Handler(Looper.getMainLooper()).post {
                    callbackAppCodeScreenSuccess!!.invoke()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    callbackAppCodeScreenError!!.invoke()
                }
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                lifecycleScope.launch {
                    dataStoreAppCode.setAppCode(code)
                }
                callbackAppCodeScreenSuccess!!.invoke()
            }
        }
    }

    private suspend fun checkAppCodeStatus() {
        if (dataStoreAppCode.getAppCodeStatus().first()) {
            Handler(Looper.getMainLooper()).post {
                callbackAppCodeScreenInput!!.invoke()
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                callbackAppCodeScreenCreate!!.invoke()
            }
        }
    }
}