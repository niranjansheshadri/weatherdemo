package com.example.weatherdemo.ui.prelogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.weatherdemo.R
import com.example.weatherdemo.databinding.ActivityLoginRegisterBinding

class LoginOrRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginRegisterBinding>(
            this,
            R.layout.activity_login_register
        )
    }

}