package br.com.soluevo.cobrei.application.modules.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.application.usecases.remote.auth.AuthUseCase
import br.com.soluevo.cobrei.application.utils.BaseViewModel
import br.com.soluevo.cobrei.domain.request.AuthRequest
import br.com.soluevo.cobrei.domain.response.AuthResponse

class LoginViewModel(
    private val authUseCase: AuthUseCase
) : BaseViewModel<AuthResponse>(), ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(authUseCase) as T
    }

    fun auth(authRequest: AuthRequest) {
        authUseCase.auth(authRequest, object : UseCaseBaseCallback.UseCaseCallback<AuthResponse> {
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
