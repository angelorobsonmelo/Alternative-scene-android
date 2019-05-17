package br.com.soluevo.cobrei.service.remote.client

import br.com.soluevo.cobrei.domain.Client
import br.com.soluevo.cobrei.service.BaseRemoteDataSource

interface ClientRemoteDataSource {

    fun getClients(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<Client>>)

}