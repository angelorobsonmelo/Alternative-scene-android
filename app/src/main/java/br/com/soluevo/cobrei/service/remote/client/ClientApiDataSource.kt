package br.com.soluevo.cobrei.service.remote.client

import br.com.soluevo.cobrei.domain.Client
import io.reactivex.Observable
import retrofit2.http.GET

interface ClientApiDataSource {

    @GET("public/clients")
    fun getClients(): Observable<MutableList<Client>>

}