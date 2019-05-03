package br.com.soluevo.cobrei.application.commom.di.modules.application

import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSource
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationAbstractModule {

    @Binds
    abstract fun provideSessionLocalDataSource(sessionLocalDataSourceImpl: SessionLocalDataSourceImpl): SessionLocalDataSource

}