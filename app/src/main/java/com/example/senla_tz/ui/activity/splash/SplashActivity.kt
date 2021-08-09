package com.example.senla_tz.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.senla_tz.R
import com.example.senla_tz.ui.activity.auth.AuthActivity

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper())
            .postDelayed(
                {
//                    val token = vm.getToken()
//                    Log.e("TAG","tokern - $token")
//                    val hasToken = token.isNotEmpty()
//                    if (hasToken) {
//                        val i = Intent(this, MainActivity::class.java)
//                        startActivity(i)
//                    } else {
                        val i = Intent(this, AuthActivity::class.java)
                        startActivity(i)
                    //}
                    finish()
                }, 3000
            )
    }

}