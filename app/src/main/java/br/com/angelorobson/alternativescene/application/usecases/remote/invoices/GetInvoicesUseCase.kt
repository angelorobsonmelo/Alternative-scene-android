package br.com.angelorobson.alternativescene.application.usecases.remote.invoices

import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.domain.response.InvoiceResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.invoices.InvoiceRemoteDataSource

class GetInvoicesUseCase(private val invoiceRemoteDataSource: InvoiceRemoteDataSource) {

    fun getInvoices(useCaseCallback: UseCaseBaseCallback.UseCaseCallback<MutableList<InvoiceResponse>>) {
        invoiceRemoteDataSource.getInvoices(object :
            BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<InvoiceResponse>> {
            override fun onSuccess(response: MutableList<InvoiceResponse>) {
                if (response.isEmpty()) {
                    useCaseCallback.onEmptyData()
                    return
                }

                useCaseCallback.onSuccess(response)
            }

            override fun onError(errorMessage: String) {
                useCaseCallback.onError(errorMessage)
            }

            override fun isLoading(isLoading: Boolean) {
                useCaseCallback.isLoading(isLoading)
            }

        })
    }
}