package br.com.soluevo.cobrei.application.modules.collect.di.component

import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.modules.collect.CollectFragment
import br.com.soluevo.cobrei.application.modules.collect.di.modules.CollectModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CollectModule::class, ContextModule::class])
interface CollectComponent {

    fun inject(collectFragment: CollectFragment)
}