package br.com.angelorobson.alternativescene.application.partials.events.di.modules

import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.SimpleRecyclerView
import br.com.angelorobson.alternativescene.application.commom.di.modules.viewmodel.ViewModelModule
import dagger.Module

@Module(includes = [NetWorkModule::class, ViewModelModule::class, EventsApiModule::class, SimpleRecyclerView::class])
class EventModule