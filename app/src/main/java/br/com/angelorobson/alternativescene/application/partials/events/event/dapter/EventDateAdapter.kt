package br.com.angelorobson.alternativescene.application.partials.events.event.dapter

import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingAdapter
import br.com.angelorobson.alternativescene.databinding.EventDateItemBinding
import br.com.angelorobson.alternativescene.domain.EventDate

class EventDateAdapter(
    private val mEventDates: ArrayList<EventDate>
) : BindingAdapter<EventDateItemBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_date_item

    override fun onBindViewHolder(binding: EventDateItemBinding, position: Int) {
        binding.eventDate = mEventDates[position]
    }

    override fun getItemCount(): Int = mEventDates.size

    fun updateItems(eventDates: List<EventDate>) {
        mEventDates.clear()
        mEventDates.addAll(eventDates)
        notifyDataSetChanged()
    }
}