package br.com.angelorobson.alternativescene.application.commom.di.modules.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.angelorobson.alternativescene.LoginViewModel
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelFactory
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelKey
import br.com.angelorobson.alternativescene.application.partials.events.create.EventFormViewModel
import br.com.angelorobson.alternativescene.application.partials.events.event.EventViewModel
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsViewModel
import br.com.angelorobson.alternativescene.application.partials.events.favorite.FavoriteViewModel
import br.com.angelorobson.alternativescene.application.partials.signin.SignInViewModel
import br.com.angelorobson.alternativescene.application.partials.userdevice.UserDeviceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    internal abstract fun eventsViewModel(eventsViewModel: EventsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    internal abstract fun eventViewModel(eventViewModel: EventViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun spreadViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventFormViewModel::class)
    internal abstract fun eventFormViewModel(eventFormViewModel: EventFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun favoriteViewModel(eventFormViewModel: FavoriteViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(eventFormViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDeviceViewModel::class)
    internal abstract fun userDeviceViewModel(userDeviceViewModel: UserDeviceViewModel): ViewModel


}