package br.com.soluevo.cobrei.application.modules.invoices.invoices

import br.com.soluevo.cobrei.application.commom.utils.BaseViewModel
import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.application.usecases.remote.invoices.GetInvoicesUseCase
import br.com.soluevo.cobrei.domain.response.InvoiceResponse
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
