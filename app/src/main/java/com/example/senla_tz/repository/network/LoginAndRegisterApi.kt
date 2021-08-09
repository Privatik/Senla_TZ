package com.example.senla_tz.repository.network

import com.example.senla_tz.repository.network.data.LoginRequest
import com.example.senla_tz.repository.network.data.LoginResponse
import com.example.senla_tz.repository.network.data.RegisterRequest
import com.example.senla_tz.repository.network.data.RegisterResponse
import com.example.senla_tz.util.Constant
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginAndRegisterApi {

    @POST(Constant.ENDPOINT_REGISTER)
    @Headers(
        "Content-Type: application/json",
    )
    suspend fun register(@Body request: RegisterRequest): RegisterResponse?

    @POST(Constant.ENDPOINT_LOGIN)
    @Headers(
        "Content-Type: application/json",
    )
    suspend fun login(@Body request: LoginRequest): LoginResponse?
}