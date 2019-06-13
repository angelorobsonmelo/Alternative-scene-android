package br.com.angelorobson.alternativescene.service

import br.com.angelorobson.alternativescene.application.CobreiApplication
import br.com.angelorobson.alternativescene.domain.response.AuthResponse

object ServiceUtils {

    @JvmStatic
    fun saveUserAndTokenInSession(authResponse: AuthResponse) {

        CobreiApplication.mSessionUseCase?.saveAuthResponseInSession(authResponse)
    }
}