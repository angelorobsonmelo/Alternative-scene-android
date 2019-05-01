package br.com.soluevo.cobrei.service.remote.auth

import br.com.soluevo.cobrei.domain.request.AuthRequest
import br.com.soluevo.cobrei.domain.response.AuthResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiDataSource {

    @POST("api/v1/authenticate/token")
    fun auth(@Body authRequest: AuthRequest): Observable<AuthResponse>
}