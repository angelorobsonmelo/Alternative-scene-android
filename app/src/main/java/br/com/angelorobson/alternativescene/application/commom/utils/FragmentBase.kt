package br.com.angelorobson.alternativescene.application.commom.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.handlers.googleauth.GoogleAuthHandler
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.dialog.ListenerConfirmDialog
import br.com.angelorobson.alternativescene.application.partials.spread.SpreadFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.host_navigation_activity.*
import java.lang.ref.WeakReference


open class FragmentBase : Fragment() {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleAuthHandler: GoogleAuthHandler? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBehavorBottomNavigation()
    }

    private fun setUpBehavorBottomNavigation() {
        val bottomNavigationView = activity?.bottomNavigation
        val layoutParams = bottomNavigationView?.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()
    }

    fun showAlertError(message: String) {
        val builder = AlertDialog.Builder(context!!)

        builder
            .setMessage(message.toInt())
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> }

        val alert = builder.create()
        alert.show()
    }

    fun showConfirmDialog(title: String, message: String, dialogListener: ListenerConfirmDialog) {
        val builder = AlertDialog.Builder(context!!)

        builder
            .setMessage(message.toInt())
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialogListener.onPressPositiveButton(dialog, id)
            }
        builder.setNegativeButton(R.string.close) { dialog, id ->
            dialogListener.onPressNegativeButton(dialog, id)
        }

        val alert = builder.create()
        alert.show()
    }

    fun hideBottomNavigation() {
        activity?.bottomNavigation?.visibility = View.GONE
    }

    fun showBottomNavigation() {
        activity?.bottomNavigation?.visibility = View.VISIBLE
    }

    fun hideToolbar() {
        activity?.toolbar?.visibility = View.GONE
    }


    fun showToolbarWithDisplayArrowBack(title: String) {
        val toolbar = activity?.toolbar
        toolbar?.visibility = View.VISIBLE
        toolbar?.title = title

        val appCompatActivity = activity as AppCompatActivity?

        appCompatActivity?.setSupportActionBar(toolbar)
        appCompatActivity?.supportActionBar?.setDisplayShowTitleEnabled(true)
        appCompatActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showToolbarWithoutDisplayArrowBack(title: String) {
        val toolbar = activity?.toolbar
        toolbar?.visibility = View.VISIBLE
        toolbar?.title = title

        val appCompatActivity = activity as AppCompatActivity?

        appCompatActivity?.setSupportActionBar(toolbar)
        appCompatActivity?.supportActionBar?.setDisplayShowTitleEnabled(true)
        appCompatActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    fun setUpGoogleAuth(googleAuthHandler: GoogleAuthHandler) {
        this.googleAuthHandler = googleAuthHandler

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(this.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(
                Scope("profile")
            )
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
    }

    fun openSignIntent() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, SpreadFragment.GOOGLE_AUTH_REQUEST_CODE)
    }

    fun signOutGoogle() {
        mGoogleSignInClient?.signOut()
    }

    fun signOutFireBase() {
        auth.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SpreadFragment.GOOGLE_AUTH_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it)
                }

            } catch (e: ApiException) {
                googleAuthHandler?.onApiException(e)
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                googleAuthHandler?.onSuccess(acct)
                return@addOnCompleteListener
            }

            googleAuthHandler?.onException(it.exception)
            Log.w(ContentValues.TAG, "signInWithCredential:failure", it.exception)
        }
    }


}