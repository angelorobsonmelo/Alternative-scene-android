package br.com.soluevo.cobrei.application.modules.login.di.module

import br.com.soluevo.cobrei.application.commom.di.modules.network.NetWorkModule
import br.com.soluevo.cobrei.application.usecases.remote.auth.AuthUserAndSaveInSessionUseCase
import br.com.soluevo.cobrei.service.remote.auth.AuthApiDataSource
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSource
import br.com.soluevo.cobrei.service.remote.auth.AuthRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module(includes = [NetWorkModule::class, LoginViewModelModule::class, LoginAbstractModule::class])
class LoginModule {

    @Provides
    fun provideAuthApiDataSource(retrofit: Retrofit): AuthApiDataSource {
        return retrofit.create<AuthApiDataSource>(AuthApiDataSource::class.java)
    }

    @Provides
    fun provideAuthRemoteDataSourceImpl(authApiDataSource: AuthApiDataSource): AuthRemoteDataSourceImpl {
        return AuthRemoteDataSourceImpl(authApiDataSource)
    }

    @Provides
    fun provideAuthUserAndSaveInSessionUseCase(authRemoteDataSource: AuthRemoteDataSource): AuthUserAndSaveInSessionUseCase {
        return AuthUserAndSaveInSessionUseCase(authRemoteDataSource)
    }

}