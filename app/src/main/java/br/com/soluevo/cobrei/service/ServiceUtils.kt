package br.com.soluevo.cobrei.service

import br.com.soluevo.cobrei.application.CobreiApplication
import br.com.soluevo.cobrei.domain.response.AuthResponse

object ServiceUtils {

    @JvmStatic
    fun saveUserAndTokenInSession(authResponse: AuthResponse) {

        CobreiApplication.mSessionUseCase?.saveAuthResponseInSession(authResponse)
    }
}