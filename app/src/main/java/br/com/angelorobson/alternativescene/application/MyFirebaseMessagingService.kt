package br.com.angelorobson.alternativescene.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.angelorobson.alternativescene.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)


        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.heavy_metal_default)

            val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(bitmap)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId("channel_id")
                .setAutoCancel(true)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notificationBuilder.build())


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // For Oreo and greater than it, we required Notification Channel.
                val name: CharSequence =
                    "My New Channel" // The user-visible name of the channel.
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel =
                    NotificationChannel(
                        "channel_id",
                        name,
                        importance
                    ) //Create No
                // tification Channel
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(
                0 /* ID of notification */,
                notificationBuilder.build()
            )
        }


    }

//    private fun sendNotification(title: String, body: String) {
////        val i = Intent(this, Host::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pi = PendingIntent.getActivity(
//            this,
//            0 /* Request code */,
//            i,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder =
//            NotificationCompat.Builder(
//                this,
//                getString(R.string.default_notification_channel_id)
//            )
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(sound)
//                .setContentIntent(pi)
//        val manager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.notify(0, builder.build())
//    }


}