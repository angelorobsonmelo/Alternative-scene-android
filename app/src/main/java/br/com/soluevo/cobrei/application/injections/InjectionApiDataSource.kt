package br.com.soluevo.cobrei.application.injections

import br.com.soluevo.cobrei.service.ApiDataSource.Companion.createService
import br.com.soluevo.cobrei.service.remote.auth.AuthApiDataSource

object InjectionApiDataSource {

    @JvmStatic
    fun provideAuthApiDataSource(): AuthApiDataSource {
        return createService(AuthApiDataSource::class.java)
    }

}