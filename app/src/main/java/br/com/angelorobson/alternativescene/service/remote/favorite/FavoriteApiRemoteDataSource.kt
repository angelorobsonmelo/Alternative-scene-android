package br.com.angelorobson.alternativescene.service.remote.favorite

import br.com.angelorobson.alternativescene.domain.Favorite
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface FavoriteApiRemoteDataSource {

    @POST("v1/favorites")
    fun favor(@Body favoriteRequest: FavoriteRequest): Observable<ResponseBase<Favorite>>
}