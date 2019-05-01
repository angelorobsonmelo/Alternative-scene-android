package br.com.soluevo.cobrei.application.injections

import br.com.soluevo.cobrei.application.injections.InjectionApiDataSource.provideAuthApiDataSource
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSource
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSourceImpl.Companion.getInstance

object InjectionRemoteDataSource {

    @JvmStatic
    fun provideAuthDataSource(): AuthRemoteDataSource {
        return getInstance(provideAuthApiDataSource())
    }

}