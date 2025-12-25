package com.example.myapp.util

import android.content.Context
import android.content.SharedPreferences

object TokenStore {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(context: Context, token: String) {
        prefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? = prefs(context).getString(KEY_TOKEN, null)

    fun clear(context: Context) { prefs(context).edit().remove(KEY_TOKEN).apply() }
}
