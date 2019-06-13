package br.com.angelorobson.alternativescene.application.modules.invoices.invoices.di.modules

import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class InvoicesAbstractModule {

    @Binds
    abstract fun provideInvoicesRemoteDataSource(invoiceRemoteDataSourceImpl: InvoiceRemoteDataSourceImpl): InvoiceRemoteDataSource
}