package com.example.senla_tz.ui.activity.splash

import androidx.lifecycle.ViewModel
import com.example.senla_tz.entify.Token
import com.example.senla_tz.repository.pref.TokenPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pref: TokenPref
): ViewModel() {

    fun getToken(): Token = pref.getToken()
}