package br.com.soluevo.cobrei.application.modules.login.di.component

import br.com.soluevo.cobrei.application.modules.login.di.module.LoginModule
import br.com.soluevo.cobrei.application.modules.login.LoginFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoginModule::class])
interface UsersComponent {

    fun inject(loginFragment: LoginFragment)
}