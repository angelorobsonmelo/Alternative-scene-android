package br.com.soluevo.cobrei.application.modules.collect

import androidx.lifecycle.MutableLiveData
import br.com.soluevo.cobrei.application.commom.utils.BaseViewModel
import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.application.usecases.remote.client.GetClientsUseCase
import br.com.soluevo.cobrei.application.usecases.remote.invoices.CreateInvoiceUseCase
import br.com.soluevo.cobrei.domain.Client
import br.com.soluevo.cobrei.domain.request.CollectRequest
import javax.inject.Inject

class CollectViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsUseCase,
    private val createInvoiceUseCase: CreateInvoiceUseCase
) : BaseViewModel<MutableList<Client>>() {

    val successCreateInvoiceObserver = MutableLiveData<Void>()

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

    fun createInvoice(collectRequest: CollectRequest) {
        createInvoiceUseCase.createInvoice(collectRequest, object : UseCaseBaseCallback.VoidUseCaseCallback {
            override fun onSuccess() {
                successCreateInvoiceObserver.value = null
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