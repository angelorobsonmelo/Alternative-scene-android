package br.com.angelorobson.alternativescene.application.partials.spread

import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.alternativescene.application.Event
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.request.UserRequest
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import br.com.angelorobson.alternativescene.service.remote.user.UserApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class SpreadViewModel @Inject constructor(
    private val apiDataSource: UserApiDataSource
) :
    BaseViewModel<AuthResponse>() {

    val compositeDisposable = CompositeDisposable()
    val userNotFoundObserver = MutableLiveData<Event<String>>()

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
                                Event(it.data!!)
                            return@subscribe
                        }

                        userNotFoundObserver.value = Event("")
                    }
                },
                {
                    errorObserver.value =
                        Event(it.localizedMessage)
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
                            Event(this)
                    }
                },
                {
                    errorObserver.value =
                        Event(it.localizedMessage)
                }
            )

        compositeDisposable.add(disposable)
    }

}