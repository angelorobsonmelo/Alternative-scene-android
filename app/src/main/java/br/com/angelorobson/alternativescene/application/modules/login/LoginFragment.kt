package br.com.angelorobson.alternativescene.application.modules.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import br.com.ilhasoft.support.validation.Validator
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.modules.login.di.component.DaggerUsersComponent
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.commom.utils.facebookmanager.FBCallbackManager
import br.com.angelorobson.alternativescene.application.commom.utils.facebookmanager.FacebookCallBack
import br.com.angelorobson.alternativescene.application.commom.utils.facebookmanager.UserFacebook
import br.com.angelorobson.alternativescene.databinding.LoginFragmentBinding
import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.host_navigation_activity.*
import javax.inject.Inject

class LoginFragment : FragmentBase(), LoginHandler, FacebookCallBack {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var validator: Validator
    private lateinit var buttonFacebookLogin: LoginButton
    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initElements()
    }

    override fun onResume() {
        super.onResume()
        activity?.bottomNavigation?.visibility = GONE
        activity?.toolbar?.visibility = GONE
    }

    private fun initElements() {
        injectDependency()
        setupFragmentBinding()
        setupValidator()
        initObserveOnSuccess()
        initOserveOnError()
    }

    private fun injectDependency() {
        DaggerUsersComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    private fun setupFragmentBinding() {
        binding.lifecycleOwner = this
        binding.handler = this
        binding.viewModel = viewModel
        binding.authRequest = AuthRequest("admin", "invoiceSoluevo@3")
    }

    private fun setupValidator() {
        validator = Validator(binding)
        validator.enableFormValidationMode()
    }

    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        FBCallbackManager(buttonFacebookLogin, this, callbackManager)
    }

    override fun onPressLoginButton(authRequest: AuthRequest) {
        if (validator.validate()) {
            viewModel.auth(authRequest)
        }
    }

    override fun onPressFacebookButton(view: View) {
        if (view == binding.facebookButtonCustom) {
            binding.progressBar.visibility = VISIBLE

        }
    }

    private fun initObserveOnSuccess() {
        viewModel.successObserver.observe(this, Observer {
            goToMainScreen()
        })
    }

    private fun initOserveOnError() {
        viewModel.errorObserver.observe(this, Observer {
            showAlert(it)
        })
    }

    override fun fbCallbackOnSuccess(userFacebook: UserFacebook) {
        binding.progressBar.visibility = VISIBLE
        goToMainScreen()
    }

    override fun fbCallbackOnError(localizedMessage: String) {
        binding.progressBar.visibility = GONE
        showAlert(localizedMessage)
    }

    override fun fbCallbackOnCancel() {
        binding.progressBar.visibility = GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun goToMainScreen() {
        view?.findNavController()?.navigate(R.id.invoicesFragment)
    }

}