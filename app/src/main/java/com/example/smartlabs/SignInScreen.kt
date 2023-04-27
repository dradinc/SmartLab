package com.example.smartlabs

import android.content.Intent
import android.graphics.PorterDuff
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.convertTo
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.ApiData
import com.example.smartlabs.Common.OnBoardingStatus
import com.example.smartlabs.databinding.ActivitySignInScreenBinding
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.math.log
import kotlin.math.round


class SignInScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySignInScreenBinding
    private lateinit var dataStoreOnBoarding: OnBoardingStatus
    private val email_mask = "[A-Za-z0-9]+@[a-z]+\\.+[a-z]{2,3}"
    private var timer: CountDownTimer? = null
    private var client = OkHttpClient()
    var callbackOpenEntryCode: (()->Unit)?=null
    var callbackReapetSendCode: (()->Unit)?=null
    var callbackSendCode: (()->Unit)?=null
    var callbackReapetSendCodeStart: (()->Unit)?=null
    var callbackReapetSendCodeEnd: (()->Unit)?=null
    var callbackSignInSuccess: (()->Unit)?=null
    var callbackSignIn: (()->Unit)?=null
    var callbackSignInError: (()->Unit)?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Точка обратного вызова
        callbackOpenEntryCode = {
            binding.title.visibility = View.INVISIBLE
            binding.authEmailBlock.visibility = View.INVISIBLE
            binding.authWithBlock.visibility = View.INVISIBLE

            binding.entryCode.visibility = View.VISIBLE
            binding.backBtn.visibility = View.VISIBLE
            binding.code1.requestFocus()
            startTimer(60000)
        }
        callbackSignInSuccess = {
            binding.code1.setBackgroundResource(R.drawable.edit_text_success)
            binding.code2.setBackgroundResource(R.drawable.edit_text_success)
            binding.code3.setBackgroundResource(R.drawable.edit_text_success)
            binding.code4.setBackgroundResource(R.drawable.edit_text_success)
            binding.indicatorCodeScreen.visibility = View.INVISIBLE
            timer?.cancel()
        }
        callbackSignInError = {
            binding.code1.setBackgroundResource(R.drawable.edit_text_error)
            binding.code2.setBackgroundResource(R.drawable.edit_text_error)
            binding.code3.setBackgroundResource(R.drawable.edit_text_error)
            binding.code4.setBackgroundResource(R.drawable.edit_text_error)
            binding.indicatorCodeScreen.visibility = View.INVISIBLE
            startTimer(60000)
        }
        callbackSendCode = {
            binding.indicatorEmailScreen.visibility = View.INVISIBLE
        }
        callbackReapetSendCode = {
            Toast.makeText(this@SignInScreen, "Код повторно отправлен", Toast.LENGTH_LONG).show()
        }
        callbackReapetSendCodeStart = {
            binding.indicatorCodeScreen.visibility = View.VISIBLE
            binding.timerText.text = "Повторная отправка кода..."
        }
        callbackReapetSendCodeEnd = {
            binding.indicatorCodeScreen.visibility = View.INVISIBLE
            startTimer(60000)
        }
        callbackSignIn = {
            binding.indicatorCodeScreen.visibility = View.VISIBLE
            binding.timerText.text = "Провекра кода..."
        }


        // Проверка был ли запущен Приветсвенный экран
        dataStoreOnBoarding = OnBoardingStatus(this)
        val onBoardingStatus = dataStoreOnBoarding.getOnBoardingStatus().asLiveData()
        // Получаем данные из памяти
        onBoardingStatus.observe(this) { onBoardStat ->
            Log.e("ON_BOARD_STATUS", onBoardStat.toString())
            when (onBoardStat) {
                true -> {}
                false -> {
                    val onBoardingActivity = Intent(this@SignInScreen, OnBoarding::class.java)
                    startActivity(onBoardingActivity)
                    lifecycleScope.launch { dataStoreOnBoarding.setOnBoardingStatus(true) }
                }
            }
        }

        // Обработчик событий на поле вводе при изменении значения поля
        binding.entryEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                // Проверяет E-mail на валидность
                if (p0.toString().trim().matches(email_mask.toRegex())) {
                    binding.signinEmailBtn.isEnabled = true
                    binding.signinEmailBtn.backgroundTintMode = PorterDuff.Mode.MULTIPLY
                } else {
                    //binding.entryEmail.error = "Не верный формат E-mail"
                    binding.signinEmailBtn.isEnabled = false
                    binding.signinEmailBtn.backgroundTintMode = PorterDuff.Mode.SRC_IN
                }
            }

        })

        // Кнопка Далее
        binding.signinEmailBtn.setOnClickListener {
            binding.indicatorEmailScreen.visibility = View.VISIBLE
            val body = ("").toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(ApiData.serverUrl+ApiData.sendCode)
                .header("email", binding.entryEmail.text.toString())
                .post(body)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    AlertDialog.Builder(this@SignInScreen)
                        .setTitle("Ошибка")
                        .setMessage("Во время запроса произошла ошибка")
                        .setNeutralButton("Ок") { _, _ ->

                        }.create().show()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        Handler(Looper.getMainLooper()).post {
                            // Совершаем обратный вызов
                            callbackOpenEntryCode!!.invoke()
                            callbackSendCode!!.invoke()
                        }
                    }
                    else if (response.code == 422) {
                        AlertDialog.Builder(this@SignInScreen)
                            .setTitle("Ошибка")
                            .setMessage("Во время запроса произошла ошибка: ${response.code}")
                            .setNeutralButton("Ок") { _, _ ->

                            }.create().show()
                        callbackSendCode!!.invoke()
                    }
                    else {
                        AlertDialog.Builder(this@SignInScreen)
                            .setTitle("Ошибка")
                            .setMessage("Во время запроса произошла ошибка: ${response.code}")
                            .setNeutralButton("Ок") { _, _ ->

                            }.create().show()
                        callbackSendCode!!.invoke()
                    }
                }
            })
        }

        // Кнопка Назад
        binding.backBtn.setOnClickListener {
            binding.title.visibility = View.VISIBLE
            binding.authEmailBlock.visibility = View.VISIBLE
            binding.authWithBlock.visibility = View.VISIBLE

            binding.entryCode.visibility = View.INVISIBLE
            binding.backBtn.visibility = View.INVISIBLE
            binding.code1.text.clear()
            binding.code2.text.clear()
            binding.code3.text.clear()
            binding.code4.text.clear()
            binding.entryEmail.requestFocus()
            timer?.cancel()
        }

        // Поля для ввода кода из E-mail
        binding.code1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.code1.text.isNotEmpty()) binding.code2.requestFocus()
            }
        })
        binding.code1.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) return@OnKeyListener true
            return@OnKeyListener false
        })

        binding.code2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.code2.text.isNotEmpty()) binding.code3.requestFocus()
            }
        })
        binding.code2.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.code2.text.isEmpty()) {
                    binding.code1.text.clear()
                    binding.code1.requestFocus()
                    return@OnKeyListener true
                }
            }
            return@OnKeyListener false
        })

        binding.code3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.code3.text.isNotEmpty()) binding.code4.requestFocus()
            }
        })
        binding.code3.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.code3.text.isEmpty()) {
                    binding.code2.text.clear()
                    binding.code2.requestFocus()
                    return@OnKeyListener true
                }
            }
            return@OnKeyListener false
        })

        binding.code4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.code4.text.isEmpty()){
                    binding.code1.setBackgroundResource(R.drawable.edit_text_grey)
                    binding.code2.setBackgroundResource(R.drawable.edit_text_grey)
                    binding.code3.setBackgroundResource(R.drawable.edit_text_grey)
                    binding.code4.setBackgroundResource(R.drawable.edit_text_grey)
                } else checkCode()
            }
        })
        binding.code4.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.code4.text.isEmpty()) {
                    binding.code3.text.clear()
                    binding.code3.requestFocus()
                    return@OnKeyListener true
                }
            }
            return@OnKeyListener false
        })
    }

    private fun startTimer(timerMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timerMillis, 1000) {
            override fun onTick(p0: Long) {
                var ost = round(p0.toDouble()/1000).toInt()
                binding.timerText.text = "Отправить код повторно можно будет через $ost секунд"
            }
            override fun onFinish() {
                callbackReapetSendCodeStart!!.invoke()
                val body = ("").toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url(ApiData.serverUrl+ApiData.sendCode)
                    .header("email", binding.entryEmail.text.toString())
                    .post(body)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        AlertDialog.Builder(this@SignInScreen)
                            .setTitle("Ошибка")
                            .setMessage("Во время запроса произошла ошибка")
                            .setNeutralButton("Ок") { _, _ ->

                            }.create().show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.code == 200) {
                            Handler(Looper.getMainLooper()).post {
                                callbackReapetSendCode!!.invoke()
                                callbackReapetSendCodeEnd!!.invoke()
                            }
                        }
                        else if (response.code == 422) {
                            AlertDialog.Builder(this@SignInScreen)
                                .setTitle("Ошибка")
                                .setMessage("Во время запроса произошла ошибка: ${response.code}")
                                .setNeutralButton("Ок") { _, _ ->

                                }.create().show()
                            callbackReapetSendCodeEnd!!.invoke()
                        }
                        else {
                            AlertDialog.Builder(this@SignInScreen)
                                .setTitle("Ошибка")
                                .setMessage("Во время запроса произошла ошибка: ${response.code}")
                                .setNeutralButton("Ок") { _, _ ->

                                }.create().show()
                            callbackReapetSendCodeEnd!!.invoke()
                        }
                    }
                })
            }

        }.start()
    }

    private fun checkCode(){
        callbackSignIn!!.invoke()
        timer?.cancel()
        val body = ("").toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val codeUser = binding.code1.text.toString() +
                binding.code2.text.toString() +
                binding.code3.text.toString() +
                binding.code4.text.toString()
        val request = Request.Builder()
            .url(ApiData.serverUrl+ApiData.signIn)
            .header("email", binding.entryEmail.text.toString())
            .header("code", codeUser)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    Handler(Looper.getMainLooper()).post {
                        // Совершаем обратный вызов
                        callbackSignInSuccess!!.invoke()
                    }
                }
                else if (response.code == 422) {
                    Handler(Looper.getMainLooper()).post {
                        // Совершаем обратный вызов
                        callbackSignInError!!.invoke()
                    }
                }
            }
        })
    }
}