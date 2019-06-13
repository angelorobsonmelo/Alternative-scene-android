package br.com.angelorobson.alternativescene.application.commom.utils

import androidx.databinding.InverseMethod


object Converter {

    @JvmStatic
    @InverseMethod("convertIntToString")
    fun convertStringToInt(value: String): Int {
        try {
            return Integer.parseInt(value)
        } catch (e: NumberFormatException) {
            return -1
        }

    }

    @JvmStatic
    fun convertIntToString(value: Int): String {
        return value.toString()
    }
}