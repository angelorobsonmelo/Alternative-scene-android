package br.com.angelorobson.alternativescene.application.partials.events.events.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelFactory
import br.com.angelorobson.alternativescene.application.commom.di.utils.ViewModelKey
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EventsViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    internal abstract fun eventsViewModel(eventsViewModel: EventsViewModel): ViewModel

}