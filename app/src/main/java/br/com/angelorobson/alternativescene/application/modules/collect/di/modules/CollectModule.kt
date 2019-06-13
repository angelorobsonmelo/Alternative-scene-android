package br.com.angelorobson.alternativescene.application.modules.collect.di.modules

import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.usecases.remote.client.GetClientsUseCase
import br.com.angelorobson.alternativescene.application.usecases.remote.invoices.CreateInvoiceUseCase
import br.com.angelorobson.alternativescene.service.remote.client.ClientApiDataSource
import br.com.angelorobson.alternativescene.service.remote.client.ClientRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.client.ClientRemoteDataSourceImpl
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceApiDataSource
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSourceImpl
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



