package br.com.angelorobson.alternativescene.application

import android.app.Application
import br.com.angelorobson.alternativescene.application.commom.di.components.application.DaggerApplicationComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.usecases.local.SessionUseCase

class AlternativeSceneApplication : Application() {

    companion object {

        lateinit var mSessionUseCase: SessionUseCase
        lateinit var instance: AlternativeSceneApplication
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