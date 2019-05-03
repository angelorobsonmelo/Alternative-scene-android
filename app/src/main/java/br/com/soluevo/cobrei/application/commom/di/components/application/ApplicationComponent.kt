package br.com.soluevo.cobrei.application.commom.di.components.application

import android.app.Application
import br.com.soluevo.cobrei.application.commom.di.modules.application.ApplicationModule
import br.com.soluevo.cobrei.application.usecases.local.SessionUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: Application)
    fun getSessionUseCase(): SessionUseCase

}