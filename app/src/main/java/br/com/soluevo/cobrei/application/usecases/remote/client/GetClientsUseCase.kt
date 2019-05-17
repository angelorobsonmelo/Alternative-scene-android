package br.com.soluevo.cobrei.application.usecases.remote.client

import br.com.soluevo.cobrei.application.usecases.UseCaseBaseCallback
import br.com.soluevo.cobrei.domain.Client
import br.com.soluevo.cobrei.service.BaseRemoteDataSource
import br.com.soluevo.cobrei.service.remote.client.ClientRemoteDataSource

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