package br.com.soluevo.cobrei.service.remote.invoices

import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import br.com.soluevo.cobrei.service.BaseRemoteDataSource

interface InvoiceRemoteDataSource {

    fun getInvoices(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<InvoiceResponse>>)
}