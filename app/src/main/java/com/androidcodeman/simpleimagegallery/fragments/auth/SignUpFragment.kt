package com.example.calculater.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidcodeman.simpleimagegallery.R
import com.androidcodeman.simpleimagegallery.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_sign_up, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUp.setOnClickListener {
            val loginName = login.text.toString().trim()
            getAuthViewModel()?.signUp(loginName, password.text.toString().trim(), confirmPassword.text.toString())?.let { result ->
                when (result) {
                    AuthViewModel.SignUpResult.EXIST -> {
                        Toast.makeText(it.context, R.string.user_exist, Toast.LENGTH_LONG).show()
                    }
                    AuthViewModel.SignUpResult.SUCCESS -> {
                        getSessionViewModel()?.auth(loginName)
                        (it.context as? Listener)?.successSignUp()
                    }
                    AuthViewModel.SignUpResult.EMPTY_DATA -> {
                        Toast.makeText(it.context, R.string.empty_value, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        signIn.setOnClickListener { (it.context as? Listener)?.onSignInClick() }
    }

    interface Listener {
        fun onSignInClick()
        fun successSignUp()
    }
}