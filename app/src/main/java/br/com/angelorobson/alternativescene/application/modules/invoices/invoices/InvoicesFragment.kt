package br.com.angelorobson.alternativescene.application.modules.invoices.invoices


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.modules.invoices.invoices.adapter.InvoicesAdapter
import br.com.angelorobson.alternativescene.application.modules.invoices.invoices.di.component.DaggerInvoicesComponent
import br.com.angelorobson.alternativescene.databinding.InvoicesFragmentBinding
import kotlinx.android.synthetic.main.host_navigation_activity.*
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
        showToolbar()
        showBottomNavigation()
        setUpElements()
    }

    private fun showToolbar() {
        activity?.toolbar?.visibility = View.VISIBLE
        activity?.toolbar?.title = getString(R.string.collects)
    }

    private fun showBottomNavigation() {
        activity?.bottomNavigation?.visibility = View.VISIBLE
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
            showAlertError(it)
        })
    }

}
