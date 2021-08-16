package com.example.senla_tz.repository.pref

import android.content.Context
import com.example.senla_tz.entify.Token
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN = "token"
@Singleton
class TokenPref
@Inject constructor(
    @ApplicationContext context: Context
)
{
    private val sp = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    fun saveToken(token: Token){
        sp.edit().putString(TOKEN, token.text).apply()
    }

    fun getToken(): Token = Token(sp.getString(TOKEN, "") ?: "")

    fun deleteToken(){
        sp.edit().remove(TOKEN).apply()
    }
}