package com.example.pocemployee.ui.login

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import com.example.pocemployee.R
import java.util.regex.Pattern

/**
 * LoginViewModel is the ViewModel class of the View class LoginActivity.
 * It performs the login attempt along with validation checks.
 */
class LoginViewModel(val app: Application): AndroidViewModel(app), ILogin.ViewModel{

    private var email: EditText? = null
    private var password: EditText? = null

    /**
     * Attempts to log in based on credentials specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no login attempt is made.
     */
    override fun attemptLogin(email: EditText, password: EditText): Boolean {

        this.email = email
        this.password = password

        var isValid = false

        var cancel = false
        var focusView: View? = null

        if (!isPasswordValid()) {
            focusView = password
            cancel = true
        }

        if (!isEmailValid()) {
            focusView = email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            isValid =  true
        }

        return isValid
    }


    /**
     * isEmailValid method is used to check the validity of the email
     * It check whether the field is empty and also uses a regex to validate email format
     * and shows a validation error based on the error.
     */
    override fun isEmailValid(): Boolean{
        val emailStr: String =  email?.text.toString()

        var isNotEmpty = true
        var patternMatched = true

        if (TextUtils.isEmpty(emailStr)) {
            email?.error = app.getString(R.string.error_field_required)
            isNotEmpty = false

        } else {
            val p = Pattern.compile(app.getString(R.string.email_regex))
            val m = p.matcher(emailStr)

            if(!m.find()){
                email?.error = app.getString(R.string.error_invalid_email)
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

        val passwordStr : String = password?.text.toString()
        var errorString = ""

        var passwordLengthFlag = true
        if (passwordStr.length < 8){
            errorString =  app.getString(R.string.error_short_password) + "\n"
            passwordLengthFlag = false
        }

        var hasSplChars = false
        for (ch in passwordStr) {
            if (!Character.isLetterOrDigit(ch)) {
                hasSplChars = true
                break
            }
        }
        if(!hasSplChars)
            errorString += app.getString(R.string.error_invalid_password)

        if(errorString.isNotEmpty())
            password?.error = errorString

        return passwordLengthFlag && hasSplChars
    }
}