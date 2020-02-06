package br.com.angelorobson.alternativescene.service.remote.auth

import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiDataSource {

    @POST("v1/auth")
    fun auth(@Body authRequest: AuthRequest): Observable<ResponseBase<AuthResponse>>

    @POST("v1/auth/refresh")
    fun refreshToken(): Observable<AuthResponse>
}