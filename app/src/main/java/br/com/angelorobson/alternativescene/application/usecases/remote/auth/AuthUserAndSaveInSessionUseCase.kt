package br.com.angelorobson.alternativescene.application.usecases.remote.auth

import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.service.remote.auth.AuthRemoteDataSource

class AuthUserAndSaveInSessionUseCase(private val authRemoteDataSource: AuthRemoteDataSource) {

    fun auth(autuRequest: AuthRequest, callback: UseCaseBaseCallback.UseCaseCallback<AuthResponse>) {
        authRemoteDataSource.auth(autuRequest, object : BaseRemoteDataSource.RemoteDataSourceCallback<AuthResponse> {

            override fun onSuccess(response: AuthResponse) {
                AlternativeSceneApplication.mSessionUseCase.saveAuthResponseInSession(response)
                callback.onSuccess(response)
            }

            override fun onError(errorMessage: String) {
                callback.onError(errorMessage)
            }

            override fun isLoading(isLoading: Boolean) {
                callback.isLoading(isLoading)
            }

        })
    }

}