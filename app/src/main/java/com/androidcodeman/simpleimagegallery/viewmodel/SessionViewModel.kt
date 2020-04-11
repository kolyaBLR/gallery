package com.androidcodeman.simpleimagegallery.viewmodel

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("session", Activity.MODE_PRIVATE)
    }

    fun auth(login: String?) {
        sharedPreferences.edit().putString("AUTH", login).apply()
    }

    fun isAuth() = sharedPreferences.getString("AUTH", null) != null

    fun getUserName() = sharedPreferences.getString("AUTH", null)
}