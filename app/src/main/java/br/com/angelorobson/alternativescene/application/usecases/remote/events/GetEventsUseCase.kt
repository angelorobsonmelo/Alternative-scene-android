package br.com.angelorobson.alternativescene.application.usecases.remote.events

import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEventsUseCase(private val eventsApiDataSource: EventsApiDataSource) {

    fun getAll(
        eventFilter: EventFilter,
        page: Int,
        callback: UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>
    ) {
        eventsApiDataSource.getEvent(eventFilter, page)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.isLoading(true) }
            .doAfterTerminate { callback.isLoading(false) }
            .subscribe(
                {
                    callback.onSuccess(it)
                },
                {
                    callback.onError(it.localizedMessage)
                }
            )

    }
}