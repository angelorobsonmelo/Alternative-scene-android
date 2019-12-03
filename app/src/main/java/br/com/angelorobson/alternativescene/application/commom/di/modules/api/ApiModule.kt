package br.com.angelorobson.alternativescene.application.commom.di.modules.api

import br.com.angelorobson.alternativescene.service.remote.auth.AuthApiDataSource
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import br.com.angelorobson.alternativescene.service.remote.favorite.FavoriteApiRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.user.UserApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideUserApiDataSource(retrofit: Retrofit): UserApiDataSource {
        return retrofit.create(UserApiDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideEventsApiDataSource(retrofit: Retrofit): EventsApiDataSource {
        return retrofit.create(EventsApiDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideFavoriteApiRemoteDataSource(retrofit: Retrofit): FavoriteApiRemoteDataSource {
        return retrofit.create(FavoriteApiRemoteDataSource::class.java)
    }

}