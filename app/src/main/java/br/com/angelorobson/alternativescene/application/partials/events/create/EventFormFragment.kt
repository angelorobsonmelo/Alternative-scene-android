package br.com.angelorobson.alternativescene.application.partials.events.create


import android.app.DatePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.databinding.EventFormFragmentBinding
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
        binding.buttonAddMoreDate.setOnClickListener {
            onAddDateEventField()
        }

        binding.editTextEventDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, yearCalendar, monthOfYearCalendar, dayOfMonthCalendar ->
                    // Calendar.MONTH returns month which is zero based that is why it is giving 1 less than actual month Add 1 to get correct value
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


}
