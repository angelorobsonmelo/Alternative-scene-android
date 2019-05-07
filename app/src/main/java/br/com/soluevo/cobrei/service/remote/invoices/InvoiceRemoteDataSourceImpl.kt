package br.com.soluevo.cobrei.service.remote.invoices

import androidx.annotation.NonNull
import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import br.com.soluevo.cobrei.service.BaseRemoteDataSource
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
}