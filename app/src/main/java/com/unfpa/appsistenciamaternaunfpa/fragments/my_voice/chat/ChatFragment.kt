package com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat

import android.app.*
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_voice.ChatAdapter
import com.unfpa.appsistenciamaternaunfpa.fragments.HomeFragment
import com.unfpa.appsistenciamaternaunfpa.models.ChatMessage
import com.unfpa.appsistenciamaternaunfpa.models.User
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.utils.FirestoreUtil
import com.unfpa.appsistenciamaternaunfpa.utils.notification.NotificationService
import kotlinx.android.synthetic.main.dialog_delete_reminder.*
import kotlinx.android.synthetic.main.dialog_kp_exit.*
import kotlinx.android.synthetic.main.dialog_kp_exit.txtMessage
import kotlinx.android.synthetic.main.fragment_chat.*


/**
 * Created by KhyatiShah on 12/9/2019.
 */

class ChatFragment : androidx.fragment.app.Fragment() {


    lateinit var selectedKnowledgeablePersonId: String
    lateinit var chatAdapter: ChatAdapter
    var lastActiveTime: Long = 0L
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context != null) {
            mContext = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        selectedKnowledgeablePersonId = this.arguments!!.getString("uId").toString()
        val userName = this.arguments!!.getString("userName")
        (activity as AppCompatActivity).supportActionBar?.title = userName
        //writing user to firebase
        val sharedPrefs = (activity as AppCompatActivity).getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        val countryCode = sharedPrefs.getString(Constant.ITEM_COUNTRY_CODE, "")
        val gender = sharedPrefs.getString(Constant.ITEM_GENDER, "")
        val age_group = sharedPrefs.getString(Constant.ITEM_AGE_GROUP, "")
        val area_int = sharedPrefs.getString(Constant.ITEM_INTEREST, "")
        val education = sharedPrefs.getString(Constant.ITEM_EDUCATION, "")

        val user = User(
            AppUtils.getChatUID(this@ChatFragment!!.activity!!),
            AppUtils.getDisplayName(this@ChatFragment!!.activity!!),
            countryCode,
            gender,
            age_group,
            area_int,
            education
        )
        FirestoreUtil.writeNewUser(user, selectedKnowledgeablePersonId)
        recyclerChat.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context)
        chatAdapter = ChatAdapter(this.activity!!)
        recyclerChat.adapter = chatAdapter

        startChatChannel()
        FirestoreUtil.setKPListener(kpListener)
        btnSend.setOnClickListener {
            if (!TextUtils.isEmpty(edtMsg.text)) {
                val strMessage = edtMsg.text.toString()
                val chatMessage = ChatMessage(strMessage, Constant.MESSAGE_TYPE_OUTGOING)
                lastActiveTime = AppUtils.getCurrentTimestamp()
                chatAdapter.setMessage(chatMessage)
                FirestoreUtil.sendMessage(
                    AppUtils.getChatUID(this.activity!!),
                    AppUtils.getDisplayName(this@ChatFragment!!.activity!!),
                    selectedKnowledgeablePersonId,
                    strMessage,
                    lastActiveTime
                )
                FirestoreUtil.updateMessageCount(AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId)
                edtMsg.text.clear()
                recyclerChat.smoothScrollToPosition(chatAdapter.itemCount);
                //reset timer
                resetDisconnectTimer()
                //AppUtils.hideKeyboardFrom(this.activity!!, edtMsg)
            }
        }
    }

    fun startChatChannel() {
        FirestoreUtil.createChatChannel(
            AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId,
            this.activity!!, childEventListener
        )
    }

    val kpListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val kpKey = dataSnapshot.key
            Log.d("", "onChildAdded:" + dataSnapshot.key!!)
            if (kpKey.equals(selectedKnowledgeablePersonId)) {
                showKPExitDialog()
            }
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }

    }

    fun showKPExitDialog() {
        try {
            val dialog = activity?.let { Dialog(it) }
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_kp_exit)
                dialog.txtOk.setOnClickListener {
                    dialog.dismiss()
                    FirestoreUtil.removeUser(AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId)
                    FirestoreUtil.removeChannel(AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId)
                    AppUtils.clearBackstack(this.activity!!)
                    AppUtils.addFragment(activity as AppCompatActivity, ChatPeopleFragment(), false, "")
                }
                dialog.show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d("", "onChildAdded:" + dataSnapshot.key!!)
            // A new comment has been added, add it to the displayed list
            //val comment = dataSnapshot.getValue(Comment::class.java)
            if (dataSnapshot.value !is Long) {
                val objectHashMap: Map<String, Object> = dataSnapshot.value as Map<String, Object>
                //val objectArrayList = ArrayList<Any>(objectHashMap.values)
                //val messageJsonObject = JSONObject(dataSnapshot.value.toString())
                var userId = ""
                if (objectHashMap.containsKey("user")) {
                    userId = objectHashMap.get("user").toString()
                }
                if (!TextUtils.isEmpty(userId) && !userId.equals(AppUtils.getChatUID(mContext))) {
                    val strMsg = objectHashMap.get("message").toString()
                    val chatMessage = ChatMessage(strMsg, Constant.MESSAGE_TYPE_INCOMING)
                    chatAdapter.setMessage(chatMessage)
                    recyclerChat.smoothScrollToPosition(chatAdapter.itemCount);
                    //reset timer
                    resetDisconnectTimer()
                }
            }
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d("", "onChildChanged: ${dataSnapshot.key}")
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val commentKey = dataSnapshot.key
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val commentKey = dataSnapshot.key
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(
                context, "Failed to load comments.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    fun onBeackPressed() {

        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_delete_reminder)
            dialog.txtMessage.text = activity!!.getString(R.string.chatDataDeleted)
            dialog.txtNo.setOnClickListener {
                dialog.dismiss()
            }

            dialog.txtYes.setOnClickListener {
                dialog.dismiss()
                exitUser()
            }
            dialog.show()
        }


    }

    private fun exitUser() {
        FirestoreUtil.removeUser(AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId)
        FirestoreUtil.removeChannel(AppUtils.getChatUID(this.activity!!), selectedKnowledgeablePersonId)
        // AppUtils.clearBackstack(this.activity!!)
        AppUtils.addFragment(this.activity!!, HomeFragment(), false, "")
    }

    private val disconnectHandler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            // todo
            return true
        }
    })
    private val disconnectCallback = object : Runnable {
        override fun run() {
            //Toast.makeText(this@ChatFragment.activity, "Timeout", Toast.LENGTH_LONG).show()
            showNotification()
            disconnectHandler.removeCallbacks(this)
            disconnectHandler.postDelayed(exitCallback, Constant.DISCONNECT_TIMEOUT)
            //exitUser()
        }
    }

    private val exitCallback = object : Runnable {
        override fun run() {
            //Toast.makeText(this@ChatFragment.activity, "Timeout", Toast.LENGTH_LONG).show()
            disconnectHandler.removeCallbacks(this)
            exitUser()
        }
    }

    fun resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback)
        disconnectHandler.postDelayed(disconnectCallback, Constant.NOTIFICATION_TIMEOUT)
    }

    fun stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback)
    }

    fun onUserInteraction() {
        resetDisconnectTimer()
    }

    override fun onResume() {
        super.onResume()
        resetDisconnectTimer()
    }

    override fun onStop() {
        super.onStop()
        //stopDisconnectTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        // exitUser()
        stopDisconnectTimer()
        disconnectHandler.removeCallbacks(exitCallback)
    }

    fun showNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val context = this.activity!!.applicationContext
            var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = context.getColor(R.color.white)
            notificationChannel.description = getString(R.string.action_notification)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)

            val mNotification = Notification.Builder(this.activity, NotificationService.CHANNEL_ID)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(this.activity!!.getString(R.string.ChatWillbeDeletedSoon))
                .setStyle(
                    Notification.BigTextStyle()
                    //.bigText("Test")
                ).build()
            //.setContentText("Test").build()

            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(1000, mNotification)
        } else {
            val mNotification = Notification.Builder(this.activity)
                // Set the intent that will fire when the user taps the notification
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(this.activity!!.getString(R.string.ChatWillbeDeletedSoon))
                .setStyle(
                    Notification.BigTextStyle()
                ).build()
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(1000, mNotification)
        }
    }

}