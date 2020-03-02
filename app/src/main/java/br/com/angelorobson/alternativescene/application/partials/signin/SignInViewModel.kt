package br.com.angelorobson.alternativescene.application.partials.signin

import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.request.UserDeviceRequest
import br.com.angelorobson.alternativescene.domain.request.UserRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.remote.user.UserApiDataSource
import br.com.angelorobson.alternativescene.service.remote.userdevice.UserDeviceApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class SignInViewModel @Inject constructor(
    private val apiDataSource: UserApiDataSource,
    private val userDeviceApiDataSource: UserDeviceApiDataSource
) :
    BaseViewModel<AuthResponse>() {

    val compositeDisposable = CompositeDisposable()
    val userNotFoundObserver = MutableLiveData<EventLiveData<String>>()

    fun getUserByEmailAndGoogleAccountId(email: String, googleAccountId: String) {
        val disposable = apiDataSource.findByEmailAndGoogleAccountId(email, googleAccountId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoadingObserver.value = true }
            .doAfterTerminate { isLoadingObserver.value = false }
            .subscribe(
                {
                    it?.apply {
                        this.data?.token?.apply {
                            successObserver.value =
                                EventLiveData(it.data!!)
                            return@subscribe
                        }

                        userNotFoundObserver.value = EventLiveData("")
                    }
                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        compositeDisposable.add(disposable)
    }

    fun save(userRequest: UserRequest) {
        val disposable = apiDataSource.save(userRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStarted() }
            .doAfterTerminate { loadingFinished() }
            .subscribe(
                {
                    it.data?.apply {
                        successObserver.value =
                            EventLiveData(this)
                    }
                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        compositeDisposable.add(disposable)
    }

}