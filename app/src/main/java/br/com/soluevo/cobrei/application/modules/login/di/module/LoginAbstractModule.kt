package br.com.soluevo.cobrei.application.modules.login.di.module

import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSource
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class LoginAbstractModule {

    @Binds
    abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

}