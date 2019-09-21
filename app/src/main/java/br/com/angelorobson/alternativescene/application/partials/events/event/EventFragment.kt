package br.com.angelorobson.alternativescene.application.partials.events.event


import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerView
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.ARG_EVENT
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventComponent
import br.com.angelorobson.alternativescene.application.partials.events.event.dapter.EventDateAdapter
import br.com.angelorobson.alternativescene.databinding.EventFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.EventDate
import javax.inject.Inject


class EventFragment : BindingFragment<EventFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_fragment

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

    private var mEvent: Event? = null

    private val mEventDates = ArrayList<EventDate>()

    private var mEventDateAdapter =
        EventDateAdapter(mEventDates)

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

    private fun getEvent() {
        mEvent?.id?.let {
            mViewModel.getEvent(it)
        }
    }

    private fun initObservers() {
        mViewModel.successObserver.observe(this, EventObserver {
            binding.event = it.data
            it.data?.eventDates?.apply {
                mEventDateAdapter.updateItems(this)
            }
        })
    }


    private fun setUpBinding() {
        binding.lifecycleOwner = this
    }

    private fun setUpDagger() {
        DaggerEventComponent.builder()
            .contextModule(ContextModule(context!!))
            .simpleRecyclerView(
                SimpleRecyclerView(
                    binding.recyclerViewDates,
                    mEventDateAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
                )
            )
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposable.clear()
    }


}
