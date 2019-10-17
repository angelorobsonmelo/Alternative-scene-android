package br.com.angelorobson.alternativescene.application.partials.events.event

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerView
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.BindingActivity
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventComponent
import br.com.angelorobson.alternativescene.application.partials.events.event.dapter.EventDateAdapter
import br.com.angelorobson.alternativescene.databinding.EventActivityBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.EventDate
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import kotlinx.android.synthetic.main.event_fragment.*
import javax.inject.Inject

class EventActivity : BindingActivity<EventActivityBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_activity

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

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

    private var mEvent: Event? = null

    private var mEventId = 0L
    private var mIsFavoriteEvent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.apply {
            mEventId = this.getLongExtra(EventsContants.EVENT_ID_EXTRA, 0)
            mIsFavoriteEvent = this.getBooleanExtra(EventsContants.EVENT_IS_FAVORITE_EXTRA, false)
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        setUpElements()
    }

    private fun setUpElements() {
        setUpBinding()
        setUpDagger()
        getEvent()
        initObservers()
        initImageClickListener()
    }

    private fun setUpBinding() {
        binding.lifecycleOwner = this
    }

    private fun setUpDagger() {
        DaggerEventComponent.builder()
            .contextModule(ContextModule(this))
            .simpleRecyclerView(
                SimpleRecyclerView(
                    binding.recyclerViewDates,
                    mEventDateAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
                )
            )
            .build()
            .inject(this)
    }

    private fun getEvent() {
        mViewModel.getEvent(mEventId)

    }

    private fun initObservers() {
        mViewModel.successObserver.observe(this, EventObserver {
            mEvent = it.data
            binding.event = mEvent
            populateDates()
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

    private fun populateDates() {
        mEvent?.eventDates?.apply {
            mEventDateAdapter.updateItems(this)
        }
    }

    private fun initImageClickListener() {
        imageView.setOnClickListener {
            /*val args = Bundle()
            args.putParcelable(EventsContants.ARG_EVENT, mEvent)
            findNavController().navigate(
                R.id.action_eventFragment_to_eventImageFragment,
                args
            )*/
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mMenuItemFavorite = menu.getItem(0)
        if (mIsFavoriteEvent) {
            menu.getItem(0).setIcon(R.drawable.ic_favorite_fiiled_red_24dp)
        }

        return super.onPrepareOptionsMenu(menu)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite_event -> {
                userLooged?.apply {
                    favorEvent()
                } ?: run {
                    //                    showToast("Você precisa estar logado para favoritar um evento")
                }
            }

            R.id.action_share_event -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun favorEvent() {
        val user = AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()
            ?.userAppDto
        user?.apply {
            mViewModel.favor(FavoriteRequest(this.id, mEventId))
        } ?: run {
            //            showToast("Ocorreu um erro")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }

}
