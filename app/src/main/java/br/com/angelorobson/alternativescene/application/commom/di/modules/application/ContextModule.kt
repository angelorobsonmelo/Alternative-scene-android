package br.com.angelorobson.alternativescene.application.commom.di.modules.application

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ContextModule (private val context: Context) {

    @Provides
    @Singleton
    @Named("ApplicationContext")
    fun provideContext(): Context {
        return context
    }

}