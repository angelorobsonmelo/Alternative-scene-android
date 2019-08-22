package br.com.angelorobson.alternativescene.application.commom.di.modules.network

import android.content.Context
import br.com.angelorobson.alternativescene.BuildConfig
import br.com.angelorobson.alternativescene.service.CustomInterceptorRequest
import br.com.angelorobson.alternativescene.service.TokenAuthenticator
import com.google.gson.FieldNamingPolicy
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
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetWorkModule {

    @Provides
    @Singleton
    fun provideFile(@Named("ApplicationContext") context: Context): File {
        val file = File(context.filesDir, "cache_dir")
        val isNotExists = !file.exists()
        if (isNotExists)
            file.mkdirs()
        return file
    }

    @Provides
    @Singleton
    fun provideCache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000)  // 10 MiB cache
    }

    @Provides
    @Singleton
    fun provideCustomInterceptorRequest(): CustomInterceptorRequest {
        return CustomInterceptorRequest()
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(): TokenAuthenticator {
        return TokenAuthenticator()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
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
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }


}