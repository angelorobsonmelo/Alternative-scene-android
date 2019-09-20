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
            this.event = mEvents[position]
            this.handler = eventsHandler
            executePendingBindings()
        }

    }

    override fun getItemCount() = mEvents.size


}