package br.com.angelorobson.alternativescene.service.remote.invoices

import androidx.annotation.NonNull
import br.com.angelorobson.alternativescene.domain.request.CollectRequest
import br.com.angelorobson.alternativescene.domain.response.InvoiceResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InvoiceRemoteDataSourceImpl(private val apiDataSource: InvoiceApiDataSource) : InvoiceRemoteDataSource {

    companion object {

        @Volatile
        private var INSTANCE: InvoiceRemoteDataSourceImpl? = null

        fun getInstance(@NonNull apiDataSource: InvoiceApiDataSource): InvoiceRemoteDataSourceImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: InvoiceRemoteDataSourceImpl(apiDataSource).also {
                    INSTANCE = it
                }
            }
    }

    override fun getInvoices(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<InvoiceResponse>>) {
        apiDataSource.getInvoices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.isLoading(true) }
            .doAfterTerminate { callback.isLoading(false) }
            .subscribe(
                {
                    callback.onSuccess(it)
                },
                { throwable ->
                    callback.onError(throwable.localizedMessage)
                }
            )
    }

    override fun createInvoice(
        collectRequest: CollectRequest,
        callback: BaseRemoteDataSource.VoidRemoteDataSourceCallback
    ) {
        apiDataSource.createInvoice(collectRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.isLoading(true) }
            .doAfterTerminate { callback.isLoading(false) }
            .subscribe(
                {
                    callback.onSuccess()
                },
                { throwable ->
                    callback.onError(throwable.localizedMessage)
                }
            )
    }
}