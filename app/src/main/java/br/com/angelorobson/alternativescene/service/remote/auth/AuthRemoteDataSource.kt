package br.com.angelorobson.alternativescene.service.remote.auth

import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource

interface AuthRemoteDataSource {

    fun auth(authRequest: AuthRequest, callback: BaseRemoteDataSource.RemoteDataSourceCallback<AuthResponse>)
}