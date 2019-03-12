package com.example.pocemployee.ui.login

import android.widget.EditText

interface ILogin {
    interface ViewModel{
        fun attemptLogin(email: EditText, password: EditText): Boolean
        fun isEmailValid(): Boolean
        fun isPasswordValid(): Boolean
    }

    interface View{
        fun attemptLogin()
    }
}