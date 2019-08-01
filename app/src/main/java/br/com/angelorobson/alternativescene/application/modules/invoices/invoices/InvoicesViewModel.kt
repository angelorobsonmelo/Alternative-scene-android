package br.com.angelorobson.alternativescene.application.modules.invoices.invoices

import br.com.angelorobson.alternativescene.application.Event
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.application.usecases.remote.invoices.GetInvoicesUseCase
import br.com.angelorobson.alternativescene.domain.response.InvoiceResponse
import javax.inject.Inject

class InvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase
) : BaseViewModel<MutableList<InvoiceResponse>>() {

    init {
        getInvoices()
    }

    fun getInvoices() {
        getInvoicesUseCase.getInvoices(object : UseCaseBaseCallback.UseCaseCallback<MutableList<InvoiceResponse>> {
            override fun onSuccess(response: MutableList<InvoiceResponse>) {
                successObserver.value = Event(response)
            }

            override fun onEmptyData() {
                emptyObserver.value = Event(true)
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
