package br.com.soluevo.cobrei.application.modules.collect.di.modules

import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSourceImpl
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CollectAbstractModule {

    @Binds
    abstract fun provideClientRemoteDataSource(clientRemoteDataSourceImpl: ClientRemoteDataSourceImpl): ClientRemoteDataSource

    @Binds
    abstract fun provideInvoicesRemoteDataSource(invoiceRemoteDataSourceImpl: InvoiceRemoteDataSourceImpl): InvoiceRemoteDataSource
}