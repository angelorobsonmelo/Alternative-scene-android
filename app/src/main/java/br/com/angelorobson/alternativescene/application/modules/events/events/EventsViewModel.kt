package br.com.angelorobson.alternativescene.application.modules.events.events

import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.application.usecases.remote.events.GetEventsUseCase
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val eventsUseCase: GetEventsUseCase
) : BaseViewModel<ResponseListBase<Event>>() {

    fun getEvents(eventFilter: EventFilter = EventFilter(true), page: Int = 0) {
        eventsUseCase.getAll(
            eventFilter,
            page,
            object : UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>> {
                override fun onSuccess(response: ResponseListBase<Event>) {
                    successObserver.value = response
                }

                override fun onEmptyData() {
                    emptyObserver.value = true
                }

                override fun isLoading(isLoading: Boolean) {
                    isLoadingObserver.value = isLoading
                }

                override fun onError(errorDescription: String) {
                    errorObserver.value = errorDescription
                }

            })
    }

    fun clearDisposable() {
        eventsUseCase.clearDisposable()
    }

}
