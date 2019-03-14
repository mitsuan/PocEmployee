package com.example.pocemployee.ui.login

import androidx.lifecycle.MutableLiveData

interface ILogin {
    interface ViewModel{
        fun attemptLogin()
        fun isEmailValid(): Boolean
        fun isPasswordValid(): Boolean
        val loginSuccessNotifier: MutableLiveData<Boolean>
    }

    interface View{
        fun attemptLogin()
    }
}