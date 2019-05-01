package br.com.soluevo.cobrei.application.modules.login


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import br.com.ilhasoft.support.validation.Validator
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.injections.InjectionUseCase
import br.com.soluevo.cobrei.application.utils.FragmentBase
import br.com.soluevo.cobrei.databinding.LoginFragmentBinding
import br.com.soluevo.cobrei.domain.request.AuthRequest
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : FragmentBase(), LoginHandler {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this,
            LoginViewModel(InjectionUseCase.provideAuthseCase())).get(LoginViewModel::class.java)
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var validator: Validator

    companion object {
        fun newInstance() = LoginFragment()
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

    private fun initElements() {
        setupFragmentBinding()
        setupValidator()
        initObserveOnSuccess()
        initOserveOnError()
    }

    private fun setupFragmentBinding() {
        binding.lifecycleOwner = this
        binding.handler        = this
        binding.viewModel      = viewModel
        binding.authRequest    = AuthRequest()
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

    private fun goToMainScreen() {
        view?.findNavController()?.navigate(R.id.toAccount)
    }

    private fun initOserveOnError() {
        viewModel.errorObserver.observe(this, Observer {
            showAlert(it)
        })
    }


}
