package br.com.angelorobson.alternativescene.application.partials.events.events

import br.com.angelorobson.alternativescene.domain.Event

interface EventsHandler {

    fun onPressShare(event: Event)
    fun onPressFavorite(event: Event)
    fun onPressItem(event: Event)
}