package com.example.senla_tz.ui.fragment.login_and_register

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senla_tz.repository.LoginAndRegisterRepository
import com.example.senla_tz.repository.network.data.LoginRequest
import com.example.senla_tz.repository.network.data.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginAndRegisterViewModel @Inject constructor(
    private val repository: LoginAndRegisterRepository
    ): ViewModel() {

    val authorizationFlow: SharedFlow<String> by lazy { repository.authorizationFlow }
    val authorizationFailFlow: SharedFlow<String> by lazy { repository.authorizationFailFlow }

    val isLoginState by lazy { ObservableBoolean(true) }

    fun register(email: String, password: String, fistName: String, lastName: String){
        viewModelScope.launch {
            repository.register(
                RegisterRequest(
                email = email,
                password = password,
                firstName = fistName,
                lastName = lastName
            ))
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(
                LoginRequest(
                email = email,
                password = password
            ))
        }
    }
}