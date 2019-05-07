package br.com.soluevo.cobrei.application.commom.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

open class FragmentBase: Fragment() {

    fun showAlert(message: String) {
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
        val isLogged = CobreiApplication.mSessionUseCase.isLogged()
        if (isLogged) {
            goToMainScreen()
        }
    }

     fun goToMainScreen() {
        view?.findNavController()?.navigate(R.id.accountFragment)
    }*/

}