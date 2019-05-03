package br.com.soluevo.cobrei.service


import br.com.soluevo.cobrei.application.CobreiApplication
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