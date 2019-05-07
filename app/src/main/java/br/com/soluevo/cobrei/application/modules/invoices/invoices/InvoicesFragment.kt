package br.com.soluevo.cobrei.application.modules.invoices.invoices


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.commom.utils.FragmentBase
import br.com.soluevo.cobrei.application.modules.invoices.invoices.adapter.InvoicesAdapter
import br.com.soluevo.cobrei.application.modules.invoices.invoices.di.component.DaggerInvoicesComponent
import br.com.soluevo.cobrei.databinding.InvoicesFragmentBinding
import javax.inject.Inject

class InvoicesFragment : FragmentBase() {

    private lateinit var binding: InvoicesFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InvoicesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[InvoicesViewModel::class.java]
    }

    private var invoicesAdapter = InvoicesAdapter(
        mutableListOf()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.invoices_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
    }

    private fun setUpElements() {
        injectDependency()
        setUpDataBinding()
        setupRecyclerView()
        initObserverSuccess()
        initObserverError()
    }

    private fun injectDependency() {
        DaggerInvoicesComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    private fun setUpDataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.invoicesRecyclerView
        val layoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(divider)
        recyclerView.adapter = invoicesAdapter
    }

    private fun initObserverSuccess() {
        viewModel.successObserver.observe(this, Observer {
            invoicesAdapter.updateItems(it)
        })
    }

    private fun initObserverError() {
        viewModel.errorObserver.observe(this, Observer {
            showAlert(it)
        })
    }

}
