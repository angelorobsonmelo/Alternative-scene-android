package br.com.soluevo.cobrei.application.modules.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import br.com.ilhasoft.support.validation.Validator
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.CobreiApplication
import br.com.soluevo.cobrei.application.commom.di.modules.application.ApplicationModule
import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.modules.login.di.component.DaggerUsersComponent
import br.com.soluevo.cobrei.application.utils.FragmentBase
import br.com.soluevo.cobrei.databinding.LoginFragmentBinding
import br.com.soluevo.cobrei.domain.request.AuthRequest
import javax.inject.Inject

class LoginFragment : FragmentBase(), LoginHandler {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var validator: Validator

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
        checkIfUserIsLogged()
        initElements()
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
        binding.authRequest = AuthRequest()
    }

    private fun setupValidator() {
        validator = Validator(binding)
        validator.enableFormValidationMode()
    }

    override fun auth(authRequest: AuthRequest) {
        if (validator.validate()) {
            viewModel.auth(authRequest)
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


}
