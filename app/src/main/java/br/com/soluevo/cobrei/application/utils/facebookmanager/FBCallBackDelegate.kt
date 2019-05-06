package br.com.soluevo.cobrei.application.utils.facebookmanager

interface FacebookCallBack {

    fun fbCallbackOnSuccess(userFacebook: UserFacebook)
    fun fbCallbackOnError(localizedMessage: String)
    fun fbCallbackOnCancel()
}
