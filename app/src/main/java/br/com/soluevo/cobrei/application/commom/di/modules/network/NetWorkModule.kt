package br.com.soluevo.cobrei.application.commom.di.modules.network

import android.content.Context
import br.com.soluevo.cobrei.BuildConfig
import br.com.soluevo.cobrei.application.commom.di.modules.application.ApplicationModule
import br.com.soluevo.cobrei.service.CustomInterceptorRequest
import br.com.soluevo.cobrei.service.TokenAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module(includes = [ApplicationModule::class])
class NetWorkModule {

    @Provides
    fun provideFile(context: Context): File {
        val file = File(context.filesDir, "cache_dir")
        val isNotExists = !file.exists()
        if (isNotExists)
            file.mkdirs()
        return file
    }

    @Provides
    fun provideCache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000)  // 10 MiB cache
    }

    @Provides
    fun provideCustomInterceptorRequest(): CustomInterceptorRequest {
        return CustomInterceptorRequest()
    }

    @Provides
    fun provideTokenAuthenticator(): TokenAuthenticator {
        return TokenAuthenticator()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        customInterceptorRequest: CustomInterceptorRequest,
        tokenAuthenticator: TokenAuthenticator,
        cache: Cache
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)

        httpClient.addInterceptor(customInterceptorRequest)
        httpClient.authenticator(tokenAuthenticator)
        httpClient.addInterceptor(httpLoggingInterceptor)
        httpClient.cache(cache)
        return httpClient.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }


}