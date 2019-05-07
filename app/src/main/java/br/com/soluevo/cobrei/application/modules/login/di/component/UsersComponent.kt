package br.com.soluevo.cobrei.application.modules.login.di.component

import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.modules.login.LoginFragment
import br.com.soluevo.cobrei.application.modules.login.di.module.LoginModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoginModule::class, ContextModule::class])
interface UsersComponent {

    fun inject(loginFragment: LoginFragment)
}