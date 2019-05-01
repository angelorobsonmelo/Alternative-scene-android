package br.com.soluevo.cobrei.application.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T>: ViewModel() {

    val successObserver   = MutableLiveData<T>()
    var emptyObserver     = MutableLiveData<String>()
    val errorObserver     = MutableLiveData<String>()
    val isLoadingObserver = MutableLiveData<Boolean>()

}