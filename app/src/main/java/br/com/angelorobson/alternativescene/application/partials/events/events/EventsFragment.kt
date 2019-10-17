package br.com.angelorobson.alternativescene.application.partials.events.events


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.ARG_EVENT
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventsComponent
import br.com.angelorobson.alternativescene.application.partials.events.events.adapter.EventsAdapter
import br.com.angelorobson.alternativescene.databinding.EventsFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import javax.inject.Inject


class EventsFragment : BindingFragment<EventsFragmentBinding>(), EventsHandler {


    override fun getLayoutResId(): Int = R.layout.events_fragment

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventsViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventsViewModel::class.java]
    }

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

    private val mEvents = mutableListOf<Event>()
    private var mEventsAdapter =
        EventsAdapter(mEvents, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbarWithoutDisplayArrowBack("")
        setHasOptionsMenu(true)
        setUpElements()
    }

    override fun onResume() {
        super.onResume()
        mEvents.clear()
        mViewModel.getEvents()
        showBottomNavigation()
    }

    private fun setUpElements() {
        setUpDagger()
        setUpDataBinding()
        setUpEndlessScrollListener()
        initSuccessOberserver()
        initErrorObserver()
        initSwipeToRefreshLayoutEvents()
        showToolbarWithoutDisplayArrowBack(getString(R.string.events))
    }

    private fun setUpDagger() {
        DaggerEventsComponent.builder()
            .contextModule(ContextModule(context!!))
            .recyclerViewAnimatedWithDividerModule(
                RecyclerViewAnimatedWithDividerModule(
                    binding.recyclerViewEvents,
                    mEventsAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
                )
            )
            .build()
            .inject(this)
    }

    private fun setUpDataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
    }

    private fun setUpEndlessScrollListener() {
        mRecyclerView.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                mViewModel.getEvents(currentPage)
            }
        })
    }

    private fun initSuccessOberserver() {
        mViewModel.successObserver.observe(this, EventObserver {
            it.data?.content?.let { it1 -> mEvents.addAll(it1) }
            mEventsAdapter.notifyDataSetChanged()
        })

        mViewModel.successFavoriteObserver.observe(this, EventObserver {
            showToast("Evento favoritado")
        })

        mViewModel.successdisfavourObserver.observe(this, EventObserver {
            showToast("Evento desfavoritado")
        })
    }

    private fun initErrorObserver() {
        mViewModel.errorObserver.observe(this, EventObserver {
            showAlertError(it)
        })
    }

    private fun initSwipeToRefreshLayoutEvents() {
        binding.swipeToRefreshLayoutEvents.setOnRefreshListener {
            mEvents.clear()
            mEventsAdapter.notifyDataSetChanged()
            mViewModel.getEvents()
        }
    }

    override fun onPressShare(event: Event) {
        Toast.makeText(requireContext(), "clicou no share", Toast.LENGTH_SHORT).show()
    }

    override fun onPressFavorite(event: Event) {
        val authResponse =
            AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()

        authResponse?.apply {
            val favoriteRequest = FavoriteRequest(this.userAppDto.id, event.id)
            mViewModel.favor(favoriteRequest)
        } ?: run {
            showToast("Você precisa está logado para favoritar um evento")
        }
    }

    override fun onPressItem(event: Event) {
        goToDetailScreen(event)
    }

    private fun goToDetailScreen(event: Event) {
        val args = Bundle()
        args.putParcelable(ARG_EVENT, event)
        findNavController().navigate(
            R.id.action_eventsFragment_to_eventFragment,
            args
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_events, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_event -> {
                Toast.makeText(requireContext(), "Share", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onLongPressImage(event: Event) {
        val args = Bundle()
        args.putParcelable(ARG_EVENT, event)
        findNavController().navigate(
            R.id.action_eventsFragment_to_eventImageFragment,
            args
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }


}
