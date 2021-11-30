package com.unfpa.appsistenciamaternaunfpa.utils.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.app.NotificationChannel
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainActivity
import com.unfpa.appsistenciamaternaunfpa.utils.Constant


/**
 * Created by KhyatiShah on 12/17/2019.
 */
class NotificationService : IntentService("NotificationService") {
    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000

    @SuppressLint("NewApi")
    private fun createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = context.getColor(R.color.colorBlue)
            notificationChannel.description = getString(R.string.action_notification)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, Notification())
    }

    companion object {

        const val CHANNEL_ID = "com.unfpa.appsistenciamaternaunfpa.notification.CHANNEL_ID"
        const val CHANNEL_NAME = "mHealth Notification"
    }


    override fun onHandleIntent(intent: Intent?) {

        //Create Channel
        createChannel()

        //var timestamp: Long = 0
        if (intent != null && intent.extras != null) {
            val title = intent.extras!!.getString(Constant.NOTIFICATION_TITLE)
            val message = intent.extras!!.getString(Constant.NOTIFICATION_SUB_TITLE)
            val notificationType = intent.extras!!.getString(Constant.NOTIFICATION_TYPE)
            //if (timestamp > 0) {


            val context = this.applicationContext
            var notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifyIntent = Intent(this, MainActivity::class.java)
            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("message", message)
            notifyIntent.putExtra("notification", true)
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationType.equals(Constant.NOTIFICATION_TYPE_APPOINTMENT)) {
                    mNotification = Notification.Builder(this, CHANNEL_ID)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_noti_appointment)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(message)
                        )
                        .setContentText(message).build()
                } else {
                    mNotification = Notification.Builder(this, CHANNEL_ID)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_noti_medication)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(message)
                        )
                        .setContentText(message).build()
                }
            } else {
                if (notificationType.equals(Constant.NOTIFICATION_TYPE_APPOINTMENT)) {
                    mNotification = Notification.Builder(this)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_noti_appointment)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(title)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(message)
                        )
                        .setSound(uri)
                        .setContentText(message).build()
                } else {
                    mNotification = Notification.Builder(this)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_noti_medication)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(title)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(message)
                        )
                        .setSound(uri)
                        .setContentText(message).build()
                }
            }



            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)
        }
    }


    //}
}