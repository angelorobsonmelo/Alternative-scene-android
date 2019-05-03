package br.com.soluevo.cobrei.application.usecases.local

import br.com.soluevo.cobrei.domain.response.AuthResponse
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSource
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

}