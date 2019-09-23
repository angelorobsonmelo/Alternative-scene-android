package br.com.angelorobson.alternativescene.application.commom.utils.handlers.googleauth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

interface GoogleAuthHandler {

    fun onSuccess(googleSignInAccount: GoogleSignInAccount)
    fun onApiException(apiException: ApiException)
    fun onException(exception: Exception?)
}