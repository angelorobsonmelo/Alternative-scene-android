package br.com.soluevo.cobrei.service.remote.invoices

import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import io.reactivex.Observable
import retrofit2.http.GET


interface InvoiceApiDataSource {

    @GET("public/invoices")
    fun getInvoices(): Observable<MutableList<InvoiceResponse>>
}