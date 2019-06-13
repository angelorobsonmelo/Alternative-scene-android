package br.com.angelorobson.alternativescene.service.remote.client

import br.com.angelorobson.alternativescene.domain.Client
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource

interface ClientRemoteDataSource {

    fun getClients(callback: BaseRemoteDataSource.RemoteDataSourceCallback<MutableList<Client>>)

}