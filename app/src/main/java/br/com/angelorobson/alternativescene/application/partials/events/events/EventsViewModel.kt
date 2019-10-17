package br.com.angelorobson.alternativescene.application.partials.events.events

import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.Favorite
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseListBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import br.com.angelorobson.alternativescene.service.remote.favorite.FavoriteApiRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val eventsApiDataSource: EventsApiDataSource,
    private val favoriteApiRemoteDataSource: FavoriteApiRemoteDataSource
) : BaseViewModel<ResponseListBase<Event>>() {

    val disposables = CompositeDisposable()
    val successFavoriteObserver = MutableLiveData<EventLiveData<Favorite>>()
    val successdisfavourObserver = MutableLiveData<EventLiveData<Favorite>>()

    fun getEvents(
        page: Int = 0
    ) {
        val disposable = eventsApiDataSource.getEvent(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoadingObserver.value = true }
            .doAfterTerminate { isLoadingObserver.value = false }
            .subscribe(
                {
                    if (it.data?.content?.isNotEmpty()!!) {
                        successObserver.value =
                            EventLiveData(it)
                        return@subscribe
                    }

                    emptyObserver.value =
                        EventLiveData(true)

                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        disposables.add(disposable)
    }

    fun favor(favoriteRequest: FavoriteRequest) {
        val disposable = favoriteApiRemoteDataSource.favor(favoriteRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoadingObserver.value = true }
            .doAfterTerminate { isLoadingObserver.value = false }
            .subscribe(
                {
                    it.data?.event?.apply {
                        successFavoriteObserver.value = EventLiveData(it.data!!)
                    } ?: run {
                        successdisfavourObserver.value = EventLiveData(it.data!!)
                    }

                },
                {
                    errorObserver.value =
                        EventLiveData(it.localizedMessage)
                }
            )

        disposables.add(disposable)
    }

}
