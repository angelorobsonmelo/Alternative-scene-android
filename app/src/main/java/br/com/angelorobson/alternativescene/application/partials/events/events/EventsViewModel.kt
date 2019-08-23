package br.com.angelorobson.alternativescene.application.partials.events.events

import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val eventsApiDataSource: EventsApiDataSource
) : BaseViewModel<ResponseListBase<Event>>() {

    val disposables = CompositeDisposable()

    fun getEvents(
        eventFilter: EventFilter = EventFilter(true),
        page: Int = 0
    ) {
        val disposable = eventsApiDataSource.getEvent(eventFilter, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoadingObserver.value = true }
            .doAfterTerminate { isLoadingObserver.value = false }
            .subscribe(
                {
                    if (it.data?.content?.isNotEmpty()!!) {
                        successObserver.value =
                            br.com.angelorobson.alternativescene.application.Event(it)
                        return@subscribe
                    }

                    emptyObserver.value =
                        br.com.angelorobson.alternativescene.application.Event(true)

                },
                {
                    errorObserver.value =
                        br.com.angelorobson.alternativescene.application.Event(it.localizedMessage)
                }
            )

        disposables.add(disposable)
    }

}
