package br.com.angelorobson.alternativescene.application.partials.events.event

import android.content.Intent
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
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentWithSimpleRecyclerViewComponentGeneric
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerViewModule
import br.com.angelorobson.alternativescene.application.commom.utils.Constants
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.BindingActivity
import br.com.angelorobson.alternativescene.application.partials.events.event.dapter.EventDateAdapter
import br.com.angelorobson.alternativescene.application.partials.events.eventimage.EventImageActivity
import br.com.angelorobson.alternativescene.databinding.EventActivityBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.EventDate
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import kotlinx.android.synthetic.main.event_activity.*
import java.text.MessageFormat
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
        AlternativeSceneApplication.mSessionUseCase.isLogged()

    private var mEvent: Event? = null

    private var mEventId = 0L
    private var mIsFavoriteEvent = false
    private var isFavoriteIconClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.apply {
            mEventId = this.getLongExtra(EventsContants.EVENT_ID_EXTRA, 0)
            mIsFavoriteEvent = this.getBooleanExtra(EventsContants.EVENT_IS_FAVORITE_EXTRA, false)
        }

        val toolbar = binding.toolbar
        showToolbarWithArrowBack(toolbar, getString(R.string.event_detail))

        setUpElements()
    }

    private fun setUpElements() {
        showProgressBarWithFragNotTouchable(progressBar)
        setUpBinding()
        setUpDagger()
        getEvent()
        initObservers()
        initImageClickListener()
    }

    private fun setUpBinding() {
        binding.lifecycleOwner = this
    }

    @Suppress("UNCHECKED_CAST")
    private fun setUpDagger() {
        DaggerFragmentWithSimpleRecyclerViewComponentGeneric.builder()
            .contextModule(ContextModule(this))
            .simpleRecyclerViewModule(
                SimpleRecyclerViewModule(
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
            hideProgressBarWithFragNotTouchable(progressBar)
        })

        mViewModel.successFavoriteObserver.observe(this, EventObserver {
            binding.event?.favorite = true
            mEvent = binding.event
            isFavoriteIconClicked = true
            mMenuItemFavorite?.setIcon(R.drawable.ic_favorite_fiiled_red_24dp)
        })

        mViewModel.successDisfavourObserver.observe(this, EventObserver {
            binding.event?.favorite = false
            mEvent = binding.event
            isFavoriteIconClicked = true
            mMenuItemFavorite?.setIcon(R.drawable.ic_favorite_border_white_24dp)
        })
    }

    private fun populateDates() {
        mEvent?.eventDates?.apply {
            mEventDateAdapter.updateItems(this)
        }
    }

    private fun initImageClickListener() {
        imageView.setOnClickListener {
            val intent = Intent(this, EventImageActivity::class.java)
            intent.putExtra(Constants.EventImageConstants.EVENT_IMAGE_URL_EXTRA, mEvent?.imageUrl)
            startActivity(intent)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mMenuItemFavorite = menu.getItem(0)
        mEvent?.favorite = mIsFavoriteEvent
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
                if (userLooged) {
                    favorEvent()
                } else {
                    Toast.makeText(
                        this,
                        "Você deve estar logado para favoritar este evento",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            R.id.action_share_event -> {
                mEvent?.apply {
                    shareDeepLink(this)
                }
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

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent()

        mEvent?.apply {
            intent.putExtra(EventsContants.EVENT_IS_FAVORITE_EXTRA, this.favorite)
            intent.putExtra(EventsContants.FAVORITE_ICON_IS_CLICKED, isFavoriteIconClicked)
            setResult(EventsContants.DETAIL_EVENT_REQUEST_CODE, intent)
        } ?: run {
            setResult(EventsContants.DETAIL_EVENT_REQUEST_CODE)
        }

        finish()
        return false
    }


    private fun shareDeepLink(event: Event) {
        val firebaseLink = "https://angelorobsonn.page.link"
        val myLink = "https://www.angelorobson.com?id=${event.id}"
        val myPackage = "br.com.angelorobson.alternativescene"
        val link = "{0}?link={1}&apn={2}"
        val deepLink = MessageFormat.format(link, firebaseLink, myLink, myPackage)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Evento")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }

}
