package com.example.pocemployee.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pocemployee.R
import com.example.pocemployee.ui.employeeData.DataSourceActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * This activity is a View class for handling the login UI.
 */
class LoginActivity : AppCompatActivity(), ILogin.View {

    private val loginViewModel: ILogin.ViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    val LOGIN_HELP_MESSAGE = "Valid email: user@mail.com\n" +
            "Valid password : at least 8 characters long\n" +
            "and contains at least one special character "

    /**
     * This method sets listener for the password field and the log in button.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.pocemployee.R.layout.activity_login)

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
        if (loginViewModel.attemptLogin(email, password))
        {
            login_progress.visibility = View.VISIBLE
            startActivity(Intent(this@LoginActivity, DataSourceActivity::class.java))
        }
        else
        {
            login_help.setBackgroundResource(R.drawable.help_button_bg)
        }
    }

    /**
     * showhelp is a onClickListener method for the login_help button
     * which shows a Toast with the LOGIN_HELP_MESSAGE.
     */
    fun showHelp(view: View)
    {
        Toast.makeText(this, LOGIN_HELP_MESSAGE, Toast.LENGTH_LONG).show()
    }
}
