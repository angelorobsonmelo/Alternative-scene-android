package br.com.angelorobson.alternativescene.application.partials.events.di.modules

import br.com.angelorobson.alternativescene.application.commom.di.modules.generic.GenericModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import dagger.Module

@Module(includes = [GenericModule::class, RecyclerViewAnimatedWithDividerModule::class])
class EventsModule