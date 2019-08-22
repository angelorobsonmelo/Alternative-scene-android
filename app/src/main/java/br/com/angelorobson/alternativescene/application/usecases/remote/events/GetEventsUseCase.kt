package br.com.angelorobson.alternativescene.application.usecases.remote.events

import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GetEventsUseCase(private val eventsApiDataSource: EventsApiDataSource) {

    private val disposables = CompositeDisposable()

    fun getAll(
        eventFilter: EventFilter,
        page: Int,
        callback: UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>
    ) {
        val disposable = eventsApiDataSource.getEvent(eventFilter, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

        disposables.add(disposable)
    }

    fun clearDisposable() {
        disposables.clear()
    }
}