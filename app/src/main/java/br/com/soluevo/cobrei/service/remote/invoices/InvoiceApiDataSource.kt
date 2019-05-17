package br.com.soluevo.cobrei.service.remote.invoices

import br.com.soluevo.cobrei.domain.request.CollectRequest
import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface InvoiceApiDataSource {

    @GET("public/invoices")
    fun getInvoices(): Observable<MutableList<InvoiceResponse>>

    @POST("public/invoices")
    fun createInvoice(@Body collectRequest: CollectRequest): Observable<Void>
}