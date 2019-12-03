package br.com.angelorobson.alternativescene.application.commom.di.modules.application

import android.content.Context
import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.usecases.local.SessionUseCase
import br.com.angelorobson.alternativescene.service.local.event.EventLocalDataSource
import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSource
import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSourceImpl
import br.com.angelorobson.alternativescene.service.remote.auth.AuthApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationAbstractModule::class, NetWorkModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSessionLocalDataSourceImpl(@Named("ApplicationContext") context: Context): SessionLocalDataSourceImpl {
        return SessionLocalDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSessionUseCase(sessionLocalDataSource: SessionLocalDataSource): SessionUseCase {
        return SessionUseCase(sessionLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideEventStateDataSource(@Named("ApplicationContext") context: Context): EventLocalDataSource {
        return EventLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun provideAuthApiDataSource(retrofit: Retrofit): AuthApiDataSource {
        return retrofit.create(AuthApiDataSource::class.java)
    }


}