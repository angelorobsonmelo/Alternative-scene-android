package br.com.soluevo.cobrei.application.modules.collect.di.modules

import br.com.soluevo.cobrei.application.commom.di.modules.network.NetWorkModule
import br.com.soluevo.cobrei.application.usecases.remote.client.GetClientsUseCase
import br.com.soluevo.cobrei.service.remote.client.ClientApiDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetWorkModule::class, CollectViewModelModule::class, CollectAbstractModule::class])
class CollectModule {

    @Provides
    @Singleton
    fun provideClientApiDataSource(retrofit: Retrofit): ClientApiDataSource =
        retrofit.create(ClientApiDataSource::class.java)

    @Provides
    @Singleton
    fun provideClientRemoteDataSource(clientApiDataSource: ClientApiDataSource) =
        ClientRemoteDataSourceImpl(clientApiDataSource)

    @Provides
    @Singleton
    fun provideGetClientUseCase(clientRemoteDataSource: ClientRemoteDataSource) =
        GetClientsUseCase(clientRemoteDataSource)
}



