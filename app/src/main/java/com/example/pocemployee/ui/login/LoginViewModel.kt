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
import com.example.pocemployee.repo.login.AWSLoginInteractor
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.regex.Pattern

/**
 * LoginViewModel is the ViewModel class of the View class [AWSLoginActivity].
 * It performs the login attempt along with validation checks.
 */
class LoginViewModel(val app: Application): AndroidViewModel(app), ILogin.ViewModel, KoinComponent{

    val TAG = LoginViewModel::class.java.simpleName

    var email: String? = ""
    var password: String? = ""
    var emailError: ObservableField<String>? = ObservableField()
    var passwordError: ObservableField<String>? = ObservableField()
    var helpBackground: ObservableField<Boolean>? = ObservableField()
    var loginProgressVisibility : ObservableField<Int> = ObservableField()
    val awsLoginInteractor: AWSLoginInteractor by inject()
    override val loginSuccessNotifier  = MutableLiveData<Boolean>()

    init{
        loginProgressVisibility.set(View.GONE)
    }

    private val LOGIN_HELP_MESSAGE = app.getString(R.string.login_help_message)
    /**
     * Attempts to log in based on credentials specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no login attempt is made.
     */
    override fun attemptLogin(){
        var isValid: Boolean = isPasswordValid()
        isValid = isEmailValid() && isValid

        if(isValid)
        {
            loginSuccessNotifier.value = isValid
            awsLoginInteractor.logoutSuccessNotifier.value = false
            loginProgressVisibility.set(View.VISIBLE)
        }
        else
        {
             helpBackground?.set(true)
        }
    }

    fun awsCognitoLogin()
    {
        loginProgressVisibility.set(View.VISIBLE)
        awsLoginInteractor.auth?.getSession()
    }


    /**
     * isEmailValid method is used to check the validity of the email
     * It check whether the field is empty and also uses a regex to validate email format
     * and shows a validation error based on the error.
     */
    override fun isEmailValid(): Boolean{

        Log.d("login view model: ","email: $email")

        var isNotEmpty = true
        var patternMatched = true

        if (TextUtils.isEmpty(email)) {
            emailError?.set(app.getString(R.string.error_field_required))
            emailError?.notifyChange()
            isNotEmpty = false

        }
        else{
            val p = Pattern.compile(app.getString(R.string.email_regex))
            val m = p.matcher(email)

            patternMatched = m.find()
            Log.d("loginViewModel:if","pattern( ${app.getString(R.string.email_regex)} ) matched: $patternMatched")
            if(!patternMatched){
                emailError?.set(app.getString(R.string.error_invalid_email))
                emailError?.notifyChange()
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

        Log.d("login view model: ","password: $password")

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

        if(errorString.isNotEmpty() && errorString.isNotEmpty())
            passwordError?.set(errorString)
            passwordError?.notifyChange()

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