package br.com.angelorobson.alternativescene.application.commom.utils.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap?.encodeTobase64(): String? {
    val baos = ByteArrayOutputStream()
    this?.compress(Bitmap.CompressFormat.PNG, 90, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}