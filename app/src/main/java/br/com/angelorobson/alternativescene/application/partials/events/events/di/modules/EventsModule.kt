package br.com.angelorobson.alternativescene.application.partials.events.events.di.modules

import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetWorkModule::class, EventsViewModelModule::class, RecyclerViewAnimatedWithDividerModule::class])
class EventsModule {

    @Provides
    @Singleton
    fun provideEventsApiDataSource(retrofit: Retrofit): EventsApiDataSource {
        return retrofit.create(EventsApiDataSource::class.java)
    }

}