package com.example.pocemployee.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pocemployee.R
import com.example.pocemployee.databinding.ActivityLoginBinding
import com.example.pocemployee.ui.employeeData.DataSourceActivity

/**
 * This activity is a View class for handling the login UI.
 */
class LoginActivity : AppCompatActivity(), ILogin.View {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private val loginSuccessObserver =
        Observer<Boolean> {
                loginSuccess -> loginSuccess?.let { attemptLogin() }
        }

    /**
     * This method sets listener for the password field and the log in button.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel.loginSuccessNotifier.observe(this, loginSuccessObserver)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = loginViewModel



    }

    /**
     * This method checks performs the login attempt using the loginViewModel object
     * and based on the Boolean response it either makes the progress bar visible and
     * switches activity or does nothing.
     */
    override fun attemptLogin()
    {
        startActivity(Intent(this@LoginActivity, DataSourceActivity::class.java))
    }

}
