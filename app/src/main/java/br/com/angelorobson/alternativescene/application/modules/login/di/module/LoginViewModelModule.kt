package br.com.angelorobson.alternativescene.application.modules.login.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelFactory
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelKey
import br.com.angelorobson.alternativescene.application.modules.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun usersViewModel(viewModel: LoginViewModel): ViewModel

}