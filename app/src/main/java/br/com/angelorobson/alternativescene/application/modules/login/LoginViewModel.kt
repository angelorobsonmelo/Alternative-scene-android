package br.com.angelorobson.alternativescene.application.modules.login

import br.com.angelorobson.alternativescene.application.Event
import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.application.usecases.remote.auth.AuthUserAndSaveInSessionUseCase
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authUserAndSaveInSessionUseCase: AuthUserAndSaveInSessionUseCase
) : BaseViewModel<AuthResponse>() {

    fun auth(authRequest: AuthRequest) {
        authUserAndSaveInSessionUseCase.auth(authRequest, object : UseCaseBaseCallback.UseCaseCallback<AuthResponse> {
            override fun onSuccess(response: AuthResponse) {
                successObserver.value = Event(response)
            }

            override fun onEmptyData() {

            }

            override fun isLoading(isLoading: Boolean) {
                isLoadingObserver.value = isLoading
            }

            override fun onError(errorDescription: String) {
                errorObserver.value = Event(errorDescription)
            }

        })
    }

}
