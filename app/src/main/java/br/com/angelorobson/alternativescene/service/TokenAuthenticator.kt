package br.com.angelorobson.alternativescene.service


import br.com.angelorobson.alternativescene.application.CobreiApplication
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator: Authenticator {

    // Todo Implementar lógica após o token expirar
    override fun authenticate(route: Route?, response: Response): Request? {
        val session = CobreiApplication.mSessionUseCase!!.getAuthResponseInSession()
//        CobreiApplication.authUserAndSaveInSessionUseCase?.reAuth(session?.user!!)
        return response.request().newBuilder().header("Authorization", "Bearer").build()
    }


}