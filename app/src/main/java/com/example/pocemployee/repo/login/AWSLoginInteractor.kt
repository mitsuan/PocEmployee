package com.example.pocemployee.repo.login

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobileconnectors.cognitoauth.Auth
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession
import com.amazonaws.mobileconnectors.cognitoauth.handlers.AuthHandler
import com.example.pocemployee.R

class AWSLoginInteractor(val app: Application){

    val TAG = AWSLoginInteractor::class.java.simpleName
    var auth: Auth? = null
    var appRedirect: Uri? = null

    val loginSuccessNotifier =  MutableLiveData<Boolean>()
    val logoutSuccessNotifier =  MutableLiveData<Boolean>()

    init{
        initCognito()
    }

    private fun initCognito() {
        val builder: Auth.Builder = Auth.Builder().setAppClientId(app.getString(R.string.cognito_client_id))
            .setAppClientSecret(app.getString(R.string.cognito_client_secret))
            .setAppCognitoWebDomain(app.getString(R.string.cognito_web_domain))
            .setApplicationContext(app)
            .setAuthHandler(Callback())
            .setSignInRedirect(app.getString(R.string.app_redirect))
            .setSignOutRedirect(app.getString(R.string.app_redirect))

        auth = builder.build()
        appRedirect = Uri.parse(app.getString(R.string.app_redirect))

    }

    /**
     * Callback handler for Amazon Cognito.
     */
    inner class Callback : AuthHandler {

        override fun onSuccess(authUserSession: AuthUserSession) {
            Log.d(TAG, "success callback")
            loginSuccessNotifier.value = true
            logoutSuccessNotifier.value = false
            Log.d(TAG, "access token: $authUserSession.accessToken")
            Log.d(TAG, "id token: $authUserSession.idToken")
            Log.d(TAG, "isValid : ${authUserSession.isValid}")

        }

        override fun onSignout() {
            Log.d(TAG, "signout callback")
            logoutSuccessNotifier.value = true
            loginSuccessNotifier.value = false
        }

        override fun onFailure(e: Exception) {
            Log.e(TAG, "Failed to auth", e)
        }
    }



}