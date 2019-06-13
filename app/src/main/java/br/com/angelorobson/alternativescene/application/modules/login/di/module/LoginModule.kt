package br.com.angelorobson.alternativescene.application.modules.login.di.module

import br.com.angelorobson.alternativescene.application.commom.di.modules.network.NetWorkModule
import br.com.angelorobson.alternativescene.application.usecases.remote.auth.AuthUserAndSaveInSessionUseCase
import br.com.angelorobson.alternativescene.service.remote.auth.AuthApiDataSource
import br.com.angelorobson.alternativescene.service.remote.auth.AuthRemoteDataSource
import br.com.angelorobson.alternativescene.service.remote.auth.AuthRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetWorkModule::class, LoginViewModelModule::class, LoginAbstractModule::class])
class LoginModule {

    @Provides
    @Singleton
    fun provideAuthApiDataSource(retrofit: Retrofit): AuthApiDataSource {
        return retrofit.create<AuthApiDataSource>(AuthApiDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSourceImpl(authApiDataSource: AuthApiDataSource): AuthRemoteDataSourceImpl {
        return AuthRemoteDataSourceImpl(authApiDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthUserAndSaveInSessionUseCase(authRemoteDataSource: AuthRemoteDataSource): AuthUserAndSaveInSessionUseCase {
        return AuthUserAndSaveInSessionUseCase(authRemoteDataSource)
    }

}