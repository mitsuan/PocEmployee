package com.example.pocemployee.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.pocemployee.R
import com.example.pocemployee.databinding.ActivityLoginBinding
import com.example.pocemployee.ui.employeeData.DataSourceActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * This activity is a View class for handling the login UI.
 */
class LoginActivity : AppCompatActivity(), ILogin.View {

    val TAG = LoginActivity::class.java.simpleName

    private val loginViewModel: LoginViewModel by viewModel()
    private val loginSuccessObserver =
        Observer<Boolean> { loginSuccess ->
            Log.d(TAG, "in observe(), loginSuccess: $loginSuccess")
            loginSuccess?.let { if(loginSuccess) attemptLogin() }
        }

    /**
     * This method sets listener for the password field and the log in button.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel.loginSuccessNotifier.observe(this, loginSuccessObserver)
        loginViewModel.awsLoginInteractor.loginSuccessNotifier.observe(this, loginSuccessObserver)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = loginViewModel

    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "in onResume() method! ")

        val activityIntent = intent
        if (activityIntent?.data != null && loginViewModel.awsLoginInteractor.appRedirect?.host == activityIntent ?.data!!.host) {
            loginViewModel.awsLoginInteractor.auth?.getTokens(activityIntent.data)
            Log.d(TAG,"activity intent data: ${activityIntent.data}")
            if(activityIntent.data.toString() != getString(R.string.app_logout_redirect))
            {
                loginViewModel.loginProgressVisibility.set(View.VISIBLE)
            }
        }

    }

    fun setLoginFragment(){
        val loginFragment = LoginFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutContainer, loginFragment)
        transaction.commit()
    }

    /**
     * This method checks performs the login attempt using the loginViewModel object
     * and based on the Boolean response it either makes the progress bar visible and
     * switches activity or does nothing.
     */
    override fun attemptLogin() {

        Log.d(TAG, "in attemptLogin()")
        startActivity(Intent(this@LoginActivity, DataSourceActivity::class.java))
    }

}
