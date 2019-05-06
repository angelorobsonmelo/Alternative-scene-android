package br.com.soluevo.cobrei.application

import android.app.Application
import br.com.soluevo.cobrei.application.commom.di.components.application.DaggerApplicationComponent
import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase

class CobreiApplication : Application() {

    companion object {

        lateinit var mSessionUseCase: SessionUseCase
        lateinit var instance: CobreiApplication
    }

    override fun onCreate() {
        super.onCreate()
        setUpInjections()
        instance = this
    }

    private fun setUpInjections() {
        val applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build()

        mSessionUseCase = applicationComponent.getSessionUseCase()
    }

}