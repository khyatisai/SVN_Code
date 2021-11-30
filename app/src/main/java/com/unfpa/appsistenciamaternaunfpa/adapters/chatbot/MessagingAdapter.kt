package com.unfpa.appsistenciamaternaunfpa.adapters.chatbot

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant.ChatBotActivity
import com.unfpa.appsistenciamaternaunfpa.models.MessageBot
import com.unfpa.appsistenciamaternaunfpa.utils.bot.Constants.RECEIVE_ID
import com.unfpa.appsistenciamaternaunfpa.utils.bot.Constants.SEND_ID
import kotlinx.android.synthetic.main.message_bot_item.view.*


class MessagingAdapter(private var context: Context): RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<MessageBot>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {

                //Remove message on the item clicked
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_bot_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.itemView.tv_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_bot_message.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.itemView.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_message.visibility = View.GONE
            }
        }
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            //Toast.makeText(context, messagesList[position].message, Toast.LENGTH_SHORT).show()
            if(messagesList[position].message == "Horario de atención" || messagesList[position].message == "Clínica de Profamilia" ||
                messagesList[position].message == "Llamar a Profamilia"){
                if (context is ChatBotActivity) {
                    val activity = context as ChatBotActivity
                    activity.sendMessage2(messagesList[position].message)
                }
//                holder.itemView.tv_message.apply {
//                    text = messagesList[position].message
//                    visibility = View.VISIBLE
//                }
//                holder.itemView.tv_bot_message.visibility = View.GONE
            }

        }
    }

    fun insertMessage(message: MessageBot) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }

}