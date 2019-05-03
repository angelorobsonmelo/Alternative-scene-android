package br.com.soluevo.cobrei.application.commom.di.modules.application

import android.app.Application
import android.content.Context
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSource
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSourceImpl
import dagger.Module
import dagger.Provides

@Module(includes = [ApplicationAbstractModule::class])
class ApplicationModule (private val application: Application) {

    @Provides
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    fun provideSessionLocalDataSourceImpl(context: Context): SessionLocalDataSourceImpl {
        return SessionLocalDataSourceImpl(context)
    }

    @Provides
    fun provideSessionUseCase(sessionLocalDataSource: SessionLocalDataSource): SessionUseCase {
       return SessionUseCase(sessionLocalDataSource)
    }


}