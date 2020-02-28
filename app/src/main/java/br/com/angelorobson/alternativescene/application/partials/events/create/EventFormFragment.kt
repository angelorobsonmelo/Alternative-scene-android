package br.com.angelorobson.alternativescene.application.partials.events.create


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication.Companion.mSessionUseCase
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.NavigationHostActivity
import br.com.angelorobson.alternativescene.application.commom.di.components.fragments.DaggerFragmentComponentGeneric
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.PLACE_AUTOCOMPLETE_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.JsonUtil.jsonToListObject
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.decodeFile
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.encodeTobase64
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.dialog.ListenerConfirmDialog
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity.Companion.GOOGLE_AUTH_REQUEST_CODE
import br.com.angelorobson.alternativescene.databinding.EventFormFragmentBinding
import br.com.angelorobson.alternativescene.domain.City
import br.com.angelorobson.alternativescene.domain.request.DateEvent
import br.com.angelorobson.alternativescene.domain.request.EventRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import kotlinx.android.synthetic.main.event_form_fragment.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class EventFormFragment : BindingFragment<EventFormFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_form_fragment

    private val mCalendar = Calendar.getInstance()
    private val mYear = mCalendar.get(Calendar.YEAR)
    private val mMonth = mCalendar.get(Calendar.MONTH)
    private val dDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    private lateinit var parentLinearLayoutDates: LinearLayout

    private val eventRequest = EventRequest()
    private lateinit var mCities: List<City>

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventFormViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventFormViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        val isUserLogged =
            mSessionUseCase.isLogged()

        if (isUserLogged) {
            return
        }

        goToSignInActivity()
    }

    private fun goToSignInActivity() {
        startActivityForResult(
            Intent(requireContext(), SignInActivity::class.java),
            GOOGLE_AUTH_REQUEST_CODE
        )
    }

    private fun setUpElements() {
        setHasOptionsMenu(true)
        setUpDagger()
        showToolbarWithoutDisplayArrowBack(getString(R.string.spread_event))
        parentLinearLayoutDates = binding.eventDateLinearLayout
        initObservers()
        handleClicks()
        getCities()
    }

    private fun getCities() {
        mCities = jsonToListObject(resources, R.raw.cities, City::class.java)
        mCities.sortedBy { it.name }
    }

    private fun setUpDagger() {
        DaggerFragmentComponentGeneric.builder()
            .contextModule(ContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun handleClicks() {
        addMoreDateField()
        showDatePicker()
        uploadImageClickListener()
        eventCityListener()
    }


    private fun addMoreDateField() {
        binding.buttonAddMoreDate.setOnClickListener {
            onAddDateEventField()
        }
    }

    private fun showDatePicker() {
        binding.editTextEventDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, yearCalendar, monthOfYearCalendar, dayOfMonthCalendar ->
                    binding.editTextEventDate.setText(
                        String.format(
                            "%d/%d/%d",
                            dayOfMonthCalendar,
                            monthOfYearCalendar + 1,
                            yearCalendar
                        )
                    )
                },
                mYear,
                mMonth,
                dDay
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    private fun uploadImageClickListener() {
        binding.buttonUploadImage.setOnClickListener {
            showGallery()
        }
    }

    private fun eventCityListener() {
        binding.eventCity.setOnClickListener {
            showCitiesDialog()
        }
    }

    private fun showGallery() {
        ImagePicker.Builder(activity)
            .mode(ImagePicker.Mode.GALLERY)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(ImagePicker.Directory.DEFAULT)
            .extension(ImagePicker.Extension.PNG)
            .allowOnlineImages(true)
            .scale(600, 600)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()
    }

    private fun onAddDateEventField() {
        activity?.let {
            val inflater = it.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.row_date_field, null)
            getElementsAndHandleClicks(rowView)
            parentLinearLayoutDates.addView(rowView, parentLinearLayoutDates.childCount - 1)

        }
    }

    private fun getElementsAndHandleClicks(rowView: View) {
        val dateEditText = rowView.findViewById<EditText>(R.id.eventDateFieldRow)
        dateEditText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, yearCalendar, monthOfYearCalendar, dayOfMonthCalendar ->
                    // Calendar.MONTH returns month which is zero based that is why it is giving 1 less than actual month Add 1 to get correct value
                    dateEditText.setText(
                        String.format(
                            "%d/%d/%d",
                            dayOfMonthCalendar,
                            monthOfYearCalendar + 1,
                            yearCalendar
                        )
                    )
                },
                mYear,
                mMonth,
                dDay
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        val linearLayoutClose = rowView.findViewById<LinearLayout>(R.id.linearLayoutClose)

        linearLayoutClose.setOnClickListener { view -> onDeleteEventDateField(view) }
    }

    fun onDeleteEventDateField(view: View) {
        parentLinearLayoutDates.removeView(view.parent as View)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    var mLastClickTime = 0L

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_event -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return false
                }
                mLastClickTime = SystemClock.elapsedRealtime()

                // Todo está aqui temporariamente até ajeitar a questão do Google places
//                binding.eventPlace.setText("Rex Jazz Bar")
                eventRequest.cityName = binding.eventCity.text.toString()
                eventRequest.latitude = 5445.0
                eventRequest.longitude = 9865.54
                eventRequest.address = "Rua do nada"
                eventRequest.locality = "Kfofo"

                if (isValidForm()) {
                    binding.progressBar.visibility = View.VISIBLE
                    setDatesFromForm()
                    val authResponse =
                        mSessionUseCase.getAuthResponseInSession()

                    authResponse?.userAppDto?.let {
                        eventRequest.userAppId = it.id
                        eventRequest.imageUrl =
                            binding.previewEventImageView.drawable.toBitmap().encodeTobase64() ?: ""

                        mViewModel.save(eventRequest)
                    } ?: run {
                        showToast(getString(R.string.user_not_found))
                    }
                }
            }

        }
        return super.onOptionsItemSelected(item)

    }

    private fun initObservers() {
        mViewModel.successObserver.observe(this, EventObserver {
            binding.progressBar.visibility = View.GONE

            showConfirmDialogWithCallback("", getString(R.string.event_success),
                object : ListenerConfirmDialog {
                    override fun onPressPositiveButton(dialog: DialogInterface, id: Int) {
                        val ac = activity as NavigationHostActivity
                        ac.clickOnEventMenu()
                    }

                    override fun onPressNegativeButton(dialog: DialogInterface, id: Int) {

                    }

                })
        })

        mViewModel.errorObserver.observe(this, EventObserver {
            binding.progressBar.visibility = View.GONE
            showAlertError(it)
        })
    }

    private fun isValidForm(): Boolean {
        return when {
            eventRequest.imageUrl.isEmpty() -> {
                showToast(getString(R.string.select_event_image))
                hideProgressBarWithFragNotTouchable(binding.progressBar)
                false
            }
            binding.editTextEventDate.text.toString().isEmpty() -> {
                textInputLayoutEventDate.error = getString(R.string.can_not_be_empty)
                hideProgressBarWithFragNotTouchable(binding.progressBar)
                false
            }

            binding.eventCity.text.toString().isEmpty() -> {
                locationEventTextInputLayout.error = getString(R.string.can_not_be_empty)
                false
            }
            else -> true
        }

    }

    private fun setDatesFromForm() {
        eventRequest.eventDates.clear()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        for (i in 0 until parentLinearLayoutDates.childCount) {
            if (i == 0) {
                getFirstChild(i, formatter)
            } else {
                setDateEditValueFromConstraintLayoutChild(i, formatter)
            }
        }
    }

    private fun getFirstChild(i: Int, formatter: SimpleDateFormat) {
        if (parentLinearLayoutDates.getChildAt(i) is TextInputLayout) {

            val editText = parentLinearLayoutDates.getChildAt(i) as TextInputLayout
            val date = editText.editText?.text.toString()
            if (date.isNotEmpty()) {
                val parsedDate = formatter.parse(date)
                eventRequest.eventDates.add(DateEvent(parsedDate))
            }
        }
    }

    private fun setDateEditValueFromConstraintLayoutChild(
        i: Int,
        formatter: SimpleDateFormat
    ) {
        if (parentLinearLayoutDates.getChildAt(i) is ConstraintLayout) {
            val constraintLayout = parentLinearLayoutDates.getChildAt(i) as ConstraintLayout
            for (j in 0 until constraintLayout.childCount) {
                if (constraintLayout.getChildAt(j) is TextInputLayout) {
                    val editText = constraintLayout.getChildAt(j) as TextInputLayout
                    val date = editText.editText?.text.toString()
                    if (date.isNotEmpty()) {
                        val parsedDate = formatter.parse(date)
                        eventRequest.eventDates.add(DateEvent(parsedDate))
                    } else {
                        editText.error = getString(R.string.can_not_be_empty)
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ImagePicker.IMAGE_PICKER_REQUEST_CODE -> {
                    handleImageResult(data)
                }

                PLACE_AUTOCOMPLETE_REQUEST_CODE -> {
                    handleLocationResult(data)
                }

                GOOGLE_AUTH_REQUEST_CODE -> {

                }

                AutocompleteActivity.RESULT_ERROR -> {
                    handleError(data)
                }

                AutocompleteActivity.RESULT_CANCELED -> {
                    handleError(data)
                }
            }
        }
    }

    private fun handleImageResult(data: Intent?) {
        val mPaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)
        showImagePreviewEvent(mPaths?.first())
    }

    private fun handleError(data: Intent?) {
        data?.apply {
            Autocomplete.getStatusFromIntent(data)
        }
    }

    private fun handleLocationResult(data: Intent?) {
        data?.apply {
            val place = Autocomplete.getPlaceFromIntent(this)
            place.latLng?.apply {
                //                event_place.setText(place.name)
                val nameCity = getNameCity(this)
                eventRequest.cityName = nameCity
                eventRequest.latitude = this.latitude
                eventRequest.longitude = this.longitude
                eventRequest.address = place.address ?: ""
                eventRequest.locality = place.name ?: ""
            }
        }
    }

    private fun getNameCity(latLng: LatLng): String {
        val mGeocoder = Geocoder(activity, Locale.getDefault())

        val addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNullOrEmpty()) {
            return addresses[0].subAdminArea
        }
        return ""
    }

    private fun showImagePreviewEvent(imagePath: String?) {
        imagePath?.let {
            binding.previewEventImageView.visibility = View.VISIBLE
            val bitmap = imagePath.decodeFile()
            binding.previewEventImageView.setImageBitmap(bitmap)
            eventRequest.imageUrl = imagePath
        }
    }

    private fun showCitiesDialog() {
        val items = ArrayList<SearchModel>()

        mCities.forEach {
            items.add(SearchModel(it.name))
        }

        SimpleSearchDialogCompat<SearchModel>(
            requireContext(),
            getString(R.string.cities),
            getString(R.string.looking_for), null, items,
            SearchResultListener<SearchModel> { dialog, item, position ->
                val city = mCities.first { it.name == item.title }
                binding.eventCity.setText(city.name)
                dialog.dismiss()
            }).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposables.clear()
    }

}
