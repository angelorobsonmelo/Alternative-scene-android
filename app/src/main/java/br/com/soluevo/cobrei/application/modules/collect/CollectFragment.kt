package br.com.soluevo.cobrei.application.modules.collect


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.commom.utils.FragmentBase
import br.com.soluevo.cobrei.application.modules.collect.di.component.DaggerCollectComponent
import br.com.soluevo.cobrei.databinding.CollectFragmentBinding
import kotlinx.android.synthetic.main.host_navigation_activity.*
import javax.inject.Inject


class CollectFragment : FragmentBase() {

    private lateinit var binding: CollectFragmentBinding
    private lateinit var validator: Validator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CollectViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CollectViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.collect_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpElements()
    }

    private fun setUpElements() {
        setUpToolbar()
        injectDependency()
        setUpDataBinding()
        setupValidator()
        initObserverSuccess()
        initObserverError()
    }

    private fun setUpToolbar() {
        activity?.toolbar?.title = getString(R.string.collect)
    }

    private fun injectDependency() {
        DaggerCollectComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    private fun setUpDataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupValidator() {
        validator = Validator(binding)
        validator.enableFormValidationMode()
    }

    private fun initObserverSuccess() {
        viewModel.successObserver.observe(this, Observer {

        })
    }

    private fun initObserverError() {
        viewModel.errorObserver.observe(this, Observer {
            showAlert(it)
        })
    }


}
