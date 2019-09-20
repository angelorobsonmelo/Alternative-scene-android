package br.com.angelorobson.alternativescene.application.partials.events.di.component

import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.partials.events.di.modules.EventModule
import br.com.angelorobson.alternativescene.application.partials.events.event.EventFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [EventModule::class, ContextModule::class])
interface EventComponent {

    fun inject(eventFragment: EventFragment)
}