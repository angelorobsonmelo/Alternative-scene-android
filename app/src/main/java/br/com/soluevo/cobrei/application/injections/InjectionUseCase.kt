package br.com.soluevo.cobrei.application.injections

import android.content.Context
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase
import br.com.soluevo.cobrei.application.usecases.remote.auth.AuthUseCase

object InjectionUseCase {

    private val mAuthRemoteDataSource = InjectionRemoteDataSource.provideAuthDataSource()

    @JvmStatic
    fun provideAuthseCase(): AuthUseCase {
        return AuthUseCase(mAuthRemoteDataSource)
    }

    @JvmStatic
    fun provideSessionUseCase(context: Context): SessionUseCase {
        val sessionLocalDataSource = InjectionLocalDataSource.provideSessionLocalDataSource(context)
        return SessionUseCase(sessionLocalDataSource)
    }

}