package br.com.angelorobson.alternativescene.application.partials.events.event

import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.alternativescene.application.EventLiveData
import br.com.angelorobson.alternativescene.application.commom.utils.BaseViewModel
import br.com.angelorobson.alternativescene.domain.Event
import br.com.angelorobson.alternativescene.domain.Favorite
import br.com.angelorobson.alternativescene.domain.request.FavoriteRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import br.com.angelorobson.alternativescene.service.remote.events.EventsApiDataSource
import br.com.angelorobson.alternativescene.service.remote.favorite.FavoriteApiRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val apiDataSource: EventsApiDataSource,
    private val favoriteApiRemoteDataSource: FavoriteApiRemoteDataSource
) : BaseViewModel<ResponseBase<Event>>() {

    val disposables = CompositeDisposable()
    val successFavoriteObserver = MutableLiveData<EventLiveData<Favorite>>()
    val successDisfavourObserver = MutableLiveData<EventLiveData<Favorite>>()

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

        this.disposables.add(disposable)
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
                        successDisfavourObserver.value = EventLiveData(it.data!!)
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