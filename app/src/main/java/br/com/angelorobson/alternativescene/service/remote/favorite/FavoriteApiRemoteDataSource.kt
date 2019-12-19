package br.com.angelorobson.alternativescene.service.remote.favorite

import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.Favorite
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoriteApiRemoteDataSource {

    @POST("v1/favorites")
    fun favor(@Body favoriteRequest: FavoriteRequest): Observable<ResponseBase<Favorite>>

    @POST("v1/favorites/findAllByUserId")
    fun getFavors(@Query("pag") page: Int, @Query("userId") userId: Long): Observable<ResponseListBase<Event>>
}