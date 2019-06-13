package br.com.angelorobson.alternativescene.service.remote.auth

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.BaseRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthRemoteDataSourceImpl(private val mAuthApiDataSource: AuthApiDataSource) : AuthRemoteDataSource {

    companion object {

        @Volatile
        private var INSTANCE: AuthRemoteDataSourceImpl? = null

        fun getInstance(@NonNull mAuthApiDataSource: AuthApiDataSource): AuthRemoteDataSourceImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthRemoteDataSourceImpl(mAuthApiDataSource).also {
                    INSTANCE = it
                }
            }
    }

    @SuppressLint("CheckResult")
    override fun auth(authRequest: AuthRequest, callback: BaseRemoteDataSource.RemoteDataSourceCallback<AuthResponse>) {
        mAuthApiDataSource.auth(authRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.isLoading(true) }
            .doAfterTerminate { callback.isLoading(false) }
            .subscribe(
                {
                    callback.onSuccess(it)
                },
                { throwable ->
                    callback.onError(throwable.localizedMessage)
                }
            )
    }
}