package br.com.angelorobson.alternativescene.application.modules.collect.di.modules

import br.com.angelorobson.alternativescene.service.remote.client.ClientRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.client.ClientRemoteDataSourceImpl
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CollectAbstractModule {

    @Binds
    abstract fun provideClientRemoteDataSource(clientRemoteDataSourceImpl: ClientRemoteDataSourceImpl): ClientRemoteDataSource

    @Binds
    abstract fun provideInvoicesRemoteDataSource(invoiceRemoteDataSourceImpl: InvoiceRemoteDataSourceImpl): InvoiceRemoteDataSource
}