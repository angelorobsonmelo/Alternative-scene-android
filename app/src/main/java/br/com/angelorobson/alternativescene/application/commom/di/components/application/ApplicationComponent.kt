package br.com.angelorobson.alternativescene.application.commom.di.components.application

import android.app.Application
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ApplicationModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.usecases.local.SessionUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ContextModule::class])
interface ApplicationComponent {

    fun inject(application: Application)
    fun getSessionUseCase(): SessionUseCase

}