package br.com.angelorobson.alternativescene.application.partials.userdevice

import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.request.UserDeviceRequest
import br.com.angelorobson.alternativescene.service.remote.userdevice.UserDeviceApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDeviceViewModel @Inject constructor(
    private val userDeviceApiDataSource: UserDeviceApiDataSource
) : BaseViewModel<Boolean>() {

    val disposables = CompositeDisposable()

    fun saveUserDevice(userDeviceRequest: UserDeviceRequest) {
        val disposable = userDeviceApiDataSource.saveUserDevice(userDeviceRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStarted() }
            .doAfterTerminate { loadingFinished() }
            .subscribe(
                {
                    successObserver.value = EventLiveData(it.data!!)
                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        disposables.add(disposable)
    }

}