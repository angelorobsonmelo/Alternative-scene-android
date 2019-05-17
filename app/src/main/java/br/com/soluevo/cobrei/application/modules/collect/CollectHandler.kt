package br.com.soluevo.cobrei.application.modules.collect

import br.com.soluevo.cobrei.domain.request.CollectRequest

interface CollectHandler {

    fun onPressShowClientsDialog()
    fun onPressSaveCollect(collectRequest: CollectRequest)
}