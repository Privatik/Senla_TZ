package com.example.senla_tz.repository

import android.util.Log
import com.example.senla_tz.entify.Token
import com.example.senla_tz.repository.network.data.LoginRequest
import com.example.senla_tz.repository.network.data.RegisterRequest
import com.example.senla_tz.repository.network.LoginAndRegisterApi
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.util.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

private val TAG = LoginAndRegisterRepository::class.java.simpleName
class LoginAndRegisterRepository @Inject constructor(
    private val service: LoginAndRegisterApi,
    private val tokenPref: TokenPref
) {

    val authorizationFlow = MutableSharedFlow<String>()
    val authorizationFailFlow = MutableSharedFlow<String>()


    suspend fun register(request: RegisterRequest) {
        try {
            val res = service.register(request = request)

            if (res.status == Constant.StatusResponse.OK){
                Token(token = res.token).also {
                    authorizationFlow.emit("Добро пожаловать ${request.firstName}")
                    tokenPref.saveToken(it)
                }
            }else{
                authorizationFailFlow.emit(res.code!!.text)
            }

        } catch (exception: Exception) {
            Log.e(TAG,exception.message?: exception.toString())
            authorizationFailFlow.emit(Constant.ERROR_FROM_SERVICE)
        }
    }

    suspend fun login(request: LoginRequest){
        try {
            val res = service.login(request = request)

            if (res.status == Constant.StatusResponse.OK){
                Token(token = res.token).also {
                    authorizationFlow.emit("Добро пожаловать ${res.firstName}")
                    tokenPref.saveToken(it)
                }
            }else{
                authorizationFailFlow.emit(res.code!!.text)
            }

        } catch (exception: Exception) {
            Log.e(TAG,exception.message?: exception.toString())
            authorizationFailFlow.emit(Constant.ERROR_FROM_SERVICE)
        }
    }
}