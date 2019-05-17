package br.com.soluevo.cobrei.application.modules.collect.di.modules

import br.com.soluevo.cobrei.application.commom.di.modules.network.NetWorkModule
import br.com.soluevo.cobrei.application.modules.invoices.invoices.di.modules.InvoicesAbstractModule
import br.com.soluevo.cobrei.application.usecases.remote.client.GetClientsUseCase
import br.com.soluevo.cobrei.application.usecases.remote.invoices.CreateInvoiceUseCase
import br.com.soluevo.cobrei.service.remote.client.ClientApiDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSourceImpl
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceApiDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSourceImpl
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

    @Provides
    @Singleton
    fun provideInvoiceApiDataSource(retrofit: Retrofit): InvoiceApiDataSource {
        return retrofit.create(InvoiceApiDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideInvoiceRemoteDataSourceImpl(invoiceApiDataSource: InvoiceApiDataSource): InvoiceRemoteDataSourceImpl {
        return InvoiceRemoteDataSourceImpl(invoiceApiDataSource)
    }

    @Provides
    @Singleton
    fun provideCreateInvoiceUseCase(invoiceRemoteDataSource: InvoiceRemoteDataSource) =
        CreateInvoiceUseCase(invoiceRemoteDataSource)
}



