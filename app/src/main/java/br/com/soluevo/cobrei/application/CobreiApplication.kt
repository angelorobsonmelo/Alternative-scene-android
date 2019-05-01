package br.com.soluevo.cobrei.application

import android.app.Application
import android.content.Intent
import br.com.soluevo.cobrei.application.injections.InjectionUseCase
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase
import br.com.soluevo.cobrei.application.usecases.remote.auth.AuthUseCase

class CobreiApplication: Application() {

    companion object {
        @JvmStatic
       var mSessionUseCase: SessionUseCase? = null
       var authUseCase: AuthUseCase? = null
        lateinit var instance: CobreiApplication
    }

    override fun onCreate() {
        super.onCreate()
        mSessionUseCase = InjectionUseCase.provideSessionUseCase(applicationContext)
        authUseCase = InjectionUseCase.provideAuthseCase()

        if (mSessionUseCase!!.isLogged()) {
            startActivity(Intent(applicationContext, NavigationHostActivity::class.java))
        }

        instance = this
    }

}