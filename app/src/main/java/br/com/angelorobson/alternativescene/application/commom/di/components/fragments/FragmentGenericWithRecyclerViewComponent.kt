package br.com.angelorobson.alternativescene.application.commom.di.components.fragments

import br.com.angelorobson.alternativescene.application.commom.di.modules.generic.GenericModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [GenericModule::class, RecyclerViewAnimatedWithDividerModule::class])
interface FragmentGenericWithRecyclerViewComponent {

    fun inject(eventsFragment: EventsFragment)


}