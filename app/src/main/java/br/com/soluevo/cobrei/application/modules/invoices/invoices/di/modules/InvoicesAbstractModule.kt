package br.com.soluevo.cobrei.application.modules.invoices.invoices.di.modules

import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class InvoicesAbstractModule {

    @Binds
    abstract fun provideInvoicesRemoteDataSource(invoiceRemoteDataSourceImpl: InvoiceRemoteDataSourceImpl): InvoiceRemoteDataSource
}