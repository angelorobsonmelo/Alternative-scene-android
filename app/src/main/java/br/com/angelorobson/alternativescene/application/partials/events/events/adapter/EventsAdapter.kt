package br.com.angelorobson.alternativescene.application.partials.events.events.adapter

import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingAdapter
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsHandler
import br.com.angelorobson.alternativescene.databinding.EventItemBinding
import br.com.angelorobson.alternativescene.domain.Event

class EventsAdapter(
    private var mEvents: MutableList<Event>,
    private val eventsHandler: EventsHandler
) : BindingAdapter<EventItemBinding>() {


    override fun getLayoutResId(): Int = R.layout.event_item

    override fun onBindViewHolder(binding: EventItemBinding, position: Int) {
        binding.run {
            val event = mEvents[position]
            this.position = position
            this.event = event
            this.handler = eventsHandler
            executePendingBindings()
            initImageViewClickListener(event)
        }

    }

    private fun EventItemBinding.initImageViewClickListener(
        event: Event
    ) {
        eventImageView.setOnLongClickListener {
            eventsHandler.onLongPressImage(event)
            true
        }

        eventImageView.setOnClickListener {
            eventsHandler.onPressItem(event)
        }
    }

    override fun getItemCount() = mEvents.size


}