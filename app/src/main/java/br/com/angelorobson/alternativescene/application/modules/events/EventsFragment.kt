package br.com.angelorobson.alternativescene.application.modules.events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.modules.events.adapter.EventsAdapter
import br.com.angelorobson.alternativescene.application.modules.events.di.component.DaggerEventsComponent
import br.com.angelorobson.alternativescene.databinding.EventsFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import javax.inject.Inject


class EventsFragment : FragmentBase() {

    private lateinit var binding: EventsFragmentBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: EventsViewModel by lazy {
        ViewModelProviders.of(this, factory)[EventsViewModel::class.java]
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private val events = mutableListOf<Event>()
    private var eventsAdapter = EventsAdapter(events)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.events_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
        viewModel.getEvents()
    }

    private fun setUpElements() {
        setUpDagger()
        setUpDataBinding()
        setUpRecyclerView()
        setUpEndlessScrollListener()
        initSuccessOberserver()
        initErrorOberserver()
        initSwipeToRefreshLayoutEvents()
    }

    private fun setUpDagger() {
        DaggerEventsComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
            .inject(this)
    }

    private fun setUpDataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerViewEvents
        layoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(divider)
        recyclerView.adapter = ScaleInAnimationAdapter(eventsAdapter).apply {
            setFirstOnly(true)
            setDuration(500)
            setInterpolator(OvershootInterpolator(.5f))
        }
    }

    private fun setUpEndlessScrollListener() {
        binding.recyclerViewEvents.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.getEvents(EventFilter(true), currentPage)
            }
        })
    }

    private fun initErrorOberserver() {
        viewModel.errorObserver.observe(this, Observer {
            showAlertError(it)
        })
    }

    private fun initSuccessOberserver() {
        viewModel.successObserver.observe(this, Observer {
            events.addAll(it.data?.content ?: mutableListOf())
            eventsAdapter.notifyDataSetChanged()
        })
    }

    private fun initSwipeToRefreshLayoutEvents() {
       binding.swipeToRefreshLayoutEvents.setOnRefreshListener {
           events.clear()
           eventsAdapter.notifyDataSetChanged()
           viewModel.getEvents()
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearDisposable()
    }


}
