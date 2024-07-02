package com.example.logintoken.Model.Remote

import android.content.Context

class AuthManager (private val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)


    // Guardar un Token
    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }


    // Retorna  Token
    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }


    // ELIMINAR
    fun clearToken() {
        prefs.edit().remove("auth_token").apply()
    }

    // Eliminar token loguin
    fun clearUserLogged() {
        prefs.edit().remove("user_logged").apply()
    }
}