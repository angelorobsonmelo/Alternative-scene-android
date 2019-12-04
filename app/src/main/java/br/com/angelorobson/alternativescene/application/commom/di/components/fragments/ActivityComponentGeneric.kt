package br.com.angelorobson.alternativescene.application.commom.di.components.fragments

import br.com.angelorobson.alternativescene.application.commom.di.modules.generic.GenericModule
import br.com.angelorobson.alternativescene.application.partials.events.create.EventFormFragment
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [GenericModule::class])
interface ActivityComponentGeneric {

    fun inject(signInActivity: SignInActivity)
}