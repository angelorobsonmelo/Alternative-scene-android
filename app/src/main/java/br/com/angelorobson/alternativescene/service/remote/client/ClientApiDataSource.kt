package br.com.angelorobson.alternativescene.service.remote.client

import br.com.angelorobson.alternativescene.domain.Client
import io.reactivex.Observable
import retrofit2.http.GET

interface ClientApiDataSource {

    @GET("public/clients")
    fun getClients(): Observable<MutableList<Client>>

}