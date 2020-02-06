package br.com.angelorobson.alternativescene


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentComponentGeneric
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.FragmentComponentGeneric
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsViewModel
import br.com.angelorobson.alternativescene.databinding.LoginFragmentBinding
import br.com.ilhasoft.support.validation.Validator
import kotlinx.android.synthetic.admin.login_fragment.*
import javax.inject.Inject


class LoginFragment : BindingFragment<LoginFragmentBinding>() {


    override fun getLayoutResId(): Int = R.layout.login_fragment

    lateinit var mValidator: Validator

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[LoginViewModel::class.java]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUp()
    }

    private fun setUp() {
        hideBottomNavigation()
        hideToolbar()
        setUpDagger()
        setupValidator()
        initClickListener()
        initObservables()
    }

    private fun setUpDagger() {
        DaggerFragmentComponentGeneric
            .builder()
            .contextModule(ContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun setupValidator() {
        mValidator = Validator(binding)
        mValidator.enableFormValidationMode()
    }

    private fun initClickListener() {
        login_button.setOnClickListener {
            if (mValidator.validate()) {
                mViewModel.login(
                    email_ed.text.toString(),
                    password_ed.text.toString()
                )
            }
        }
    }

    private fun initObservables() {
        mViewModel.successObserver.observe(this, EventObserver {
            AlternativeSceneApplication.mSessionUseCase.saveAuthResponseInSession(it)
            findNavController().navigate(R.id.action_loginFragment_to_eventsFragment)
        })

        mViewModel.errorObserver.observe(this, EventObserver {
            showAlertError(it)
        })
    }

}
