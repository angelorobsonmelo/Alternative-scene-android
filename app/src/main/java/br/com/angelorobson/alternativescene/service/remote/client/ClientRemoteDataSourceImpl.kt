package br.com.angelorobson.alternativescene.service.remote.client

import br.com.angelorobson.alternativescene.domain.Client
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ClientRemoteDataSourceImpl(private val clientApiDataSource: ClientApiDataSource): ClientRemoteDataSource {


    override fun getClients(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<Client>>) {
        clientApiDataSource.getClients()
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