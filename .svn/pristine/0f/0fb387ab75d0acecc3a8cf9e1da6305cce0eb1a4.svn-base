package com.unfpa.appsistenciamaternaunfpa

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.AppointmentReminderDAO
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.util.*


class Mhserivice : Service() {
    private val mHandler: Handler? = Handler()
    private lateinit var mRunnable: Runnable
    lateinit var mSocket: Socket;
    private var message = ""
    var getUsers = EndPoints.URL_HEROKU + "/api/v1/user/getusers"
    val gson: Gson = Gson()

    lateinit var appointmentReminderDAO: AppointmentReminderDAO
    val permissionList = ArrayList<String>()
    val CONTACT_READ_PERMISSION_REQ_CODE = 100
    var enteredDt: String = ""



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        // comentarie esto para notificiones por fcm
//        startForegroundService()

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


        //comentarie esto para notificiones por fcm
        //mSocket.on("updateChat", onUpdateChat)
        //mSocket.on(Socket.EVENT_CONNECT, onConnectByReminder)
        //mSocket.on("sendNotificationAppointment", onReceiveRemienderAppointment)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    // comentarie esto para notificiones por fcm
//    override fun onBind(intent: Intent): IBinder? {
//        return null;
//    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "ON START COMMAND")

//        if (intent != null) {
//
//            when (intent.action) {
//
//                ACTION_STOP_FOREGROUND_SERVICE -> {
//                    stopService()
//                }
//
//                ACTION_OPEN_APP -> openAppHomePage(intent.getStringExtra(KEY_DATA))
//            }
//        }

        return START_NOT_STICKY;
    }

    // comentarie esto para notificiones por fcm

//    private fun openAppHomePage(value: String) {
//
//        val intent = Intent(applicationContext, TypeAccountActivity::class.java)
//        intent.putExtra(KEY_DATA, value)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//
//    }

    // comentarie esto para notificiones por fcm
//    @RequiresApi(Build.VERSION_CODES.N)
////    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            val chan = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            chan.lightColor = Color.BLUE
//            chan.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
//            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            service.createNotificationChannel(chan)
//
//        }
//    }

    // comentarie esto para notificiones por fcm
    /* Used to build and start foreground service. */
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun startForegroundService() {
//
//        //Create Notification channel for all the notifications sent from this app.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel()
//        }
//
//        // Start foreground service.
//        startFService()
//
//
////        mRunnable = Runnable {
////            if(!message.isEmpty()){
////                notifyNextEvent()
////                mHandler?.postDelayed(mRunnable, ONE_MIN_MILLI)
////            }
////              //repeat
////        };
//////
//////        // Schedule the task to repeat after 1 second
////        mHandler?.postDelayed(
////            mRunnable, // Runnable
////            ONE_MIN_MILLI // Delay in milliseconds
////        )
//
//    }

    // comentarie esto para notificiones por fcm

//    @RequiresApi(Build.VERSION_CODES.N)
//    var onReceiveRemienderAppointment =  Emitter.Listener {
//        val Message: NotificationReminder = gson.fromJson(it[0].toString(), NotificationReminder::class.java)
//        Message.viewType = MessageType.NOTIFICATION.index
//        message = Message.toString()
//        var getMyUserId = AppUtils.getDataUser(applicationContext)
//        if(!isExecute()) {
//            if (message.isNotEmpty()) {
//                notifyNextEvent(getMyUserId,Message.messageContent)
//            }
//        }
//    }

// comentarie esto para notificiones por fcm
//    @RequiresApi(Build.VERSION_CODES.N)
//    var onUpdateChat =  Emitter.Listener {
//        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
//        chat.viewType = MessageType.CHAT_PARTNER.index
//        message = chat.toString()
//        if(!isExecute()) {
//            if (message.isNotEmpty()) {
//                notifyNextEvent(chat.userName,chat.messageContent)
//            }
//        }
//    }

    // comentarie esto para notificiones por fcm

//    private fun isExecute(): Boolean {
//        val activityManager: ActivityManager =
//            this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val processInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
//        for (info in processInfos) {
//            if (BuildConfig.APPLICATION_ID.equals(info.processName, ignoreCase = true) && ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND === info.importance)
//                return true
//        }
//        return false
//    }



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

    var onConnectByReminder = Emitter.Listener {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        try {
            val data = myData(getMyUserId)
            val jsonData = gson.toJson(data)
            mSocket.emit("sendNotificationAppointment", jsonData)
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
// comentarie esto para notificiones por fcm
//    private fun startFService() {
//
//        val description = getString(R.string.msg_notification_service_desc)
//        val title = String.format(
//            getString(R.string.title_foreground_service_notification),
//            getString(R.string.app_name)
//        )
//
//        startForeground(SERVICE_ID, getStickyNotification(title, description))
//        IS_RUNNING = true
//    }

// comentarie esto para notificiones por fcm
//    private fun getStickyNotification(title: String, message: String): Notification? {
//
//        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, Intent(), 0)
//
//        // Create notification builder.
//        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//        // Make notification show big text.
//        val bigTextStyle = NotificationCompat.BigTextStyle()
//        bigTextStyle.setBigContentTitle(title)
//        bigTextStyle.bigText(message)
//        // Set big text style.
//        builder.setStyle(bigTextStyle)
//        builder.setWhen(System.currentTimeMillis())
//        builder.setSmallIcon(R.drawable.ic_app_default_icon)
//        //val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_alarm_on)
//        //builder.setLargeIcon(largeIconBitmap)
//        // Make the notification max priority.
//        builder.priority = NotificationCompat.PRIORITY_DEFAULT
//        builder.setAutoCancel(true);
//        // Make head-up notification.
//        builder.setFullScreenIntent(pendingIntent, false)
//
//
//
//        // Add Open App button in notification.
//        val openAppIntent = Intent(applicationContext, Mhserivice::class.java)
//        openAppIntent.action = ACTION_OPEN_APP
//        openAppIntent.putExtra(KEY_DATA, "" + System.currentTimeMillis())
//        val pendingPlayIntent = PendingIntent.getService(applicationContext, 0, openAppIntent, 0)
//        val openAppAction = NotificationCompat.Action(
//            android.R.drawable.ic_menu_view,
//            getString(R.string.lbl_btn_sticky_notification_open_app),
//            pendingPlayIntent
//        )
//        builder.addAction(openAppAction)
//
//
//        // Build the notification.
//        return builder.build()
//
//    }

    // comentarie esto para notificiones por fcm

//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun notifyNextEvent(user:String, msg:String) {
//        NotificationHelper.onHandleEvent(
//            getString(R.string.title_gen_notification) + " de " + user,
//            msg,
//            applicationContext
//        )
//    }
//
//    private fun stopService() {
//        // Stop foreground service and remove the notification.
//        stopForeground(true)
//        // Stop the foreground service.
//        stopSelf()
//
//        IS_RUNNING = false
//    }
//
//


    override fun onDestroy() {
//        IS_RUNNING = false
        mHandler?.removeCallbacks(null)
    }

    companion object {

        const val TAG = "FOREGROUND_SERVICE"


        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"

        const val ACTION_OPEN_APP = "ACTION_OPEN_APP"
        const val KEY_DATA = "KEY_DATA"

        private const val CHANNEL_ID: String = "1001"
        private const val CHANNEL_NAME: String = "Event Tracker"
        private const val SERVICE_ID: Int = 1
        private const val ONE_MIN_MILLI: Long = 60000  //1min

        var IS_RUNNING: Boolean = false
    }
}

