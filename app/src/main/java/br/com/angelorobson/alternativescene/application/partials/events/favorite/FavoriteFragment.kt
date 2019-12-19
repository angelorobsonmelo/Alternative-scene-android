package br.com.angelorobson.alternativescene.application.partials.events.favorite


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentGenericWithRecyclerViewAnimatedWithoutDividerComponent
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentGenericWithRecyclerViewComponent
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.FragmentGenericWithRecyclerViewAnimatedWithoutDividerComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isEqual
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsHandler
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity.Companion.GOOGLE_AUTH_REQUEST_CODE
import br.com.angelorobson.alternativescene.databinding.FavoriteFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
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

    }

    private fun isSuccess(requestCode: Int, resultCode: Int): Boolean {
        return requestCode.isEqual(GOOGLE_AUTH_REQUEST_CODE) && resultCode.isEqual(
            Activity.RESULT_OK
        )
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

    override fun onPressShare(event: Event) {

    }

    override fun onPressFavorite(event: Event, position: Int) {

    }

    override fun onPressItem(event: Event, position: Int) {

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
