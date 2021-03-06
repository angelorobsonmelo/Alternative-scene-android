package br.com.angelorobson.alternativescene.application.usecases.local

import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSource
import javax.inject.Inject

class SessionUseCase @Inject constructor(private val mSessionLocalDataSource: SessionLocalDataSource) {

    fun saveAuthResponseInSession(authResponse: AuthResponse) {
        mSessionLocalDataSource.saveAuthInSession(authResponse)
    }

    fun getAuthResponseInSession(): AuthResponse? {
        return mSessionLocalDataSource.getAuthResponseInSession()
    }

    fun destroySession(): Boolean {
        return mSessionLocalDataSource.destroySession()
    }

    fun isLogged(): Boolean {
        return mSessionLocalDataSource.isLogged()
    }

    fun getToken(): String? {
        return mSessionLocalDataSource.getToken()
    }

    fun saveToken(token: String) {
        return mSessionLocalDataSource.saveToken(token)
    }

}