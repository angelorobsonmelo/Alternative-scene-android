package br.com.soluevo.cobrei.service.remote.invoices

import br.com.soluevo.cobrei.domain.request.CollectRequest
import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import br.com.soluevo.cobrei.service.BaseRemoteDataSource
import io.reactivex.Observable

interface InvoiceRemoteDataSource {

    fun getInvoices(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<InvoiceResponse>>)
    fun createInvoice(collectRequest: CollectRequest, callback: BaseRemoteDataSource.VoidRemoteDataSourceCallback)

}