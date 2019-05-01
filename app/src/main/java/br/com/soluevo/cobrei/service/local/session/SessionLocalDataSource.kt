package br.com.soluevo.cobrei.service.local.session

import br.com.soluevo.cobrei.domain.response.AuthResponse

interface SessionLocalDataSource {

    fun saveAuthInSession(authResponse: AuthResponse)
    fun getAuthResponseInSession(): AuthResponse
    fun destroySession(): Boolean
    fun isLogged(): Boolean
}