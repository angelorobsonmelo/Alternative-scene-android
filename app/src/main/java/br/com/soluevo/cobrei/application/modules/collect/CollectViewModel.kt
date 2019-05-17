package br.com.soluevo.cobrei.application.modules.collect

import br.com.soluevo.cobrei.application.commom.utils.BaseViewModel
import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.application.usecases.remote.client.GetClientsUseCase
import br.com.soluevo.cobrei.domain.Client
import javax.inject.Inject

class CollectViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsUseCase
) : BaseViewModel<MutableList<Client>>() {

    init {
        getClients()
    }

    private fun getClients() {
        getClientsUseCase.getClients(object : UseCaseBaseCallback.UseCaseCallback<MutableList<Client>> {
            override fun onSuccess(response: MutableList<Client>) {
                successObserver.value = response
            }

            override fun onEmptyData() {
                emptyObserver.value = true
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