package br.com.angelorobson.alternativescene.application.partials.events.event


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.ARG_EVENT
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventComponent
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventsComponent
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsViewModel
import br.com.angelorobson.alternativescene.databinding.EventFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import javax.inject.Inject


class EventFragment : BindingFragment<EventFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_fragment

    private var mEvent: Event? = null

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventViewModel::class.java]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            mEvent = it.getParcelable(ARG_EVENT)
        }

        setUpElements()
    }

    private fun setUpElements() {
        setUpBinding()
        setUpDagger()
        hideBottomNavigation()
        showToolbarWithDisplayArrowBack("")
        getEvent()
        initObservers()
    }

    private fun initObservers() {
        mViewModel.successObserver.observe(this, EventObserver {
          binding.event = it.data
        })
    }

    private fun getEvent() {
        mEvent?.id?.let {
            mViewModel.getEvent(it)
        }
    }

    private fun setUpBinding() {
        binding.lifecycleOwner = this
    }

    private fun setUpDagger() {
        DaggerEventComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposable.clear()
    }


}
