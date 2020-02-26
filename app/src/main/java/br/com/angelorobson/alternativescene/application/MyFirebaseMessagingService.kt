package br.com.angelorobson.alternativescene.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants
import br.com.angelorobson.alternativescene.application.partials.events.event.EventActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var data: MutableMap<String, String>? = null
    private var randomNumber: Int = 0


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        randomNumber = ThreadLocalRandom.current().nextInt()
        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            data = remoteMessage.data as MutableMap<String, String>

            val bitmap = getBitmapfromUrl(data!!["imageThumbUrl"] as String)
            val pendingIntent = getPendingIntent()
            buildNotification(pendingIntent, bitmap)
        }
    }


    private fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        val idEvent = data!!["id"]?.toLong()

        val backIntent = Intent(this, NavigationHostActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val notifyIntent = Intent(this, EventActivity::class.java).apply {
            putExtra(Constants.EventsContants.EVENT_ID_EXTRA, idEvent)
        }

        val resultPendingIntent = PendingIntent.getActivities(
            baseContext,
            randomNumber,
            arrayOf(backIntent, notifyIntent),
            PendingIntent.FLAG_ONE_SHOT
        )
        return resultPendingIntent
    }

    private fun buildNotification(
        resultPendingIntent: PendingIntent?,
        bitmap: Bitmap?
    ) {
        val uuid = UUID.randomUUID().toString()
        val eventInfo = "{0}. {1}"
        val eventMessageText =
            MessageFormat.format(eventInfo, data!!["eventDate"], data!!["eventLocation"])


        val notificationBuilder = NotificationCompat.Builder(this, uuid)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.new_event_add))
            .setContentText(eventMessageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null)
            )
            .setGroup("1")
            .setContentIntent(resultPendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setChannelId(uuid)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // For Oreo and greater than it, we required Notification Channel.
            val name: CharSequence =
                uuid
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(
                    uuid,
                    name,
                    importance
                ) //Create No
            // tification Channel
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(
            randomNumber,
            notificationBuilder.build()
        )
    }

}