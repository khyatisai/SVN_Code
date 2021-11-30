package com.unfpa.appsistenciamaternaunfpa

//import com.unfpa.appsistenciamaternaunfpa.activities.IncomingCall

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unfpa.appsistenciamaternaunfpa.activities.SplashActivity
import com.unfpa.appsistenciamaternaunfpa.activities.TypeAccountActivity
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant.CHANNEL_ID


class callService : Service() {

    lateinit var mp: MediaPlayer;
    var f = Firebase.database.getReference("users")

    val NOTIFICATION_CHANNEL_ID_SERVICE = "com.unfpa.appsistenciamaternaunfpa.notification.CHANNEL_ID"

    override fun onCreate() {
        super.onCreate()
        initChannel();
        //startService(Intent(this, DummyService::class.java))
        mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mp.isLooping = true

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        val getMyUserId = AppUtils.getDataUser(this)
        f.child(getMyUserId).child("incoming").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
//                    val intent = Intent(this@callService, IncomingCall::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
                        if (!mp.isPlaying) {
                            mp = MediaPlayer.create(
                                applicationContext,
                                Settings.System.DEFAULT_RINGTONE_URI
                            )
                            mp.isLooping = true
                            mp.start()
                        }else{
                            mp.start()
                        }

                    } else if (snapshot.value === null) {
                        if (mp.isPlaying) {
                            mp.stop()
                        }
                    }
                } catch (e: Exception) {
                    println(e.message.toString())
                }
            }
        })

        val notificationIntent = Intent(this, SplashActivity::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =  NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE)
            .setContentTitle("Teleconsulta Profamilia")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_cd_phone)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(45, notification);

        return START_STICKY
    }

    fun initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_SERVICE,
                    "App Service",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
           if(mp != null){
               mp.release();
           }
    }
}