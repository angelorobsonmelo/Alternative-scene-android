package br.com.angelorobson.alternativescene.application.commom.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.angelorobson.alternativescene.application.Event

open class BaseViewModel<T> : ViewModel() {

    val successObserver = MutableLiveData<Event<T>>()
    var emptyObserver = MutableLiveData<Event<Boolean>>()
    val errorObserver = MutableLiveData<Event<String>>()
    val isLoadingObserver = MutableLiveData<Boolean>()

    fun loadingStarted() {
        isLoadingObserver.value = true
    }

    fun loadingFinished() {
        isLoadingObserver.value = false
    }

}