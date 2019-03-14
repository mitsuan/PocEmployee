package com.example.pocemployee.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pocemployee.R
import com.example.pocemployee.databinding.ActivityLoginBinding
import com.example.pocemployee.ui.employeeData.DataSourceActivity
import kotlinx.android.synthetic.main.activity_login.*

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
        setContentView(com.example.pocemployee.R.layout.activity_login)

        loginViewModel.loginSuccessNotifier.observe(this, loginSuccessObserver)


        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = loginViewModel

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        log_in_button.setOnClickListener {
            attemptLogin()
        }
    }

    /**
     * This method checks performs the login attempt using the loginViewModel object
     * and based on the Boolean response it either makes the progress bar visible and
     * switches activity or does nothing.
     */
    override fun attemptLogin()
    {
        startActivity(Intent(this@LoginActivity, DataSourceActivity::class.java))
//            login_help.setBackgroundResource(R.drawable.help_button_bg)
    }

}
