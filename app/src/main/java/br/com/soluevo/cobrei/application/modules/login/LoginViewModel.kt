package br.com.soluevo.cobrei.application.modules.login

import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.application.usecases.remote.auth.AuthUserAndSaveInSessionUseCase
import br.com.soluevo.cobrei.application.utils.BaseViewModel
import br.com.soluevo.cobrei.domain.request.AuthRequest
import br.com.soluevo.cobrei.domain.response.AuthResponse
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authUserAndSaveInSessionUseCase: AuthUserAndSaveInSessionUseCase
) : BaseViewModel<AuthResponse>() {

    fun auth(authRequest: AuthRequest) {
        authUserAndSaveInSessionUseCase.auth(authRequest, object : UseCaseBaseCallback.UseCaseCallback<AuthResponse> {
            override fun onSuccess(response: AuthResponse) {
                successObserver.value = response
            }

            override fun onEmptyData() {

            }

            override fun isLoading(isLoading: Boolean) {
                isLoadingObserver.value = isLoading
            }

            override fun onError(errorDescription: String) {
                errorObserver.value = errorDescription
            }

        })
    }

}
