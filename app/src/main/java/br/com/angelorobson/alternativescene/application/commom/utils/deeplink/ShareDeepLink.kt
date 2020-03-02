package br.com.angelorobson.alternativescene.application.commom.utils.deeplink

import android.content.Intent
import br.com.angelorobson.alternativescene.domain.Event
import java.text.MessageFormat

object ShareDeepLink {

    fun getDeepLinkIntent(event: Event): Intent {
        val firebaseLink = "https://angelorobsonn.page.link"
        val myLink = "https://www.angelorobson.com?id=${event.id}"
        val myPackage = "br.com.angelorobson.alternativescene"
        val link = "{0}?link={1}&apn={2}"
        val deepLink = MessageFormat.format(link, firebaseLink, myLink, myPackage)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Veja este evento")
        return intent.putExtra(Intent.EXTRA_TEXT, deepLink)
    }
}