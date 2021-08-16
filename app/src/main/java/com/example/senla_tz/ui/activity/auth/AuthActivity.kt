package com.example.senla_tz.ui.activity.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.senla_tz.R
import com.example.senla_tz.repository.LoginAndRegisterRepository
import com.example.senla_tz.ui.fragment.login_and_register.LoginAndRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}