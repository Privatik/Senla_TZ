package com.example.senla_tz.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.example.senla_tz.R
import com.example.senla_tz.ui.activity.auth.AuthActivity
import com.example.senla_tz.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val vm: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    val token = vm.getToken()
                    Log.e("TOKEN",token.token)
                    val hasToken = token.token.isNotEmpty()
                    if (hasToken) {
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(this, AuthActivity::class.java)
                        startActivity(i)
                    }
                    finish()
                }, 3000
            )
    }

}