package br.com.soluevo.cobrei.application

import android.app.Application
import android.content.Intent
import br.com.soluevo.cobrei.application.commom.di.components.application.DaggerApplicationComponent
import br.com.soluevo.cobrei.application.commom.di.modules.application.ApplicationModule
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase

class CobreiApplication : Application() {

    companion object {

        var mSessionUseCase: SessionUseCase? = null
        lateinit var instance: CobreiApplication
    }

    override fun onCreate() {
        super.onCreate()
        setUpInjections()
        instance = this
    }

    private fun setUpInjections() {
        val applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        mSessionUseCase = applicationComponent.getSessionUseCase()
    }

}