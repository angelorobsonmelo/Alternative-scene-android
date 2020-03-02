package br.com.angelorobson.alternativescene.application.partials.events.events

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentGenericWithRecyclerViewComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.EVENT_ID_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.EVENT_IS_FAVORITE_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventImageConstants.EVENT_IMAGE_URL_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.EndlessRecyclerOnScrollListener
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.dialog.ListenerConfirmDialog
import br.com.angelorobson.alternativescene.application.partials.events.event.EventActivity
import br.com.angelorobson.alternativescene.application.partials.events.eventimage.EventImageActivity
import br.com.angelorobson.alternativescene.application.partials.events.events.adapter.EventsAdapter
import br.com.angelorobson.alternativescene.application.partials.userdevice.UserDeviceViewModel
import br.com.angelorobson.alternativescene.databinding.EventsFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.request.UserDeviceRequest
import br.com.angelorobson.alternativescene.enum.StatusEnum
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.events_fragment.*
import javax.inject.Inject


class EventsAdminFragment : BindingFragment<EventsFragmentBinding>(), EventsHandler {


    val APPROVED = 0
    val REPPROVED = 1

    override fun getLayoutResId(): Int = R.layout.events_fragment

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventsViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventsViewModel::class.java]
    }

    private val mUserDeviceViewModel: UserDeviceViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[UserDeviceViewModel::class.java]
    }

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

    private val mEvents = mutableListOf<Event>()
    private var mEventsAdapter =
        EventsAdapter(mEvents, this)

    private var mEventPosition: Int? = null

    private val mSessionUseCase =
        AlternativeSceneApplication.mSessionUseCase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setUpElements()
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
        showToolbarWithoutDisplayArrowBack(getString(R.string.events))
        if (mSessionUseCase.isLogged()) {
            sendFireBaseTokenToServer()
        }
    }

    private fun sendFireBaseTokenToServer() {
        val user = mSessionUseCase.getAuthResponseInSession()?.userAppDto
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result?.token
                mUserDeviceViewModel.saveUserDevice(
                    UserDeviceRequest(
                        user?.id!!,
                        token!!
                    )
                )
            })
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
        if (mSessionUseCase.isLogged()) {
            mViewModel.getEventsByAdmin()
        }

    }

    private fun setUpDagger() {
        DaggerFragmentGenericWithRecyclerViewComponent.builder()
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
                if (mSessionUseCase.isLogged()) {
                    mViewModel.getEventsByAdmin(currentPage)
                }
            }
        })
    }

    private fun initSuccessOberserver() {
        mViewModel.successObserver.observe(this, EventObserver {
            noEventTextView.visibility = View.GONE

            it.data?.content?.let { it1 -> mEvents.addAll(it1) }
            mEventsAdapter.notifyDataSetChanged()
        })

        mViewModel.emptyObserver.observe(this, EventObserver {
            if (mEvents.isEmpty()) {
                noEventTextView.visibility = View.VISIBLE
            }
        })

        mViewModel.activeObserver.observe(this, EventObserver {
            getEvents()
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

            if (mSessionUseCase.isLogged()) {
                mViewModel.getEventsByAdmin()
            }

        }
    }

    override fun onPressShare(event: Event) {
        shareApp()
    }

    override fun onPressFavorite(event: Event, position: Int) {

    }

    override fun onPressApprovedOrReprove(event: Event, position: Int) {
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builderSingle.setTitle(getString(R.string.select_option))

        val arrayAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_singlechoice)
        arrayAdapter.add(getString(R.string.approved))
        arrayAdapter.add(getString(R.string.reprove))

        builderSingle.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter) { dialog, which ->
            when (which) {
                APPROVED -> {
                    showDialogToApprovedOrReprove(
                        event,
                        getString(
                            R.string.are_you_really_approved_or_reproved,
                            getString(R.string.approve)
                        ),
                        StatusEnum.APPROVED.name
                    )
                }

                REPPROVED -> {
                    showDialogToApprovedOrReprove(
                        event,
                        getString(
                            R.string.are_you_really_approved_or_reproved,
                            getString(R.string.reprove)
                        ),
                        StatusEnum.REPROVE.name
                    )
                }

            }


        }
        builderSingle.show()
    }

    private fun showDialogToApprovedOrReprove(
        event: Event,
        message: String,
        status: String
    ) {
        showConfirmDialogWithMessageString(
            "",
            message,
            object : ListenerConfirmDialog {
                override fun onPressPositiveButton(dialog: DialogInterface, id: Int) {
                    val isLogged =
                        AlternativeSceneApplication.mSessionUseCase.isLogged()

                    if (isLogged) {
                        mSessionUseCase.getAuthResponseInSession()?.userAppDto?.apply {
                            mViewModel.approvedOrReprove(
                                event.id,
                                status
                            )
                        }

                        return
                    }
                }

                override fun onPressNegativeButton(dialog: DialogInterface, id: Int) {

                }

            })
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
            R.id.action_account -> {
                findNavController().navigate(R.id.action_eventsFragment_to_accountFragment)
            }
            R.id.action_about -> {
                findNavController().navigate(R.id.action_eventsFragment_to_aboutFragment)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        if (mSessionUseCase.isLogged().not()) {
            menu.findItem(R.id.action_account).isVisible = false
        }

    }

    override fun onLongPressImage(event: Event) {
        val intent = Intent(requireActivity(), EventImageActivity::class.java)
        intent.putExtra(EVENT_IMAGE_URL_EXTRA, event.imageUrl)
        startActivity(intent)
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

    private fun shareApp() {
        val appLink =
            "https://play.google.com/store/apps/details?id=br.com.angelorobson.alternativescene"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_TEXT, appLink)
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)))
    }

}