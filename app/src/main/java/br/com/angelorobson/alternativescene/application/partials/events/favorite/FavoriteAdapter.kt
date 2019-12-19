package br.com.angelorobson.alternativescene.application.partials.events.favorite

import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingAdapter
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsHandler
import br.com.angelorobson.alternativescene.databinding.FavoriteItemBinding
import br.com.angelorobson.alternativescene.domain.Event

class FavoriteAdapter(
    private var mEvents: MutableList<Event>,
    private val eventsHandler: EventsHandler
) : BindingAdapter<FavoriteItemBinding>() {


    override fun getLayoutResId(): Int = R.layout.favorite_item


    override fun onBindViewHolder(binding: FavoriteItemBinding, position: Int) {
        binding.run {
            event = mEvents[position]
            this.position = position
            handler = eventsHandler
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = mEvents.size
}