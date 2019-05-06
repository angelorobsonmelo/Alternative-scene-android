package br.com.soluevo.cobrei.application.commom.di.modules.application

import android.content.Context
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSource
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationAbstractModule::class])
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


}