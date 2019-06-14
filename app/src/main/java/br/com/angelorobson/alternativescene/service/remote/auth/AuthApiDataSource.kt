package br.com.angelorobson.alternativescene.service.remote.auth

import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiDataSource {

    @POST("auth/login")
    fun auth(@Body authRequest: AuthRequest): Observable<AuthResponse>
}