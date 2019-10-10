package br.com.angelorobson.alternativescene.application.partials.events.create


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventObserver
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.PLACE_AUTOCOMPLETE_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.PlacesFieldSelector
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.decodeFile
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.encodeTobase64
import br.com.angelorobson.alternativescene.application.partials.events.di.component.DaggerEventFormComponent
import br.com.angelorobson.alternativescene.application.partials.events.event.EventViewModel
import br.com.angelorobson.alternativescene.databinding.EventFormFragmentBinding
import br.com.angelorobson.alternativescene.domain.request.DateEvent
import br.com.angelorobson.alternativescene.domain.request.EventRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.event_form_fragment.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EventFormFragment : BindingFragment<EventFormFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_form_fragment

    private val mCalendar = Calendar.getInstance()
    private val mYear = mCalendar.get(Calendar.YEAR)
    private val mMonth = mCalendar.get(Calendar.MONTH)
    private val dDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    private lateinit var parentLinearLayoutDates: LinearLayout

    private val eventRequest = EventRequest()

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    private val mViewModel: EventFormViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[EventFormViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
    }

    private fun setUpElements() {
        setHasOptionsMenu(true)
        setUpDagger()
        showToolbarWithoutDisplayArrowBack(getString(R.string.spread_event))
        parentLinearLayoutDates = binding.eventDateLinearLayout
        initObservers()
        handleClicks()
    }

    private fun setUpDagger() {
        DaggerEventFormComponent.builder()
            .contextModule(ContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun handleClicks() {
        addMoreDateField()
        showDatePicker()
        uploadImageClickListener()
        eventPlaceClickListener()
    }

    private fun eventPlaceClickListener() {
        event_place.setOnClickListener {
            val fieldSelector = PlacesFieldSelector()
            val autocompleteIntent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                fieldSelector.allFields
            )
                .build(requireContext())
            startActivityForResult(autocompleteIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        }
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
                DatePickerDialog.OnDateSetListener { view, yearCalendar, monthOfYearCalendar, dayOfMonthCalendar ->
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

            datePickerDialog.show()
        }
    }

    private fun uploadImageClickListener() {
        binding.buttonUploadImage.setOnClickListener {
            showGallery()
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
                DatePickerDialog.OnDateSetListener { view, yearCalendar, monthOfYearCalendar, dayOfMonthCalendar ->
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_event -> {
                // Todo está aqui temporariamente até ajeitar a questão do Google places
                binding.eventPlace.setText("Rex Jazz Bar")
                eventRequest.cityName = "Maceió"
                eventRequest.latitude = 5445.0
                eventRequest.longitude = 9865.54
                eventRequest.address = "Rua do nada"
                eventRequest.locality = "Kfofo"

                if (isValidForm()) {
                    setDatesFromForm()
                    val authResponse =
                        AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()
                    eventRequest.userAppId = 2
                    eventRequest.imageUrl =
                        binding.previewEventImageView.drawable.toBitmap().encodeTobase64() ?: ""
                    mViewModel.save(eventRequest)
                }
            }

        }
        return super.onOptionsItemSelected(item)

    }

    private fun initObservers() {
        mViewModel.successObserver.observe(this, EventObserver {
            showToast("Succcess!")
        })

        mViewModel.errorObserver.observe(this, EventObserver {
            showAlertError(it)
        })
    }

    private fun isValidForm(): Boolean {
        return when {
            eventRequest.imageUrl.isEmpty() -> {
                showToast("A imagem do evento deve ser selecionada")
                false
            }
            binding.eventPlace.text.toString().isEmpty() -> {
                locationEventTextInputLayout.error = "Este campo deve ser preenchido"
                false
            }
            binding.editTextEventDate.text.toString().isEmpty() -> {
                textInputLayoutEventDate.error = "Este campo deve ser preenchido"
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
                        editText.error = "Este campo não pode ficar vazio"
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
            val status = Autocomplete.getStatusFromIntent(data)
        }
    }

    private fun handleLocationResult(data: Intent?) {
        data?.apply {
            val place = Autocomplete.getPlaceFromIntent(this)
            place.latLng?.apply {
                event_place.setText(place.name)
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

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.disposable.clear()
    }

}
