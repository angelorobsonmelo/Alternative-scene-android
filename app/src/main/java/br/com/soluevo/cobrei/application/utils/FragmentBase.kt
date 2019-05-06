package br.com.soluevo.cobrei.application.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.CobreiApplication

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

    override fun onResume() {
        super.onResume()
        checkIfUserIsLogged()
    }

     fun checkIfUserIsLogged() {
        val isLogged = CobreiApplication.mSessionUseCase.isLogged()
        if (isLogged) {
            goToMainScreen()
        }
    }

     fun goToMainScreen() {
        view?.findNavController()?.navigate(R.id.toAccount)
    }

}