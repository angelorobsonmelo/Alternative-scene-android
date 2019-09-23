package br.com.angelorobson.alternativescene.service.remote.user

import br.com.angelorobson.alternativescene.domain.request.UserRequest
import br.com.angelorobson.alternativescene.domain.response.UserResponse
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiDataSource {

    @GET("v1/users_app/findByEmailAndGoogleAccountId")
    fun findByEmailAndGoogleAccountId(@Query("email") email: String, @Query("googleAccountId") googleAccountId: String): Observable<ResponseBase<UserResponse>>

    @POST("v1/users_app")
    fun save(@Body userRequest: UserRequest): Observable<ResponseBase<UserResponse>>
}