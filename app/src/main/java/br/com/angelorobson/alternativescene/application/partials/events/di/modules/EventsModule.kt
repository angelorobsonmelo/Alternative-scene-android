package br.com.angelorobson.alternativescene.application.partials.events.di.modules

import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.viewmodel.ViewModelModule
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetWorkModule::class, ViewModelModule::class, EventsApiModule::class, RecyclerViewAnimatedWithDividerModule::class])
class EventsModule