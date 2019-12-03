package br.com.angelorobson.alternativescene.application.commom.di.components.fragments

import br.com.angelorobson.alternativescene.application.commom.di.modules.generic.GenericModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerViewModule
import br.com.angelorobson.alternativescene.application.partials.events.create.EventFormFragment
import br.com.angelorobson.alternativescene.application.partials.events.event.EventActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [GenericModule::class, SimpleRecyclerViewModule::class])
interface FragmentWithSimpleRecyclerViewComponentGeneric {

    fun inject(eventActivity: EventActivity)
    fun inject(eventFormFragment: EventFormFragment)


}