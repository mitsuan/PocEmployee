package com.example.pocemployee.ui.login

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pocemployee.R
import java.util.regex.Pattern

/**
 * LoginViewModel is the ViewModel class of the View class LoginActivity.
 * It performs the login attempt along with validation checks.
 */
class LoginViewModel(val app: Application): AndroidViewModel(app), ILogin.ViewModel{

    var email: String? = ""
    var password: String? = ""
    var emailError: ObservableField<String>? = ObservableField()
    var passwordError: ObservableField<String>? = ObservableField()
    var loginProgressVisibility = View.GONE
    override val loginSuccessNotifier  = MutableLiveData<Boolean>()

    val LOGIN_HELP_MESSAGE = "Valid email: user@mail.com\n" +
            "Valid password : at least 8 characters long\n" +
            "and contains at least one special character "

    /**
     * Attempts to log in based on credentials specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no login attempt is made.
     */
    override fun attemptLogin(){

        Log.d("LoginViewModel","in AttemptLogin()")

        var isValid: Boolean

        var cancel = false
        var focusView: View? = null

        isValid = isPasswordValid()
        isValid = isEmailValid() && isValid
//        if (!isPasswordValid()) {
////            focusView = password
//            cancel = true
//        }
//
//        if (!isEmailValid()) {
////            focusView = email
//            cancel = true
//        }
//
//        if (cancel) {
////            focusView?.requestFocus()
//        } else {
//            isValid =  true
//        }
        if(isValid)
        {
            loginSuccessNotifier.value = isValid
            loginProgressVisibility = View.VISIBLE
        }


    }


    /**
     * isEmailValid method is used to check the validity of the email
     * It check whether the field is empty and also uses a regex to validate email format
     * and shows a validation error based on the error.
     */
    override fun isEmailValid(): Boolean{
        var isNotEmpty = true
        var patternMatched = true

        if (TextUtils.isEmpty(email)) {
            emailError?.set(app.getString(R.string.error_field_required))
            isNotEmpty = false

        } else {
            val p = Pattern.compile(app.getString(R.string.email_regex))
            val m = p.matcher(email)

            if(!m.find()){
                emailError?.set(app.getString(R.string.error_invalid_email))
                patternMatched = false
            }
        }

        return isNotEmpty && patternMatched

    }

    /**
     * isPasswordValid method checks validity of the password.
     * It checks the length of the password > 8 and whether it
     * contains at least one special character.
     */
    override fun isPasswordValid(): Boolean{

        var errorString = ""

        var passwordLengthFlag = true
        if (password?.length!! < 8){
            errorString =  app.getString(R.string.error_short_password) + "\n"
            passwordLengthFlag = false
        }

        var hasSplChars = false
        for (ch in password!!) {
            if (!Character.isLetterOrDigit(ch)) {
                hasSplChars = true
                break
            }
        }
        if(!hasSplChars)
            errorString += app.getString(R.string.error_invalid_password)

        if(errorString.isNotEmpty())
            passwordError?.set(errorString)

        return passwordLengthFlag && hasSplChars
    }

    /**
     * showhelp method is a onClickListener method for the login_help button
     * which shows a Toast with the LOGIN_HELP_MESSAGE.
     */
    fun showHelp()
    {
        Toast.makeText(app, LOGIN_HELP_MESSAGE, Toast.LENGTH_LONG).show()
    }
}