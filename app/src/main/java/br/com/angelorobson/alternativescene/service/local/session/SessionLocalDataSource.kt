package br.com.angelorobson.alternativescene.service.local.session

import br.com.angelorobson.alternativescene.domain.response.AuthResponse

interface SessionLocalDataSource {

    fun saveAuthInSession(authResponse: AuthResponse)
    fun getAuthResponseInSession(): AuthResponse
    fun destroySession(): Boolean
    fun isLogged(): Boolean
}