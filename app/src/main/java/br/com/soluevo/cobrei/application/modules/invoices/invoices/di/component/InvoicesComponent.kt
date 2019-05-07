package br.com.soluevo.cobrei.application.modules.invoices.invoices.di.component

import br.com.soluevo.cobrei.application.commom.di.modules.application.ContextModule
import br.com.soluevo.cobrei.application.modules.invoices.invoices.InvoicesFragment
import br.com.soluevo.cobrei.application.modules.invoices.invoices.di.modules.InvoicesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [InvoicesModule::class, ContextModule::class])
interface InvoicesComponent {

    fun inject(invoicesFragment: InvoicesFragment)
}