package br.com.angelorobson.alternativescene.application.partials.events.di.modules

import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class EventsApiModule {

    @Provides
    @Singleton
    fun provideEventsApiDataSource(retrofit: Retrofit): EventsApiDataSource {
        return retrofit.create(EventsApiDataSource::class.java)
    }
}