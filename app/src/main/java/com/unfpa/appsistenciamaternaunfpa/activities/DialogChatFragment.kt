package com.unfpa.appsistenciamaternaunfpa.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.*
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.ChatRoomAdapterNursePatient
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.ChatRoomAdapterPatientNurse
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints.URL_HEROKU
import com.unfpa.appsistenciamaternaunfpa.models.MessageType
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chatroom.recyclerView
import kotlinx.android.synthetic.main.fragment_dialog_chat.*
import org.json.JSONObject
import java.lang.reflect.Field


class DialogChatFragment : BottomSheetDialogFragment() {

    lateinit var mSocket: Socket
    lateinit var userName: String
    lateinit var roomName: String
    lateinit var receive: String
    protected var mBehavior: BottomSheetBehavior<FrameLayout>? = null
    var getMessage = EndPoints.URL_HEROKU + "/api/v1/user/getmessage";
    var getUserDetail = EndPoints.URL_HEROKU + "/api/v1/user/getuserdetail";
    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<Message> = arrayListOf();
    val data: ArrayList<String> = arrayListOf();
    lateinit var chatRoomAdapter: ChatRoomAdapter

    //lateinit var chatRoomAdapterPatient: ChatRoomAdapterPatient
    lateinit var chatRoomAdapterPatient: ChatRoomAdapterPatientNurse
    lateinit var chatRoomAdapterNursePatient: ChatRoomAdapterNursePatient
    var typeUser = ""
    var typeReceiver = "enfermera"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dialog_chat, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED
        try {

            typeUser = context?.let { AppUtils.getTypeUser(it) }.toString()
            val mArgs = arguments

            userName = mArgs!!.getString("userName")!!
            roomName = mArgs.getString("roomName")!!
            receive = mArgs.getString("receive")!!


        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (typeUser == "medico") {
            chatRoomAdapter = ChatRoomAdapter(context, chatList);
            recyclerView.adapter = chatRoomAdapter;
        } else if (typeUser.equals("enfermera")) {
            chatRoomAdapterNursePatient = ChatRoomAdapterNursePatient(context, chatList);
            recyclerView.adapter = chatRoomAdapterNursePatient;
        } else {
            if (typeUser.equals("paciente")) {
                //val receiverType = getUserDetails(receive)
                val jsonobj = JSONObject()
                jsonobj.put("userid", receive)

                val quee = Volley.newRequestQueue(context)
                val reqq = JsonObjectRequest(
                    Request.Method.POST, getUserDetail, jsonobj,
                    Response.Listener { response ->
                        val resJson = JSONObject(response.toString())
                        if (resJson.has("message") && resJson.getString("message").equals("ok")) {
                            typeReceiver = resJson.getJSONObject("response").getString("typeUser")
                            chatRoomAdapterPatient =
                                ChatRoomAdapterPatientNurse(context, chatList, typeReceiver);
                            recyclerView.adapter = chatRoomAdapterPatient;
                        } else {
                            chatRoomAdapterPatient =
                                ChatRoomAdapterPatientNurse(context, chatList, typeReceiver);
                            recyclerView.adapter = chatRoomAdapterPatient;
                        }
                    },
                    Response.ErrorListener { error ->
                        println(error)
                        chatRoomAdapterPatient =
                            ChatRoomAdapterPatientNurse(context, chatList, typeReceiver);
                        recyclerView.adapter = chatRoomAdapterPatient;
                    })
                quee.add(reqq)
            }
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        //Let's connect to our Chat room! :D
        try {
            mSocket = IO.socket(URL_HEROKU)
            // Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket.on("newUserToChatRoom", onNewUser)
        mSocket.on("updateChat", onUpdateChat)
//        mSocket.on("userLeftChatRoom", onUserLeft)

        val getMyUserId = context?.let { AppUtils.getDataUser(it) }
        val jsonobj = JSONObject()
        if (typeUser == "medico") {
            jsonobj.put("sender", getMyUserId)
            jsonobj.put("receive", receive)
        } else {
            jsonobj.put("sender", receive)
            jsonobj.put("receive", getMyUserId)
        }
        try {
            val quee = Volley.newRequestQueue(context)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getMessage, jsonobj,
                Response.Listener { response ->

                    val message = response.getJSONArray("response")
                    //if(typeUser == "medico"){
                    if (typeUser.equals("medico") || typeUser.equals("enfermera")) {
                        for (i in 0 until message.length()) {
                            if (message.getJSONObject(i).getString("state") == "r") {
                                val messages = Message(
                                    "",
                                    message.getJSONObject(i).getString("message"),
                                    roomName,
                                    MessageType.CHAT_PARTNER.index
                                )
                                addItemToRecyclerView(messages)
                            } else if (message.getJSONObject(i).getString("state") == "s") {
                                val messages = Message(
                                    "",
                                    message.getJSONObject(i).getString("message"),
                                    roomName,
                                    MessageType.CHAT_MINE.index
                                )
                                addItemToRecyclerView(messages)
                            }

                        }
                    } else {
                        for (i in 0 until message.length()) {
                            if (message.getJSONObject(i).getString("state") == "s") {
                                val messages = Message(
                                    "",
                                    message.getJSONObject(i).getString("message"),
                                    roomName,
                                    MessageType.CHAT_PARTNER.index
                                )
                                addItemToRecyclerView(messages)
                            } else if (message.getJSONObject(i).getString("state") == "r") {
                                val messages = Message(
                                    "",
                                    message.getJSONObject(i).getString("message"),
                                    roomName,
                                    MessageType.CHAT_MINE.index
                                )
                                addItemToRecyclerView(messages)
                            }

                        }
                    }
//

                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        sendChatDialog.setOnClickListener {
            val content = editTextChatDialog.text.toString()
            val state: String =
                if (typeUser.equals("medico") || typeUser.equals("enfermera")) "s" else "r"
            val sendData = SendMessage(userName, content, roomName, state)

            val jsonData = gson.toJson(sendData)
            mSocket.emit("newMessage", jsonData)

            val message = Message(userName, content, roomName, MessageType.CHAT_MINE.index)
            addItemToRecyclerView(message)
            editTextChatDialog.setText("")
        }

        btn_exit.setOnClickListener {
            this.dismiss()
            val data = initialData(userName, roomName)
            val jsonData = gson.toJson(data)
            mSocket.emit("unsubscribe", jsonData)
            mSocket.disconnect()
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

    private fun addItemToRecyclerView(message: Message) {

        //por estar dentro de in listener hay que meterlo en un UiThread
        activity?.runOnUiThread {
            chatList.add(message)
            if (typeUser == "medico") {
                chatRoomAdapter.notifyItemInserted(chatList.size)
                recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
            } else if (typeUser == "enfermera") {
                chatRoomAdapterNursePatient.notifyItemInserted(chatList.size)
                recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
            } else {
                chatRoomAdapterPatient.notifyItemInserted(chatList.size)
                recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
            }

        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (typeUser.equals("medico")) {
            roomName = ""
            userName = ""
            receive = ""
            chatList.clear()
            chatRoomAdapter.notifyDataSetChanged()
        } else if (typeUser == "enfermera") {
            roomName = ""
            userName = ""
            receive = ""
            chatList.clear()
            chatRoomAdapterNursePatient.notifyDataSetChanged()
        } else {
            roomName = ""
            userName = ""
            receive = ""
            chatList.clear()
            chatRoomAdapterPatient.notifyDataSetChanged()
        }

    }

    private fun getUserDetails(receive: String): String {

        return ""
    }

}