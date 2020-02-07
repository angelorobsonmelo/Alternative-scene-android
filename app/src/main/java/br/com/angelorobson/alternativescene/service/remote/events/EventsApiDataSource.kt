package br.com.angelorobson.alternativescene.service.remote.events

import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.domain.request.EventRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import io.reactivex.Observable
import retrofit2.http.*


interface EventsApiDataSource {

    @POST("v1/events/findAll")
    fun getEvent(@Query("pag") page: Int): Observable<ResponseListBase<Event>>

    @POST("v1/events/findAllByAdmin")
    fun findAllByAdmin(@Query("pag") page: Int): Observable<ResponseListBase<Event>>

    @GET("v1/events/{id}")
    fun getEvent(@Path("id") id: Long): Observable<ResponseBase<Event>>

    @POST("v1/events")
    fun save(@Body eventRequest: EventRequest): Observable<ResponseBase<Event>>

    @POST("v1/events/findAllByUserId")
    fun getEventByUserId(@Query("pag") page: Int, @Query("userId") userId: Long): Observable<ResponseListBase<Event>>
}