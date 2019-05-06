package br.com.soluevo.cobrei.application.commom.utils

import android.os.Build
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object RandomUtil {

    @JvmStatic
    fun getRandomNumber(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThreadLocalRandom.current().nextInt(1000, 9000)
        } else {
            Random().nextInt(9000) + 1000
        }
    }
}
