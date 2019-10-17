package br.com.angelorobson.alternativescene.application.partials.events.create

import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.request.EventRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventFormViewModel @Inject constructor(
    private val apiDataSource: EventsApiDataSource
) : BaseViewModel<ResponseBase<Event>>() {

    val disposable = CompositeDisposable()

    fun save(eventRequest: EventRequest) {
        val disposable = apiDataSource.save(eventRequest)
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
                    EventLiveData(it.localizedMessage)
                }
            )

    }
}