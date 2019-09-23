package br.com.angelorobson.alternativescene.application.commom.di.modules.generic

import br.com.angelorobson.alternativescene.application.commom.di.modules.api.ApiModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.commom.di.modules.viewmodel.ViewModelModule
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import br.com.angelorobson.alternativescene.service.remote.user.UserApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetWorkModule::class, ViewModelModule::class, ApiModule::class])
class GenericModule