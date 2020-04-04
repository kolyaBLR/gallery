package com.example.calculater.fragments.auth

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.androidcodeman.simpleimagegallery.viewmodel.AuthViewModel
import com.androidcodeman.simpleimagegallery.viewmodel.SessionViewModel

abstract class BaseFragment: Fragment() {

    protected fun getAuthViewModel(): AuthViewModel? {
        return activity?.let { ViewModelProviders.of(it).get(AuthViewModel::class.java) }
    }

    protected fun getSessionViewModel(): SessionViewModel? {
        return activity?.let { ViewModelProviders.of(it).get(SessionViewModel::class.java) }
    }
}