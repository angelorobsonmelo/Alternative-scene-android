package br.com.soluevo.cobrei.application.injections

import android.content.Context
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSource
import br.com.soluevo.cobrei.service.local.session.SessionLocalDataSourceImpl


object InjectionLocalDataSource {

    @JvmStatic
    fun provideSessionLocalDataSource(context: Context): SessionLocalDataSource {
        return SessionLocalDataSourceImpl.getInstance(context)
    }
}