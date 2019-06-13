package br.com.angelorobson.alternativescene.application.modules.login

import android.view.View
import br.com.angelorobson.alternativescene.domain.request.AuthRequest

interface LoginHandler {

    fun onPressLoginButton(authRequest: AuthRequest)
    fun onPressFacebookButton(view: View)

}