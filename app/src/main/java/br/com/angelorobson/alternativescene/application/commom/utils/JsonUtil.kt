package br.com.angelorobson.alternativescene.application.commom.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.internal.`$Gson$Types`.newParameterizedTypeWithOwner
import java.io.InputStream


object JsonUtil {

    @JvmStatic
    fun <T> getListEntityFromJsonFile(inputStream: InputStream, clazz: Class<T>): List<T> {
        val json = inputStream.bufferedReader().use { it.readText() }
        val parser = JsonParser()
        val array = parser.parse(json).asJsonArray

        val type =
            newParameterizedTypeWithOwner(null, ArrayList::class.java, clazz)

        return Gson().fromJson(array, type)
    }

    @JvmStatic
    fun <T> jsonToListObject(resources: Resources, @RawRes resourceId: Int, clazz: Class<T>): List<T> {
        val json = resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        val parser = JsonParser()
        val array = parser.parse(json).asJsonArray

        val type =
            newParameterizedTypeWithOwner(null, ArrayList::class.java, clazz)

        return Gson().fromJson(array, type)
    }

}