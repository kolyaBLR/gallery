package com.androidcodeman.simpleimagegallery.viewmodel

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("security", Activity.MODE_PRIVATE)
    }

    fun signUp(login: String, password: String, confirmPassword: String): SignUpResult {
        val isUniqueUser = sharedPreferences.getString(login, null) == null
        if (!isUniqueUser) {
            return SignUpResult.EXIST
        }
        val isEmptyData = login.isEmpty() || password.isEmpty()
        if (isEmptyData || password != confirmPassword) {
            return SignUpResult.EMPTY_DATA
        }
        sharedPreferences.edit().putString(login, password).apply()
        return SignUpResult.SUCCESS
    }

    fun signIn(login: String, password: String): SignInResult {
        val isEmptyData = login.isEmpty() || password.isEmpty()
        if (isEmptyData) {
            return SignInResult.EMPTY_DATA
        }
        val sharedPassword = sharedPreferences.getString(login, null)
        return if (password == sharedPassword) SignInResult.SUCCESS else SignInResult.FAILED
    }

    enum class SignUpResult {
        EXIST, SUCCESS, EMPTY_DATA
    }

    enum class SignInResult {
        SUCCESS, EMPTY_DATA, FAILED
    }
}