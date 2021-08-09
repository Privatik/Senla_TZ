package com.example.senla_tz.repository

import com.example.senla_tz.model.Token
import com.example.senla_tz.repository.network.data.LoginRequest
import com.example.senla_tz.repository.network.data.RegisterRequest
import com.example.senla_tz.repository.network.LoginAndRegisterApi
import com.example.senla_tz.repository.pref.TokenPref
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class LoginAndRegisterRepository @Inject constructor(
    private val service: LoginAndRegisterApi,
    private val tokenPref: TokenPref
) {

    val authorizationFlow = MutableSharedFlow<Token>()
    val authorizationFailFlow = MutableSharedFlow<String>()


    suspend fun register(request: RegisterRequest) {
        try {
            val res = service.register(request = request) ?: throw Exception("Response null")

            if (res.code == null){
                Token(text = res.token).also {
                    authorizationFlow.emit(it)
                    tokenPref.saveToken(it)
                }
            }else{
                authorizationFailFlow.emit(res.code!!.text)
            }

        } catch (exception: Exception) {
            authorizationFailFlow.emit(exception.message ?: "Error")
        }
    }

    suspend fun login(request: LoginRequest){
        //service.login(request = request)
    }
}