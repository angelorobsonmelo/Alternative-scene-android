package br.com.soluevo.cobrei.service.remote.auth

import br.com.soluevo.cobrei.domain.request.AuthRequest
import br.com.soluevo.cobrei.domain.response.AuthResponse
import br.com.soluevo.cobrei.service.BaseRemoteDataSource

interface AuthRemoteDataSource {

    fun auth(authRequest: AuthRequest, callback: BaseRemoteDataSource.RemoteDataSourceCallback<AuthResponse>)
}