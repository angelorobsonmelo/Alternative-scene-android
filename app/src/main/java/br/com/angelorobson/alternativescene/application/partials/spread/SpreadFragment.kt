package br.com.angelorobson.alternativescene.application.partials.spread


import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerView
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.handlers.googleauth.GoogleAuthHandler
import br.com.angelorobson.alternativescene.application.partials.events.event.EventViewModel
import br.com.angelorobson.alternativescene.application.partials.spread.di.component.DaggerSpreadComponent
import br.com.angelorobson.alternativescene.databinding.SpreadFragmentBinding
import br.com.angelorobson.alternativescene.domain.request.UserRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject


class SpreadFragment : BindingFragment<SpreadFragmentBinding>() {

    companion object {
        const val GOOGLE_AUTH_REQUEST_CODE = 23
    }

    private var mGoogleSignInAccount: GoogleSignInAccount? = null

    override fun getLayoutResId(): Int = R.layout.spread_fragment

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: SpreadViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[SpreadViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbarWithoutDisplayArrowBack(getString(R.string.spread_event))
        setUpFragment()
    }

    private fun setUpFragment() {
        setUpDagger()
        setUpGoogleLogin()
        signUpListener()
        initObservers()
    }

    private fun setUpDagger() {
        DaggerSpreadComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    private fun setUpGoogleLogin() {
        setUpGoogleAuth(object : GoogleAuthHandler {
            override fun onSuccess(googleSignInAccount: GoogleSignInAccount) {
                mGoogleSignInAccount = googleSignInAccount
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

    private fun signUpListener() {
        binding.signInButton.setOnClickListener {
            openSignIntent()
        }
    }

    private fun initObservers() {
        mViewModel.userNotFoundObserver.observe(this, EventObserver {
            val user = UserRequest(
                mGoogleSignInAccount?.email!!,
                mGoogleSignInAccount?.id!!,
                mGoogleSignInAccount?.id!!,
                "",
                mGoogleSignInAccount?.displayName!!
            )

            mViewModel.save(user)
        })

        mViewModel.successObserver.observe(this, EventObserver {
            navigateToEventForm()
        })
    }

    override fun onStart() {
        super.onStart()
        auth.signOut()
        auth.currentUser?.apply {
            navigateToEventForm()
        }
    }

    private fun navigateToEventForm() {
        findNavController().navigate(
            R.id.action_spreadFragment_to_eventFormFragment
        )
    }

}

