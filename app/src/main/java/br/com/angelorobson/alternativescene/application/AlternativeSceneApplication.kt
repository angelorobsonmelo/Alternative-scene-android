package br.com.angelorobson.alternativescene.application

import android.app.Application
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.di.components.application.DaggerApplicationComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.usecases.local.SessionUseCase
import com.google.android.libraries.places.api.Places

class AlternativeSceneApplication : Application() {

    companion object {

        lateinit var mSessionUseCase: SessionUseCase
        lateinit var instance: AlternativeSceneApplication
    }

    override fun onCreate() {
        super.onCreate()
        setUpInjections()
        instance = this

        val isNotInitialized = !Places.isInitialized()
        if (isNotInitialized) {
            Places.initialize(applicationContext, getString(R.string.google_places_api_key))
        }
    }

    private fun setUpInjections() {
        val applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build()

        mSessionUseCase = applicationComponent.getSessionUseCase()
    }

}