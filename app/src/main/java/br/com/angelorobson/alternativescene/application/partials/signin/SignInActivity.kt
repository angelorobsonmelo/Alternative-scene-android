package br.com.angelorobson.alternativescene.application.partials.signin


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication.Companion.mSessionUseCase
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.NavigationHostActivity
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerActivityComponentGeneric
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.googlemanager.GoogleManager
import br.com.angelorobson.alternativescene.application.commom.utils.handlers.googleauth.GoogleAuthHandler
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.BindingActivity
import br.com.angelorobson.alternativescene.databinding.SiginActivityBinding
import br.com.angelorobson.alternativescene.domain.request.UserRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.sigin_activity.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class SignInActivity : BindingActivity<SiginActivityBinding>() {

    companion object {
        const val GOOGLE_AUTH_REQUEST_CODE = 23
    }

    private var mGoogleSignInAccount: GoogleSignInAccount? = null
    private var mGoogleManager: GoogleManager? = null

    override fun getLayoutResId(): Int = R.layout.sigin_activity

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: SignInViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[SignInViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToolbarWithArrowBack(toolbar, getString(R.string.signup))
        setUp()
    }

    private fun setUp() {
        setUpDagger()
        initGoogleManager()
        siginClickListener()
        initObservers()
    }

    private fun siginClickListener() {
        sign_in_button.setOnClickListener {
            this.mGoogleManager?.openSignIntent()
        }
    }

    private fun initGoogleManager() {
        mGoogleManager = GoogleManager(WeakReference(this), object : GoogleAuthHandler {
            override fun onSuccess(googleSignInAccount: GoogleSignInAccount) {
                mViewModel.getUserByEmailAndGoogleAccountId(
                    googleSignInAccount.email!!,
                    googleSignInAccount.id!!
                )

            }

            override fun onApiException(apiException: ApiException) {

            }

            override fun onException(exception: Exception?) {

            }

        })
    }

    private fun setUpDagger() {
        DaggerActivityComponentGeneric.builder()
            .contextModule(ContextModule(this))
            .build()
            .inject(this)
    }

    private fun initObservers() {
        mViewModel.userNotFoundObserver.observe(this, EventObserver {
            val user = UserRequest(
                mGoogleSignInAccount?.email!!,
                mGoogleSignInAccount?.id!!,
                mGoogleSignInAccount?.id!!,
                mGoogleSignInAccount?.photoUrl.toString(),
                mGoogleSignInAccount?.displayName!!
            )

            mViewModel.save(user)
        })

        mViewModel.successObserver.observe(this, EventObserver {
            mSessionUseCase.saveAuthResponseInSession(it)
            finishActivity()
        })
    }

    override fun onStart() {
        super.onStart()
        val userLogged = mSessionUseCase.isLogged()
        if (userLogged) {
            finishActivity()
        }

    }

    private fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_AUTH_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    mGoogleManager?.firebaseAuthWithGoogle(it)
                }

            } catch (e: ApiException) {
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, NavigationHostActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

}

