package br.com.angelorobson.alternativescene.application.partials.events.event


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerView
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.ARG_EVENT
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventComponent
import br.com.angelorobson.alternativescene.application.partials.events.event.dapter.EventDateAdapter
import br.com.angelorobson.alternativescene.databinding.EventFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.EventDate
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import kotlinx.android.synthetic.main.event_fragment.*
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

    private var mMenuItemFavorite: MenuItem? = null

    private val userLooged =
        AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.userAppDto

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

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
        initImageClickListener()
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

        mViewModel.successFavoriteObserver.observe(this, EventObserver {
            binding.event?.favorite = true
            mMenuItemFavorite?.setIcon(R.drawable.ic_favorite_fiiled_red_24dp)
        })

        mViewModel.successDisfavourObserver.observe(this, EventObserver {
            binding.event?.favorite = false
            mMenuItemFavorite?.setIcon(R.drawable.ic_favorite_border_24dp)
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

    private fun initImageClickListener() {
        imageView.setOnClickListener {
            val args = Bundle()
            args.putParcelable(ARG_EVENT, mEvent)
            findNavController().navigate(
                R.id.action_eventFragment_to_eventImageFragment,
                args
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite_event -> {
                userLooged?.apply {
                    mMenuItemFavorite = item
                    favorEvent()
                } ?: run {
                    showToast("Você precisa estar logado para favoritar um evento")
                }
            }

            R.id.action_share_event -> {
                Toast.makeText(requireContext(), "Share", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)

    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        mEvent?.apply {
            if (this.favorite) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_fiiled_red_24dp)
            }
        }
    }

    private fun favorEvent() {
        val user = AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()
            ?.userAppDto
        user?.apply {
            mViewModel.favor(FavoriteRequest(this.id, mEvent?.id!!))
        } ?: run {
            showToast("Ocorreu um erro")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }


}
