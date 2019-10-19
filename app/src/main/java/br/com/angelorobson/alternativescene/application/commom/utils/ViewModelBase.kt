package br.com.angelorobson.alternativescene.application.commom.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.angelorobson.alternativescene.application.EventLiveData

open class BaseViewModel<T> : ViewModel() {

    val successObserver = MutableLiveData<EventLiveData<T>>()
    var emptyObserver = MutableLiveData<EventLiveData<Boolean>>()
    val errorObserver = MutableLiveData<EventLiveData<String>>()
    val isLoadingObserver = MutableLiveData<Boolean>()

    fun loadingStarted() {
        isLoadingObserver.value = true
    }

    fun loadingFinished() {
        isLoadingObserver.value = false
    }

}