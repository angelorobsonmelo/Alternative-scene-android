package br.com.angelorobson.alternativescene.application.modules.events.events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
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
import br.com.angelorobson.alternativescene.application.commom.utils.RecyclerItemClickListener
import br.com.angelorobson.alternativescene.application.modules.events.events.adapter.EventsAdapter
import br.com.angelorobson.alternativescene.application.modules.events.events.di.component.DaggerEventsComponent
import br.com.angelorobson.alternativescene.databinding.EventsFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import javax.inject.Inject


class EventsFragment : FragmentBase() {

    private lateinit var mBinding: EventsFragmentBinding

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventsViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventsViewModel::class.java]
    }

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView

    private val mEvents = mutableListOf<Event>()
    private var mEventsAdapter =
        EventsAdapter(mEvents)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.events_fragment, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
        mViewModel.getEvents()
    }

    private fun setUpElements() {
        setUpDagger()
        setUpDataBinding()
        setUpRecyclerView()
        setUpEndlessScrollListener()
        initRecyclerViewClickListener()
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
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
    }

    private fun setUpRecyclerView() {
        mRecyclerView = mBinding.recyclerViewEvents
        mLayoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(
            mRecyclerView.context,
            mLayoutManager.orientation
        )

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.addItemDecoration(divider)
        mRecyclerView.adapter = ScaleInAnimationAdapter(mEventsAdapter).apply {
            setFirstOnly(true)
            setDuration(500)
            setInterpolator(OvershootInterpolator(.5f))
        }
    }

    private fun setUpEndlessScrollListener() {
        mRecyclerView.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                mViewModel.getEvents(EventFilter(true), currentPage)
            }
        })
    }

    private fun initRecyclerViewClickListener() {
        mRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context!!,
                mRecyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val event = mEvents[position]
                    }

                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                }
            )
        )
    }

    private fun initErrorOberserver() {
        mViewModel.errorObserver.observe(this, Observer {
            showAlertError(it)
        })
    }

    private fun initSuccessOberserver() {
        mViewModel.successObserver.observe(this, Observer {
            mEvents.addAll(it.data?.content ?: mutableListOf())
            mEventsAdapter.notifyDataSetChanged()
        })
    }

    private fun initSwipeToRefreshLayoutEvents() {
        mBinding.swipeToRefreshLayoutEvents.setOnRefreshListener {
            mEvents.clear()
            mEventsAdapter.notifyDataSetChanged()
            mViewModel.getEvents()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.clearDisposable()
    }


}
