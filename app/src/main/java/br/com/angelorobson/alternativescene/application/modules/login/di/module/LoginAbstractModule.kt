package br.com.angelorobson.alternativescene.application.modules.login.di.module

import br.com.angelorobson.alternativescene.service.remote.auth.AuthRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.auth.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class LoginAbstractModule {

    @Binds
    abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

}