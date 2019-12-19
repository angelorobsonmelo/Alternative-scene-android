package br.com.angelorobson.alternativescene.application.partials.events.favorite


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentGenericWithRecyclerViewAnimatedWithoutDividerComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isEqual
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isNotTrue
import br.com.angelorobson.alternativescene.application.partials.events.event.EventActivity
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsHandler
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity.Companion.GOOGLE_AUTH_REQUEST_CODE
import br.com.angelorobson.alternativescene.databinding.FavoriteFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import javax.inject.Inject


class FavoriteFragment : BindingFragment<FavoriteFragmentBinding>(), EventsHandler {

    override fun getLayoutResId(): Int = R.layout.favorite_fragment

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: FavoriteViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[FavoriteViewModel::class.java]
    }

    private val mEvents = mutableListOf<Event>()

    private var mEventPosition: Int? = null

    private val userLogeed =
        AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.userAppDto

    private var mEventsAdapter =
        FavoriteAdapter(mEvents, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
    }

    private fun setUpScreen() {
        setUpDagger()
        setUpDataBinding()
        checkIfUserIsLogged()
        setUpEndlessScrollListener()
        initSuccessOberserver()
        initErrorObserver()
        initSwipeToRefreshLayoutEvents()
        showToolbarWithoutDisplayArrowBack(getString(R.string.favorites))
    }


    private fun checkIfUserIsLogged() {
        val isUserLogged =
            AlternativeSceneApplication.mSessionUseCase.isLogged()

        if (isUserLogged) {
            getEvents()
            return
        }

        goToSignInActivity()
    }

    private fun getEvents() {
        mEvents.clear()

        mViewModel.getFavorsEventsByUser(userId = 4)
    }

    private fun setUpDagger() {
        DaggerFragmentGenericWithRecyclerViewAnimatedWithoutDividerComponent.builder()
            .contextModule(ContextModule(requireContext()))
            .recyclerViewAnimatedModule(
                RecyclerViewAnimatedModule(
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


    private fun goToSignInActivity() {
        startActivityForResult(
            Intent(requireContext(), SignInActivity::class.java),
            GOOGLE_AUTH_REQUEST_CODE
        )
    }

    private fun initSuccessOberserver() {
        mViewModel.successObserver.observe(this, EventObserver {
            it.data?.content?.let { it1 -> mEvents.addAll(it1) }
            mEventsAdapter.notifyDataSetChanged()
        })


        mViewModel.emptyObserver.observe(this, EventObserver {
            if (mEvents.isEmpty()) {
                binding.noEventTextView.visibility = VISIBLE
            }
        })

        mViewModel.successdisfavourObserver.observe(this, EventObserver {
            mEventPosition?.apply {
                val eventsToRemove = mEvents[this]

                mEvents.remove(eventsToRemove)
                mEventsAdapter.notifyItemRemoved(this)
                mEventsAdapter.notifyDataSetChanged()

                if (mEvents.isEmpty()) {
                    binding.noEventTextView.visibility = VISIBLE
                }
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
                mViewModel.getFavorsEventsByUser(userId = this.id)
            }
        }
    }


    private fun setUpEndlessScrollListener() {
        mRecyclerView.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(mLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                userLogeed?.apply {
                    mViewModel.getFavorsEventsByUser(currentPage, this.id)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isSuccess(requestCode, resultCode)) {
            print("ffff")
        }

        if (resultCode.isEqual(Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE)) {
            data?.apply {
                handleFavoriteIcon(this)
            }
        }
    }

    private fun handleFavoriteIcon(intent: Intent) {
        val isEventFavorite =
            intent.getBooleanExtra(Constants.EventsContants.EVENT_IS_FAVORITE_EXTRA, false)
        val isFavoriteIconClicked =
            intent.getBooleanExtra(Constants.EventsContants.FAVORITE_ICON_IS_CLICKED, false)

        if (isFavoriteIconClicked) {
            if (isEventFavorite.isNotTrue()) {
                mEventPosition?.let {
                    val eventsToRemove = mEvents[it]

                    mEvents.remove(eventsToRemove)
                    mEventsAdapter.notifyItemRemoved(it)
                    mEventsAdapter.notifyDataSetChanged()
                }

                if (mEvents.isEmpty()) {
                    binding.noEventTextView.visibility = VISIBLE
                }
                return
            }
        }
    }

    private fun isSuccess(requestCode: Int, resultCode: Int): Boolean {
        return requestCode.isEqual(GOOGLE_AUTH_REQUEST_CODE) && resultCode.isEqual(
            Activity.RESULT_OK
        )
    }

    override fun onPressShare(event: Event) {
        Toast.makeText(requireContext(), "clicou no share", Toast.LENGTH_SHORT).show()
    }

    override fun onPressFavorite(event: Event, position: Int) {
        mEventPosition = position
        val favoriteRequest = FavoriteRequest(4, event.id)
        mViewModel.favor(favoriteRequest)
    }

    override fun onPressItem(event: Event, position: Int) {
        mEventPosition = position
        goToDetailScreen(event)
    }

    private fun goToDetailScreen(event: Event) {
        val intent = Intent(requireActivity(), EventActivity::class.java)
        intent.putExtra(Constants.EventsContants.EVENT_ID_EXTRA, event.id)
        intent.putExtra(Constants.EventsContants.EVENT_IS_FAVORITE_EXTRA, event.favorite)

        startActivityForResult(intent, Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE)
    }

    override fun onLongPressImage(event: Event) {

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }

}
