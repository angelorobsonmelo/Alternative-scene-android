package br.com.angelorobson.alternativescene.service


import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator: Authenticator {

    // Todo Implementar lógica após o token expirar
    override fun authenticate(route: Route?, response: Response): Request? {
        val session = AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()
//        AlternativeSceneApplication.authUserAndSaveInSessionUseCase?.reAuth(session?.user!!)
        return response.request().newBuilder().header("Authorization", "Bearer").build()
    }


}