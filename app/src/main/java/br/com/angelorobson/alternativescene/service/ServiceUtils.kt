package br.com.angelorobson.alternativescene.service

import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.domain.response.AuthResponse

object ServiceUtils {

    @JvmStatic
    fun saveUserAndTokenInSession(authResponse: AuthResponse) {

        AlternativeSceneApplication.mSessionUseCase.saveAuthResponseInSession(authResponse)
    }
}