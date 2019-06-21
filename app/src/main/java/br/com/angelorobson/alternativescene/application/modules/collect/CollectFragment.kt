package br.com.angelorobson.alternativescene.application.modules.collect


import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.modules.collect.di.component.DaggerCollectComponent
import br.com.angelorobson.alternativescene.databinding.CollectFragmentBinding
import br.com.angelorobson.alternativescene.domain.Client
import br.com.angelorobson.alternativescene.domain.request.CollectRequest
import kotlinx.android.synthetic.main.host_navigation_activity.*
import java.util.*
import javax.inject.Inject


class CollectFragment : FragmentBase(), CollectHandler {

    private lateinit var binding: CollectFragmentBinding
    private lateinit var validator: Validator
    private var spinnerDialog: SpinnerDialog? = null
    private var client = Client()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CollectViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CollectViewModel::class.java]
    }

    private var clients = mutableListOf<Client>()

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
        initObserverCreateInvoiceSuccess()
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
        binding.handler = this
        binding.collectRequest = CollectRequest()
    }

    private fun setupValidator() {
        validator = Validator(binding)
        validator.enableFormValidationMode()
    }

    private fun initObserverSuccess() {
        viewModel.successObserver.observe(this, Observer {
            clients = it
            setUpClientsDialog()
        })
    }

    private fun setUpClientsDialog() {
        spinnerDialog = SpinnerDialog(activity, clients.map { it.name } as ArrayList<String>, "Select Client")

        spinnerDialog?.bindOnSpinerListener { _, position ->
            client = clients[position]
            binding.getClientsButton.text = client.name
        }
    }

    private fun initObserverError() {
        viewModel.errorObserver.observe(this, Observer {
            showAlertError(it)
        })
    }

    override fun onPressShowClientsDialog() {
        spinnerDialog?.showSpinerDialog()
    }

    override fun onPressSaveCollect(collectRequest: CollectRequest) {
        if (validator.validate()) {
            if (client.name.isNotBlank()) {
                collectRequest.value = binding.valueEditText.value
                collectRequest.clientUuid = client.authUuid
                collectRequest.authUuid = AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.user?.uuid!!

                viewModel.createInvoice(collectRequest)
            }
        }
    }

    private fun initObserverCreateInvoiceSuccess() {
        viewModel.successCreateInvoiceObserver.observe(this, Observer {
            Toast.makeText(context, "Criou", Toast.LENGTH_LONG).show()
        })
    }

}
