package br.com.angelorobson.alternativescene.application.commom.di.components.fragments

import br.com.angelorobson.alternativescene.application.commom.di.modules.generic.GenericModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.recyclerview.RecyclerViewAnimatedWithDividerModule
import br.com.angelorobson.alternativescene.application.partials.events.events.EventsFragment
import br.com.angelorobson.alternativescene.application.partials.events.favorite.FavoriteFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [GenericModule::class, RecyclerViewAnimatedModule::class])
interface FragmentGenericWithRecyclerViewAnimatedWithoutDividerComponent {

    fun inject(favoriteFragment: FavoriteFragment)

}