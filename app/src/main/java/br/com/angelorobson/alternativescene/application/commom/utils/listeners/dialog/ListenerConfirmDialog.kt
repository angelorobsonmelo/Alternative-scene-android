package br.com.angelorobson.alternativescene.application.commom.utils.listeners.dialog

import android.content.DialogInterface

interface ListenerConfirmDialog {

    fun onPressPositiveButton(dialog: DialogInterface, id: Int)
    fun onPressNegativeButton(dialog: DialogInterface, id: Int)
}