package br.com.soluevo.cobrei.application.commom.utils.facebookmanager

interface FacebookCallBack {

    fun fbCallbackOnSuccess(userFacebook: UserFacebook)
    fun fbCallbackOnError(localizedMessage: String)
    fun fbCallbackOnCancel()
}
