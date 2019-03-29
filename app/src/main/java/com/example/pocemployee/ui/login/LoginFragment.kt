package com.example.pocemployee.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pocemployee.R

class LoginFragment(): Fragment() {

    override fun onCreateView(layoutInflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view = layoutInflater.inflate(R.layout.fragment_login, container, false)
        return view
    }



}