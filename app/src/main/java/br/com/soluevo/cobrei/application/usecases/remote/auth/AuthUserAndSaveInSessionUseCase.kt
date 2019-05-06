package br.com.soluevo.cobrei.application.usecases.remote.auth

import br.com.soluevo.cobrei.application.CobreiApplication
import br.com.soluevo.cobrei.domain.request.AuthRequest
import br.com.soluevo.cobrei.domain.response.AuthResponse
import br.com.soluevo.cobrei.service.BaseRemoteDataSource
import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSource

class AuthUserAndSaveInSessionUseCase(private val authRemoteDataSource: AuthRemoteDataSource) {

    fun auth(autuRequest: AuthRequest, callback: UseCaseBaseCallback.UseCaseCallback<AuthResponse>) {
        authRemoteDataSource.auth(autuRequest, object : BaseRemoteDataSource.RemoteDataSourceCallback<AuthResponse> {

            override fun onSuccess(response: AuthResponse) {
                CobreiApplication.mSessionUseCase.saveAuthResponseInSession(response)
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