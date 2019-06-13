package br.com.angelorobson.alternativescene.application.commom.utils.extensions

import java.math.BigInteger

fun String.extractNumbers(): BigInteger {
   return  this.replace(Regex("[^0-9]"), "").toBigInteger()
}

fun String?.getFileName(): String? {
   return  this?.substring(this.lastIndexOf(".") + 1)
}

fun String?.getFileExntesion(): String? {
   return  this?.substring(this.lastIndexOf(".") + 1)
}

