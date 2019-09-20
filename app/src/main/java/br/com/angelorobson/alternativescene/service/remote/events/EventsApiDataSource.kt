package br.com.angelorobson.alternativescene.service.remote.events

import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import io.reactivex.Observable
import retrofit2.http.*


interface EventsApiDataSource {

    @POST("v1/events/filter")
    fun getEvent(@Body filter: EventFilter, @Query("pag") page: Int): Observable<ResponseListBase<Event>>

    @GET("v1/events/{id}")
    fun getEvent(@Path("id") id: Long): Observable<ResponseBase<Event>>
}