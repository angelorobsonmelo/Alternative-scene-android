package br.com.angelorobson.alternativescene.application

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.di.components.application.DaggerApplicationComponent
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.usecases.local.SessionUseCase
import br.com.angelorobson.alternativescene.service.local.event.EventLocalDataSource
import br.com.angelorobson.alternativescene.service.local.session.SessionLocalDataSource
import br.com.angelorobson.alternativescene.service.remote.auth.AuthApiDataSource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

class AlternativeSceneApplication : Application() {

    companion object {

        lateinit var mSessionUseCase: SessionUseCase
        lateinit var mEventLocalDataSource: EventLocalDataSource
        lateinit var mAuthApiDataSource: AuthApiDataSource
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

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }

    private fun setUpInjections() {
        val applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build()

        mSessionUseCase = applicationComponent.getSessionUseCase()
        mEventLocalDataSource = applicationComponent.getEventLocalDataSource()
        mAuthApiDataSource = applicationComponent.getAuthApiDataSource()
    }

}