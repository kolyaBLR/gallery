package com.example.calculater.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidcodeman.simpleimagegallery.R
import com.androidcodeman.simpleimagegallery.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_sign_in, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signIn.setOnClickListener {
            val loginName = login.text.toString().trim()
            getAuthViewModel()?.signIn(loginName, password.text.toString().trim())?.let { result ->
                when(result) {
                    AuthViewModel.SignInResult.SUCCESS -> {
                        getSessionViewModel()?.auth(loginName)
                        (it.context as? Listener)?.successSignIn()
                    }
                    AuthViewModel.SignInResult.EMPTY_DATA -> {
                        Toast.makeText(it.context, R.string.empty_value, Toast.LENGTH_LONG).show()
                    }
                    AuthViewModel.SignInResult.FAILED -> {
                        Toast.makeText(it.context, R.string.failed_auth, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        back.setOnClickListener { (it.context as? Listener)?.onBackPressed() }
    }

    interface Listener {
        fun onBackPressed()
        fun successSignIn()
    }
}