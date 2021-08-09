package com.example.senla_tz.util.resourse

import com.google.gson.annotations.SerializedName

enum class FailAuthStatus(val text: String){
    @SerializedName("INVALID_CREDENTIALS")
    INVALID_CREDENTIALS("Неправильный email, пароль или и то и другое."),
    @SerializedName("REQUIRED_FIELDS_ARE_EMPTY")
    REQUIRED_FIELDS_ARE_EMPTY("В запросе есть пустые поля"),
    @SerializedName("INVALID_EMAIL")
    INVALID_EMAIL("Неправильный формат email-а"),
    @SerializedName("EMAIL_ALREADY_EXISTS")
    EMAIL_ALREADY_EXISTS("Пользователь с таким email уже был зарегистрирован."),
}