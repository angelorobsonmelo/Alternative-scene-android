package br.com.soluevo.cobrei.application.modules.login

import android.view.View
import br.com.soluevo.cobrei.domain.request.AuthRequest

interface LoginHandler {

    fun onPressLoginButton(authRequest: AuthRequest)
    fun onPressFacebookButton(view: View)

}