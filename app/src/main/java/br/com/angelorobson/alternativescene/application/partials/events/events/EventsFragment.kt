package br.com.angelorobson.alternativescene.application.partials.events.events


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.EVENT_ID_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.EVENT_IS_FAVORITE_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.FAVORITE_ICON_IS_CLICKED
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventImageConstants.EVENT_IMAGE_URL_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isEqual
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isNotTrue
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventsComponent
import br.com.angelorobson.alternativescene.application.partials.events.event.EventActivity
import br.com.angelorobson.alternativescene.application.partials.events.eventimage.EventImageActivity
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

    private var mEventPosition: Int? = null

    private val userLogeed =
        AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.userAppDto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbarWithoutDisplayArrowBack("")
        setHasOptionsMenu(true)
        setUpElements()
    }

    override fun onResume() {
        super.onResume()


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
        getEvents()
    }

    private fun getEvents() {
        mEvents.clear()
        userLogeed?.apply {
            mViewModel.getEventsByUser(userId = this.id)
        } ?: run {
            mViewModel.getEvents()
        }
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
                userLogeed?.apply {
                    mViewModel.getEventsByUser(currentPage, this.id)
                } ?: run {
                    mViewModel.getEvents(currentPage)
                }
            }
        })
    }

    private fun initSuccessOberserver() {
        mViewModel.successObserver.observe(this, EventObserver {
            it.data?.content?.let { it1 -> mEvents.addAll(it1) }
            mEventsAdapter.notifyDataSetChanged()
        })

        mViewModel.successFavoriteObserver.observe(this, EventObserver {
            mEventPosition?.apply {
                setIconFavoriteOnEventPosition()
            }
        })

        mViewModel.successdisfavourObserver.observe(this, EventObserver {
            mEventPosition?.apply {
                setIconDisfavorOnEventPosition()
            }
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

            userLogeed?.apply {
                mViewModel.getEventsByUser(userId = this.id)
            } ?: run {
                mViewModel.getEvents()
            }
        }
    }

    override fun onPressShare(event: Event) {
        Toast.makeText(requireContext(), "clicou no share", Toast.LENGTH_SHORT).show()
    }

    override fun onPressFavorite(event: Event, position: Int) {
        val authResponse =
            AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.userAppDto

        mEventPosition = position

        authResponse?.apply {
            val favoriteRequest = FavoriteRequest(this.id, event.id)
            mViewModel.favor(favoriteRequest)
        } ?: run {
            showToast("Você precisa está logado para favoritar um evento")
        }
    }

    override fun onPressItem(event: Event, position: Int) {
        mEventPosition = position
        goToDetailScreen(event)
    }

    private fun goToDetailScreen(event: Event) {
        val intent = Intent(requireActivity(), EventActivity::class.java)
        intent.putExtra(EVENT_ID_EXTRA, event.id)
        intent.putExtra(EVENT_IS_FAVORITE_EXTRA, event.favorite)

        startActivityForResult(intent, DETAIL_EVENT_REQUEST_CODE)
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
        val intent = Intent(requireActivity(), EventImageActivity::class.java)
        intent.putExtra(EVENT_IMAGE_URL_EXTRA, event.imageUrl)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode.isEqual(DETAIL_EVENT_REQUEST_CODE)) {
            data?.apply {
                handleFavoriteIcon(this)
            }
        }
    }

    private fun handleFavoriteIcon(intent: Intent) {
        val isEventFavorite = intent.getBooleanExtra(EVENT_IS_FAVORITE_EXTRA, false)
        val isFavoriteIconClicked = intent.getBooleanExtra(FAVORITE_ICON_IS_CLICKED, false)

        if (isFavoriteIconClicked) {
            if (isEventFavorite.isNotTrue()) {
                setIconDisfavorOnEventPosition()
                return
            }
            mEventPosition?.apply { setIconFavoriteOnEventPosition() }
        }
    }

    private fun setIconFavoriteOnEventPosition() {
        mEventPosition?.apply {
            val eventsUpdated = mEvents[this]
            eventsUpdated.favorite = true

            mEvents[this] = eventsUpdated
            mEventsAdapter.notifyItemChanged(this)
        }
    }

    private fun setIconDisfavorOnEventPosition() {
        mEventPosition?.apply {
            val eventsUpdated = mEvents[this]
            eventsUpdated.favorite = false

            mEvents[this] = eventsUpdated
            mEventsAdapter.notifyItemChanged(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }


}
