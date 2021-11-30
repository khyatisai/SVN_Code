package com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unfpa.appsistenciamaternaunfpa.Message
import com.unfpa.appsistenciamaternaunfpa.R

class ChatRoomAdapterNursePatient(val context: Context?, val chatList: ArrayList<Message>) :
    RecyclerView.Adapter<ChatRoomAdapterNursePatient.ViewHolder>() {

    val CHAT_MINE = 0
    val CHAT_PARTNER = 1
    val USER_JOIN = 2
    val USER_LEAVE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("chatlist size", chatList.size.toString())
        var view: View? = null
        when (viewType) {

            0 -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.row_chat_user_nurse, parent, false)
                Log.d("user inflating", "viewType : ${viewType}")
            }

            1 -> {
                view =
                    LayoutInflater.from(context).inflate(R.layout.row_chat_partner, parent, false)
                Log.d("partner inflating", "viewType : ${viewType}")
            }
            2 -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_into_notification, parent, false)
                Log.d("someone in or out", "viewType : ${viewType}")
            }
            3 -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_into_notification, parent, false)
                Log.d("someone in or out", "viewType : ${viewType}")
            }
        }

        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageData = chatList[position]
        val userName = messageData.userName;
        val content = messageData.messageContent;
        val viewType = messageData.viewType;

        when (viewType) {

            CHAT_MINE -> {
                holder.message.setText(content)
            }
            CHAT_PARTNER -> {
                holder.userName.setText(userName)
                holder.message.setText(content)
            }
            USER_JOIN -> {
                val text = "${userName} ha entrado en la sala"
                holder.text.setText(text)
            }
            USER_LEAVE -> {
                val text = "${userName} ha dejado la sala"
                holder.text.setText(text)
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.username)
        val message = itemView.findViewById<TextView>(R.id.message)
        val text = itemView.findViewById<TextView>(R.id.text)
    }
}