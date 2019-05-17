package br.com.soluevo.cobrei.application.usecases.remote.invoices

import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.domain.request.CollectRequest
import br.com.soluevo.cobrei.service.BaseRemoteDataSource
import br.com.soluevo.cobrei.service.remote.invoices.InvoiceRemoteDataSource

class CreateInvoiceUseCase(private val invoiceRemoteDataSource: InvoiceRemoteDataSource) {

    fun createInvoice(collectRequest: CollectRequest, callback: UseCaseBaseCallback.VoidUseCaseCallback) {
        invoiceRemoteDataSource.createInvoice(
            collectRequest,
            object : BaseRemoteDataSource.VoidRemoteDataSourceCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                }

                override fun onError(errorMessage: String) {
                    callback.onError(errorMessage)
                }

                override fun onEmpty() {
                    callback.onEmptyData()
                }

                override fun isLoading(isLoading: Boolean) {
                    callback.isLoading(isLoading)
                }

            })
    }
}