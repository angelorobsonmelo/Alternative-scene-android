package br.com.angelorobson.alternativescene.service


import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.EventLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {

        val token = AlternativeSceneApplication.mSessionUseCase.getAuthResponseInSession()?.token

        AlternativeSceneApplication.mAuthApiDataSource.refreshToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    AlternativeSceneApplication.mSessionUseCase.saveToken(it.token)
                },
                {

                }
            )

        return response.request().newBuilder().header("Authorization", token).build()
    }


}