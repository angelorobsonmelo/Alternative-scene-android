package br.com.angelorobson.alternativescene.application.usecases

import br.com.angelorobson.alternativescene.application.usecases.remote.events.GetEventsUseCase
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import br.com.angelorobson.alternativescene.utils.SetupRemoteUtils
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

@RunWith(Enclosed::class)
class GetEventsUseCaseTest {

    internal abstract class Context_Setup_test : SetupRemoteUtils() {

        @InjectMocks
        lateinit var getEventsUseCase: GetEventsUseCase

        @Mock
        lateinit var eventsApiDataSource: EventsApiDataSource

        @Mock
        lateinit var callback: UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>

    }

    internal class Context_success : Context_Setup_test() {

        @Mock
        private lateinit var event: ResponseListBase<Event>

        @Mock
        private lateinit var eventFilter: EventFilter

        private val page = 0

        @Before
        fun setUp() {
            val observable = Observable.just(event)

            given(
                eventsApiDataSource.getEvent(
                    com.nhaarman.mockitokotlin2.eq(eventFilter),
                    com.nhaarman.mockitokotlin2.eq(page)
                )
            ).willReturn(observable)

            getEventsUseCase.getAll(eventFilter, page, callback)
        }

        @Test
        fun `It should call on success callback`() {
            verify(callback, times(1)).onSuccess(event)
        }

        @Test
        fun `it should call never interaction with error`() {
            verify(callback, never()).onError(com.nhaarman.mockitokotlin2.any())
        }

        @Test
        fun `it should call always have 2 interactions with is loading callback`() {
            BDDMockito.verify(callback, BDDMockito.times(2)).isLoading(com.nhaarman.mockitokotlin2.any())
        }

    }

}