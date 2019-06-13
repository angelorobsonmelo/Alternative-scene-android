package br.com.angelorobson.alternativescene.application.commom.di.modules.application

import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSource
import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationAbstractModule {

    @Binds
    abstract fun provideSessionLocalDataSource(sessionLocalDataSourceImpl: SessionLocalDataSourceImpl): SessionLocalDataSource

}