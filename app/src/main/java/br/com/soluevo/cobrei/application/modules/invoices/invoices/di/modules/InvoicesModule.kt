package br.com.soluevo.cobrei.application.modules.invoices.invoices.di.modules

import br.com.soluevo.cobrei.application.commom.di.modules.network.NetWorkModule
import br.com.soluevo.cobrei.application.usecases.remote.invoices.GetInvoicesUseCase
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceApiDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetWorkModule::class, InvoicesViewModelModule::class, InvoicesAbstractModule::class])
class InvoicesModule {

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
    fun provideGetInvoicesUseCase(invoiceRemoteDataSource: InvoiceRemoteDataSource): GetInvoicesUseCase {
        return GetInvoicesUseCase(invoiceRemoteDataSource)
    }
}