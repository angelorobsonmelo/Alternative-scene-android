package br.com.angelorobson.alternativescene.application.partials.spread.di.component

import br.com.angelorobson.alternativescene.application.commom.di.modules.application.ContextModule
import br.com.angelorobson.alternativescene.application.partials.spread.SpreadFragment
import br.com.angelorobson.alternativescene.application.partials.spread.di.module.SpreadModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SpreadModule::class, ContextModule::class])
interface SpreadComponent {

    fun inject(spreadFragment: SpreadFragment)
}