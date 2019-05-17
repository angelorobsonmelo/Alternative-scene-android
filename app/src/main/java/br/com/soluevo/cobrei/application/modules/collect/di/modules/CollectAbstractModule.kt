package br.com.soluevo.cobrei.application.modules.collect.di.modules

import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CollectAbstractModule {

    @Binds
    abstract fun provideClientRemoteDataSource(clientRemoteDataSourceImpl: ClientRemoteDataSourceImpl): ClientRemoteDataSource
}