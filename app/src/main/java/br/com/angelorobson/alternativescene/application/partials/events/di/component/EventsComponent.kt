package br.com.angelorobson.alternativescene.application.partials.events.di.component

import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsFragment
import br.com.angelorobson.alternativescene.application.partials.events.di.modules.EventsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [EventsModule::class, ContextModule::class])
interface EventsComponent {

    fun inject(eventsFragment: EventsFragment)
}