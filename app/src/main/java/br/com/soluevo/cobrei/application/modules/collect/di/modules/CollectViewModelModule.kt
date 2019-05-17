package br.com.soluevo.cobrei.application.modules.collect.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.soluevo.cobrei.application.commom.di.utils.ViewModelFactory
import br.com.soluevo.cobrei.application.commom.di.utils.ViewModelKey
import br.com.soluevo.cobrei.application.modules.collect.CollectViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CollectViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CollectViewModel::class)
    internal abstract fun collectViewModel(collectViewModel: CollectViewModel): ViewModel

}