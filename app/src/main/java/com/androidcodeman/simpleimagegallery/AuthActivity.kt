package com.androidcodeman.simpleimagegallery

import android.content.Intent
import android.os.Bundle
import com.example.calculater.fragments.auth.SignInFragment
import com.example.calculater.fragments.auth.SignUpFragment


class AuthActivity : BaseActivity(), SignUpFragment.Listener, SignInFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, SignUpFragment())
            .commit()
    }

    private fun openCalculator() {
        val intent = Intent(this, RootActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun successSignIn() {
        openCalculator()
    }

    override fun successSignUp() {
        openCalculator()
    }

    override fun onSignInClick() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .add(android.R.id.content, SignInFragment())
            .addToBackStack(null)
            .commit()
    }
}
