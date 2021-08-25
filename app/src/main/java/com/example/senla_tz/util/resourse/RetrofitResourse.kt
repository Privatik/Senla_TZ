package com.example.senla_tz.util.resourse

import com.google.gson.annotations.SerializedName

enum class FailAuthStatus(val text: String){
    @SerializedName("INVALID_CREDENTIALS")
    INVALID_CREDENTIALS("Неправильный email, пароль или и то и другое."),
    @SerializedName("REQUIRED_FIELDS_ARE_EMPTY")
    REQUIRED_FIELDS_ARE_EMPTY("В запросе есть пустые поля"),
    @SerializedName("INVALID_REQUEST_DATA")
    INVALID_REQUEST_DATA("Не удалось десериализовать данные запроса"),
    @SerializedName("INVALID_EMAIL")
    INVALID_EMAIL("Неправильный формат email-а"),
    @SerializedName("EMAIL_ALREADY_EXISTS")
    EMAIL_ALREADY_EXISTS("Пользователь с таким email уже был зарегистрирован."),
}

enum class FailUserStatus(val text: String){
    @SerializedName("INVALID_ID")
    INVALID_ID("Неправильный ID трека"),
    @SerializedName("INVALID_TOKEN")
    INVALID_TOKEN("Неверный авторизационный токен"),
    @SerializedName("INVALID_FIELDS")
    INVALID_FIELDS("Неверные значения"),
    @SerializedName("NO_POINTS")
    NO_POINTS("Нет поля points"),
    @SerializedName("INVALID_POINTS")
    INVALID_POINTS("какая-то одна (из всех) точек неправильная")
}