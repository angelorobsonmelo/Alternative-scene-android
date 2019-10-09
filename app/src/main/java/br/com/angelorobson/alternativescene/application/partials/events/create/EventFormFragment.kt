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
import android.widget.Toast
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.PLACE_AUTOCOMPLETE_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.PlacesFieldSelector
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.decodeFile
import br.com.angelorobson.alternativescene.databinding.EventFormFragmentBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.event_form_fragment.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.util.*


class EventFormFragment : BindingFragment<EventFormFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_form_fragment

    private val mCalendar = Calendar.getInstance()
    private val mYear = mCalendar.get(Calendar.YEAR)
    private val mMonth = mCalendar.get(Calendar.MONTH)
    private val dDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    private lateinit var parentConstraint: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
    }

    private fun setUpElements() {
        setHasOptionsMenu(true)
        showToolbarWithoutDisplayArrowBack(getString(R.string.spread_event))
        parentConstraint = binding.eventDateLinearLayout

        handleClicks()
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
            parentConstraint.addView(rowView, parentConstraint.childCount - 1)
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
        parentConstraint.removeView(view.parent as View)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_event -> {
                Toast.makeText(requireContext(), "save event", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onOptionsItemSelected(item)

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
        binding.previewEventImageView.visibility = View.VISIBLE
        val bitmap = imagePath.decodeFile()
        binding.previewEventImageView.setImageBitmap(bitmap)
    }


}
