package br.com.angelorobson.alternativescene.service.remote.invoices

import br.com.angelorobson.alternativescene.domain.request.CollectRequest
import br.com.angelorobson.alternativescene.domain.response.InvoiceResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource

interface InvoiceRemoteDataSource {

    fun getInvoices(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<InvoiceResponse>>)
    fun createInvoice(collectRequest: CollectRequest, callback: BaseRemoteDataSource.VoidRemoteDataSourceCallback)

}