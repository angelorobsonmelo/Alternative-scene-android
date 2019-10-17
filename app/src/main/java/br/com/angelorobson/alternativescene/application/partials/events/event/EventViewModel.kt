package br.com.angelorobson.alternativescene.application.partials.events.event

import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val apiDataSource: EventsApiDataSource
) : BaseViewModel<ResponseBase<Event>>() {

    val disposable = CompositeDisposable()

    fun getEvent(id: Long) {
        val disposable = apiDataSource.getEvent(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStarted() }
            .doAfterTerminate { loadingFinished() }
            .subscribe(
                {
                    successObserver.value =
                        EventLiveData(it)
                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        this.disposable.add(disposable)
    }

}