package br.com.angelorobson.alternativescene.application.usecases.remote.client

import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.domain.Client
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.client.ClientRemoteDataSource

class GetClientsUseCase(private val clientRemoteDataSource: ClientRemoteDataSource) {

    fun getClients(callback: UseCaseBaseCallback.UseCaseCallback<MutableList<Client>>) {
        clientRemoteDataSource.getClients(object : BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<Client>> {
            override fun onSuccess(response: MutableList<Client>) {
                if (response.isNotEmpty()) {
                    callback.onSuccess(response)
                    return
                }

                callback.onEmptyData()
            }

            override fun onError(errorMessage: String) {
                callback.onError(errorMessage)
            }

            override fun isLoading(isLoading: Boolean) {
                callback.isLoading(isLoading)
            }

        })

    }

}