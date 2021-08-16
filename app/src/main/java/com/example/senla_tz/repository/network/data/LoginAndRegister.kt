package com.example.senla_tz.repository.network.data

import com.example.senla_tz.util.Constant
import com.example.senla_tz.util.resourse.FailAuthStatus
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status")
    var status: Constant.StatusResponse?,
    @SerializedName("token")
    var token: String,
    @SerializedName("code")
    var code: FailAuthStatus?
)

data class RegisterRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String
)


data class LoginResponse(
    @SerializedName("status")
    var status: Constant.StatusResponse?,
    @SerializedName("token")
    var token: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("code")
    var code: FailAuthStatus?
)

data class LoginRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)





/*
* registerResponse
* {
  "status": "ok",
  "token": "..."
  * }
  *
  *
*
* registerRequest
* {
  "email": "...",
  "firstName": "...",
  "lastName": "...",
  "password": "..."
}
*
* loginResponse
* {
  "status": "ok",
  "token": "...",
  "firstName": "...",
  "lastName": "..."
  * }
*
* loginRequest
* {
  "email": "...",
  "password": "..."
  * }
*
* */