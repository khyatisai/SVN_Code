package com.unfpa.appsistenciamaternaunfpa.activities.nurse.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.*
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.ListDoctorsActivity
import com.unfpa.appsistenciamaternaunfpa.models.MessageType
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_chat_room_nurse.*
import org.json.JSONObject


class ChatRoomActivityNurseDoctor : AppCompatActivity(), View.OnClickListener {

    private var toolbar: Toolbar? = null
    val TAG = ChatRoomActivityNurseDoctor::class.java.simpleName


    lateinit var mSocket: Socket;
    lateinit var userName: String;
    lateinit var roomName: String;
    lateinit var receive: String
    var from: String = ""
    var getMessage = EndPoints.URL_HEROKU + "/api/v1/user/getmessage";

    //var readMessage = EndPoints.URL_HEROKU + "/api/v1/message/readmessagepatient";
    var readMessage = EndPoints.URL_HEROKU + "/api/v1/message/readmessagedoctor";

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<Message> = arrayListOf();
    lateinit var chatRoomAdapterPatient: ChatRoomAdapterPatient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_nurse)

        toolbar = findViewById(R.id.toolbar)
//        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar!!.title = intent.getStringExtra("doctorName")!!
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                var intent = Intent(applicationContext, ListDoctorsActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


        send.setOnClickListener(this)
//        leave.setOnClickListener(this)

        //Get the nickname and roomname from entrance activity.
        try {
            userName = intent.getStringExtra("userName")!!
            roomName = intent.getStringExtra("roomName")!!
            receive = intent.getStringExtra("receive")
            from = intent.getStringExtra("from")!!
//            Toast.makeText(this, userName.toString() + roomName.toString(), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //Set Chatroom adapter

        chatRoomAdapterPatient = ChatRoomAdapterPatient(this, chatList);
        recyclerView.adapter = chatRoomAdapterPatient;

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //Let's connect to our Chat room! :D
        try {
            mSocket = IO.socket(EndPoints.URL_HEROKU)
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket.on("newUserToChatRoom", onNewUser)
        mSocket.on("updateChat", onUpdateChat)
//        mSocket.on("userLeftChatRoom", onUserLeft)

        var getMyUserId = AppUtils.getDataUser(applicationContext)
        val jsonobj = JSONObject()

        jsonobj.put("sender", receive)
        jsonobj.put("receive", getMyUserId)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getMessage, jsonobj,
                { response ->

                    val message = response.getJSONArray("response")

                    for (i in 0 until message.length()) {
                        if (message.getJSONObject(i).getString("state") == "r") {
                            val messages = Message(
                                "",
                                message.getJSONObject(i).getString("message"),
                                roomName,
                                MessageType.CHAT_MINE.index
                            )
                            addItemToRecyclerView(messages)
                        } else if (message.getJSONObject(i).getString("state") == "s") {
                            val messages = Message(
                                "",
                                message.getJSONObject(i).getString("message"),
                                roomName,
                                MessageType.CHAT_PARTNER.index
                            )
                            addItemToRecyclerView(messages)
                        }

                    }
//

                },
                { error ->
                    println(error)
                })
            quee.add(reqq)
            readMessages(receive)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat: Message = Message(leftUserName, "", "", MessageType.USER_LEAVE.index)
        addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        chat.viewType = MessageType.CHAT_PARTNER.index
        addItemToRecyclerView(chat)
    }

    var onConnect = Emitter.Listener {
        val data = initialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)

    }

//    var onNewUser = Emitter.Listener {
//        val name = it[0] as String //This pass the userName!
//        val chat = Message(name, "", roomName, MessageType.USER_JOIN.index)
//        addItemToRecyclerView(chat)
//        Log.d(TAG, "on New User triggered.")
//    }

    private fun readMessages(receive: String) {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        val jsonobj = JSONObject()

        jsonobj.put("sender", receive)
        jsonobj.put("receive", getMyUserId)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, readMessage, jsonobj,
                { response ->
                },
                { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun sendMessage() {
        val content = editText.text.toString()
        val sendData = SendMessage(userName, content, roomName, "r")
        val jsonData = gson.toJson(sendData)
        mSocket.emit("newMessage", jsonData)

        val message = Message(userName, content, roomName, MessageType.CHAT_MINE.index)
        addItemToRecyclerView(message)
        editText.setText("")
    }

    private fun addItemToRecyclerView(message: Message) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!

        runOnUiThread {
            chatList.add(message)
            chatRoomAdapterPatient.notifyItemInserted(chatList.size)
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
//            R.id.leave -> onDestroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = initialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()

    }

    override fun onBackPressed() {
        //  super.onBackPressed()
        if (from.equals("Doctor")) {
            var intent = Intent(
                applicationContext,
                com.unfpa.appsistenciamaternaunfpa.activities.my_health.ListPacienteActivity::class.java
            )
            startActivity(intent)
        } else {
            var intent = Intent(applicationContext, ListDoctorsActivity::class.java)
            startActivity(intent)
        }
        finish()

    }

}
