package br.com.angelorobson.alternativescene.application.modules.collect

import br.com.angelorobson.alternativescene.domain.request.CollectRequest

interface CollectHandler {

    fun onPressShowClientsDialog()
    fun onPressSaveCollect(collectRequest: CollectRequest)
}