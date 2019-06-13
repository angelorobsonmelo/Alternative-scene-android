package br.com.angelorobson.alternativescene.service.remote.invoices

import br.com.angelorobson.alternativescene.domain.request.CollectRequest
import br.com.angelorobson.alternativescene.domain.response.InvoiceResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface InvoiceApiDataSource {

    @GET("public/invoices")
    fun getInvoices(): Observable<MutableList<InvoiceResponse>>

    @POST("public/invoices")
    fun createInvoice(@Body collectRequest: CollectRequest): Observable<Void>
}