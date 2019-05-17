package br.com.soluevo.cobrei.application.modules.collect


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.commom.utils.FragmentBase
import br.com.soluevo.cobrei.application.modules.collect.di.component.DaggerCollectComponent
import javax.inject.Inject


class CollectFragment : FragmentBase() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CollectViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CollectViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_collect, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependency()
        initObserverSuccess()
        initObserverError()
    }

    private fun injectDependency() {
        DaggerCollectComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
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
