package br.com.angelorobson.alternativescene.application.commom.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

open class FragmentBase: Fragment() {

    fun showAlertError(message: String) {
        val builder = AlertDialog.Builder(context!!)

        builder
            .setMessage(message.toInt())
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->  }

        val alert = builder.create()
        alert.show()
    }

  /*  override fun onResume() {
        super.onResume()
        checkIfUserIsLogged()
        activity?.bottomNavigation?.visibility = VISIBLE
    }

     fun checkIfUserIsLogged() {
        val isLogged = AlternativeSceneApplication.mSessionUseCase.isLogged()
        if (isLogged) {
            goToMainScreen()
        }
    }

     fun goToMainScreen() {
        view?.findNavController()?.navigate(R.id.accountFragment)
    }*/

}