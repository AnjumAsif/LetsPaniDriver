package com.example.letspaanidriver.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.Home
import com.example.letspaanidriver.activity.OrderCancelledByUser
import com.example.letspani.utils.UserShared
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Created by Aniket Sharma on 28-May-19.
 * as5560811@gmail.com
 */
class FirebaseRecived : FirebaseMessagingService() {

    internal var intent: Intent? = null
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    val NOTIFICATION_CHANNEL_ID = "10001"

    var NOTIFICATION_for = ""
    companion object {
        var OrderID: String = ""
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)


        Log.i("Firebase", " your token  $p0")
        UserShared.setFirebaseToken(this@FirebaseRecived, p0!!)

    }


    @SuppressLint("WrongConstant")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        OrderID = remoteMessage?.notification?.tag!!



        Log.i("asdf","notificatrionrecived======data===="+remoteMessage!!);

        Log.v("Aniket", "${remoteMessage?.notification?.tag}    ${remoteMessage?.notification?.body}")

        Log.i("asdf","notificatrionrecived======body===="+remoteMessage?.notification!!.body);
        Log.i("asdf","notificatrionrecived======tag===="+remoteMessage?.notification?.tag);

        Log.i("asdf","notificatrionrecived======channelId===="+remoteMessage?.notification!!.channelId);
        Log.i("asdf","notificatrionrecived======clickAction===="+remoteMessage?.notification!!.clickAction);




        when (remoteMessage.notification?.clickAction) {
               "OrderAlert" -> {
                   intent = Intent(this@FirebaseRecived, Home::class.java)
                   NOTIFICATION_for="Order Alert"
               }

            "OrderDelivered" -> {
                intent = Intent(this@FirebaseRecived, Home::class.java)
                NOTIFICATION_for="Order Delivered"
            }
            "OrderCanceledbyUser" -> {
                intent = Intent(this@FirebaseRecived, OrderCancelledByUser::class.java)
                NOTIFICATION_for="Customer Cancel your order"
            }

        }





        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        val soundUri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.best_notification)

        mBuilder = NotificationCompat.Builder(this, "10001")
        mBuilder!!.setContentTitle(NOTIFICATION_for)
        mBuilder?.setContentText(remoteMessage?.notification!!.body)
        mBuilder?.setAutoCancel(true)
        mBuilder?.setSmallIcon(R.drawable.paani_app_icon)
        mBuilder?.setBadgeIconType(R.drawable.paani_app_icon)
//        mBuilder?.setSound(Uri.parse("android.resource://com.example.letspaanidriver/" + R.raw.best_notification))
        mBuilder?.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" + applicationContext.packageName + "/"+R.raw.best_notification))

        mBuilder?.setContentIntent(pendingIntent)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        mBuilder?.setSound(alarmSound)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel("", "NOTIFICATION_CHANNEL_NAME", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED

              notificationChannel.enableVibration(true);

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(soundUri, audioAttributes)

            assert(mNotificationManager != null)
            mBuilder?.setChannelId("10001")
            mNotificationManager?.createNotificationChannel(notificationChannel)
        }

        assert(mNotificationManager != null)
        mNotificationManager?.notify(0, mBuilder?.build())
    }
}