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
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.ChatRoomAdapterNurseAndDoctor
import com.unfpa.appsistenciamaternaunfpa.models.MessageType
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_chatroom.recyclerView
import kotlinx.android.synthetic.main.activity_chatroom.send
import kotlinx.android.synthetic.main.activity_chatroom_doctor_and_doctor.*
import org.json.JSONObject


class ChatRoomActivityDoctorAndDoctor : AppCompatActivity(), View.OnClickListener {

    private var toolbar: Toolbar? = null
    val TAG = ChatRoomActivityDoctorAndDoctor::class.java.simpleName


    lateinit var mSocket: Socket;
    lateinit var userName: String;
    lateinit var roomName1: String;
    lateinit var roomName2: String;

    lateinit var receive: String

    var getMessage = EndPoints.URL_HEROKU + "/api/v1/user/getmessage";

    var getUsers = EndPoints.URL_HEROKU + "/api/v1/user/getusers"

    var readMessage = EndPoints.URL_HEROKU + "/api/v1/message/readmessagenurse";

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<Message> = arrayListOf();
    lateinit var chatRoomAdapterDoctorAndDoctor: ChatRoomAdapterNurseAndDoctor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_room_activity_nurse_doc)

        toolbar = findViewById(R.id.toolbar)
//        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar!!.title = intent.getStringExtra("doctorName")!!
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                var intent = Intent(applicationContext, ListPacienteActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


        send.setOnClickListener(this)
//        leave.setOnClickListener(this)

        //Get the nickname and roomname from entrance activity.
        try {
            userName = intent.getStringExtra("userName")!!
            roomName1 = intent.getStringExtra("roomName1")!!
            roomName2 = intent.getStringExtra("roomName2")!!
            receive = intent.getStringExtra("receive")
//            Toast.makeText(this, userName.toString() + roomName.toString(), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //Set Chatroom adapter

        chatRoomAdapterDoctorAndDoctor = ChatRoomAdapterNurseAndDoctor(this, chatList);
        recyclerView.adapter = chatRoomAdapterDoctorAndDoctor;

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
        getMessagePartner()
        readMessages(receive)
//        getMessageMine()
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

        val data = initialData(userName, roomName1)
        val data2 = initialData(userName, roomName2)
        val jsonData1 = gson.toJson(data)
        val jsonData2 = gson.toJson(data2)
        mSocket.emit("subscribe", jsonData1)
        mSocket.emit("subscribe", jsonData2)

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
                Response.Listener { response ->
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun readMessages2(receive: String) {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        val jsonobj = JSONObject()

        jsonobj.put("sender", receive)
        jsonobj.put("receive", getMyUserId)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, readMessage, jsonobj,
                Response.Listener { response ->
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun sendMessage() {
        val content = edtChatDoc.text.toString()
        val getMyUserId = AppUtils.getDataUser(applicationContext)
        val activeUser = roomName1.split(",").toTypedArray()
        for (i in activeUser.indices) {
            if (getMyUserId == activeUser[i]) {
                val sendData2 = SendMessage(userName, content, roomName2, "s")
                // val sendData2 = SendMessage(userName, content, roomName2, "r")
                val jsonData2 = gson.toJson(sendData2)
                mSocket.emit("newMessage", jsonData2)
                val message2 = Message(userName, content, roomName2, MessageType.CHAT_MINE.index)
                addItemToRecyclerView(message2)
            }
        }

//        for(i in activeUser.indices){
//            if(getMyUserId == activeUser[i]){
//                val sendData1 = SendMessage(userName, content, roomName1, "r")
//                val jsonData1 = gson.toJson(sendData1)
//                mSocket.emit("newMessage", jsonData1)
//                val message1 = Message(userName, content, roomName1, MessageType.CHAT_MINE.index)
//                addItemToRecyclerView(message1)
//            }
//        }


        edtChatDoc.setText("")
    }

    private fun addItemToRecyclerView(message: Message) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!

        runOnUiThread {
            chatList.add(message)
            chatRoomAdapterDoctorAndDoctor.notifyItemInserted(chatList.size)
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }

    private fun getMessagePartner() {
        var getMyUserId = AppUtils.getDataUser(applicationContext)
        val jsonobj = JSONObject()

        jsonobj.put("sender", receive)
        jsonobj.put("receive", getMyUserId)
        try {
            val quee = Volley.newRequestQueue(applicationContext)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getMessage, jsonobj,
                Response.Listener { response ->

//                    val message = response.getJSONArray("response")
//
//                    for (i in 0 until message.length()) {
//                        val sender = message.getJSONObject(i).getJSONObject("sender")
//                        if(sender.getString("id") != getMyUserId){
//                            val messages = Message("", message.getJSONObject(i).getString("message"), roomName2, MessageType.CHAT_PARTNER.index)
//                            addItemToRecyclerView(messages)
//                        }
//                        if(sender.getString("id") == getMyUserId){
//                            val messages = Message("", message.getJSONObject(i).getString("message"), roomName2, MessageType.CHAT_MINE.index)
//                            addItemToRecyclerView(messages)
//                        }
//                    }

                    val message = response.getJSONArray("response")

                    var messages = Message("", "", "", 0)
                    for (i in 0 until message.length()) {
                        val sender = message.getJSONObject(i).getJSONObject("sender")
                        val receive = message.getJSONObject(i).getJSONObject("receiver")

                        if (sender.getString("id") == getMyUserId) {
                            val messages = Message(
                                sender.getString("firstname") + sender.getString("lastname"),
                                message.getJSONObject(i).getString("message"),
                                roomName2,
                                MessageType.CHAT_MINE.index
                            )
                            addItemToRecyclerView(messages)
                        }
                        if (sender.getString("id") != getMyUserId) {
                            val messages = Message(
                                sender.getString("firstname") + sender.getString("lastname"),
                                message.getJSONObject(i).getString("message"),
                                roomName2,
                                MessageType.CHAT_PARTNER.index
                            )
                            addItemToRecyclerView(messages)
                        }
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
//            R.id.leave -> onDestroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data1 = initialData(userName, roomName1)
        val data2 = initialData(userName, roomName2)
        val jsonData1 = gson.toJson(data1)
        val jsonData2 = gson.toJson(data2)
        mSocket.emit("unsubscribe", jsonData1)
        mSocket.emit("unsubscribe", jsonData2)
        mSocket.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        var intent = Intent(applicationContext, ListPacienteActivity::class.java)
        startActivity(intent)
        finish()
    }

}
