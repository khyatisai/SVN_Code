package com.unfpa.appsistenciamaternaunfpa.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.TypeAccountActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.dao.notification.NotificationDAO2
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification2
import com.unfpa.appsistenciamaternaunfpa.initialData
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class FCMService : FirebaseMessagingService() {
    private lateinit var notificationDAO: NotificationDAO2
    lateinit var mSocket: Socket;
    private var message = ""
    var getUsers = EndPoints.URL_HEROKU + "/api/v1/user/getusers"
    val gson: Gson = Gson()

    override fun onCreate() {
        super.onCreate()
        try {
            mSocket = IO.socket(EndPoints.URL_HEROKU)
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect1)
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
    }
    var onConnect1 = Emitter.Listener {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getUsers, null,
                Response.Listener { response ->

                    var users = response.getJSONArray("users")

                    for (i in 0 until users.length()) {
                        var userid = users.getJSONObject(i).getString("id")
                        val data = initialData(getMyUserId, getMyUserId+","+userid)
                        val jsonData = gson.toJson(data)
                        mSocket.emit("subscribe", jsonData)
                    }
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }
    var onConnect = Emitter.Listener {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getUsers, null,
                Response.Listener { response ->

                    var users = response.getJSONArray("users")

                    for (i in 0 until users.length()) {
                        var userid = users.getJSONObject(i).getString("id")
                        val data = initialData(getMyUserId, userid+","+getMyUserId)
                        val jsonData = gson.toJson(data)
                        mSocket.emit("subscribe", jsonData)
                    }
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Log.d(TAG,newToken)
        val getMyUserId = AppUtils.getDataUser(applicationContext)
        if(getMyUserId.isNotEmpty()){
            val UPDATETOKEN = EndPoints.URL_HEROKU + "/api/v1/auth/fcm/token"

            val jsonobj = JSONObject()
            jsonobj.put("userId", getMyUserId)
            jsonobj.put("token", newToken)


            val que = Volley.newRequestQueue(this)
            val req = JsonObjectRequest(
                Request.Method.POST, UPDATETOKEN, jsonobj,
                Response.Listener { response ->
                    try {
                        Log.d("SUCCESS", "update token success")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR UPDATE TOKEN", "error update token")
                })
            que.add(req)
        }
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
       // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            handleNow()
        }

        // Check if message contains a notification payload.
        remoteMessage.notification.let {
            addNotificationForAppointment( "Recordatorio",remoteMessage.notification?.body.toString())
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.body)
        }


    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, TypeAccountActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_schedule)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager!!.createNotificationChannel(channel)
        }
        notificationManager!!.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    fun addNotificationForAppointment(title:String, data:String) {
        notificationDAO = MhealthRoomDatabase.getAppDataBase(applicationContext)!!.notificationDAO2()
        val addNotification = obtenerFechaConFormato("HH:mm:ss","America/Managua")?.let {
            Notification2(
                data,
                title,
                false,
                it
            )
        }
        if (addNotification != null) {
            notificationDAO.insertNotification(addNotification)
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun obtenerFechaConFormato(
        formato: String?,
        zonaHoraria: String?
    ): String? {
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = calendar.time
        val sdf: SimpleDateFormat = SimpleDateFormat(formato)
        sdf.timeZone = TimeZone.getTimeZone(zonaHoraria)
        return sdf.format(date)
    }

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID: String = "1001"
    }
}