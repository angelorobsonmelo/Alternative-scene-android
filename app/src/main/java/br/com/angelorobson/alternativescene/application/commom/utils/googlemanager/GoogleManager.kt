package br.com.angelorobson.alternativescene.application.commom.utils.googlemanager

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.handlers.googleauth.GoogleAuthHandler
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference

class GoogleManager(
    private val activity: WeakReference<Activity>,
    private val googleAuthHandler: GoogleAuthHandler
) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mGoogleSignInClient: GoogleSignInClient? = null


    init {
        activity.get()?.apply {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(
                    Scope("profile")
                )
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }

    }

    fun openSignIntent() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        activity.get()
            ?.startActivityForResult(signInIntent, SignInActivity.GOOGLE_AUTH_REQUEST_CODE)
    }


    fun signOutGoogle() {
        mGoogleSignInClient?.signOut()
    }

    fun signOutFireBase() {
        auth.signOut()
    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                googleAuthHandler.onSuccess(acct)
                return@addOnCompleteListener
            }

            googleAuthHandler.onException(it.exception)
            Log.w(ContentValues.TAG, "signInWithCredential:failure", it.exception)
        }
    }
}