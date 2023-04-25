package com.example.smartlabs

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlabs.databinding.ActivitySignInScreenBinding


class SignInScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySignInScreenBinding
    private val email_mask = "[A-Za-z0-9]+@[a-z]+\\.+[a-z]{2,3}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обработчик событий на поле вводе при изменении значения поля
        binding.entryEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

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


    }
}