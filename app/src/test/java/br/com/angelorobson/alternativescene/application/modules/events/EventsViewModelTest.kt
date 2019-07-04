package br.com.angelorobson.alternativescene.application.modules.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.angelorobson.alternativescene.application.modules.events.events.EventsViewModel
import br.com.angelorobson.alternativescene.application.usecases.UseCaseBaseCallback
import br.com.angelorobson.alternativescene.application.usecases.remote.events.GetEventsUseCase
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.filter.EventFilter
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

@RunWith(Enclosed::class)
class EventsViewModelTest {

    internal abstract class Contex_setup {

        @Rule
        @JvmField
        val rule = InstantTaskExecutorRule()

        @Rule
        @JvmField
        val mockitoRule = MockitoJUnit.rule()!!

        @InjectMocks
        lateinit var eventsViewModel: EventsViewModel

        @Mock
        lateinit var getEventsUseCase: GetEventsUseCase

        val eventFilter = EventFilter(true)
        val page = 0

        @Before
        open fun setUp() {
            eventsViewModel.getEvents(eventFilter, page)
        }

    }

    internal class ContextSuccess : Contex_setup() {

        @Before
        override fun setUp() {
            super.setUp()
            argumentCaptor<UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>>().apply {
                verify(getEventsUseCase, times(1)).getAll(eq(eventFilter), eq(page), capture())
                firstValue.onSuccess(ResponseListBase())
            }
        }

        @Test
        fun `It should call successObserver`() {
            eventsViewModel.successObserver
                .test()
                .assertHasValue()
        }

    }

    internal class Context_Is_Loading : Contex_setup() {

        @Before
       override fun setUp() {
            super.setUp()

            argumentCaptor<UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>>().apply {
                verify(getEventsUseCase, times(1)).getAll(eq(eventFilter), eq(page), capture())
                firstValue.isLoading(true)
            }
        }

        @Test
        fun `It should call isLoadingObserver`() {
            eventsViewModel.isLoadingObserver
                .test()
                .assertValue(true)
        }
    }

    internal class Context_onEmptyData : Contex_setup() {

        @Before
        override fun setUp() {
            super.setUp()

            argumentCaptor<UseCaseBaseCallback.UseCaseCallback<ResponseListBase<Event>>>().apply {
                verify(getEventsUseCase, times(1)).getAll(eq(eventFilter), eq(page), capture())
                firstValue.onEmptyData()
            }
        }

        @Test
        fun `It should call isLoadingObserver`() {
            eventsViewModel.emptyObserver
                .test()
                .assertValue(true)
        }
    }

}