package br.com.angelorobson.alternativescene

import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.request.AuthRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.remote.auth.AuthApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authApiDataSource: AuthApiDataSource
) : BaseViewModel<AuthResponse>() {


    val disposables = CompositeDisposable()

    fun login(email: String, password: String) {
        val request = AuthRequest(email, password)
        val disposable = authApiDataSource.auth(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStarted() }
            .doAfterTerminate { loadingFinished() }
            .subscribe(
                {
                    it.data?.apply {
                        successObserver.value = EventLiveData(this)
                    }

                }, {
                    errorObserver.value = EventLiveData(it.localizedMessage)
                }
            )

        disposables.add(disposable)
    }
}