package com.unfpa.appsistenciamaternaunfpa.adapters.my_voice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.models.ChatMessage
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.item_chat_messages.view.*

/**
 * Created by KhyatiShah on 12/10/2019.
 */
class ChatAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val chatMessages = ArrayList<ChatMessage>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return ContentListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_messages,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = chatMessages.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val contentViewHolder = viewHolder as ContentListViewHolder
        contentViewHolder.bindView(chatMessages.get(position))
    }

    inner class ContentListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(chatMessage: ChatMessage) {
            if (chatMessage.messageType.equals(Constant.MESSAGE_TYPE_INCOMING)) {
                itemView.llOutgoingMsg.visibility = View.GONE
                itemView.llIncommmingMsg.visibility = View.VISIBLE
                itemView.txtIncommingMsg.text =
                    HtmlCompat.fromHtml(chatMessage.message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            } else {
                itemView.llIncommmingMsg.visibility = View.GONE
                itemView.llOutgoingMsg.visibility = View.VISIBLE
                itemView.txtOutgoingMsg.text =
                    HtmlCompat.fromHtml(chatMessage.message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            }
        }

    }

    fun setContentList(chatMessages: ArrayList<ChatMessage>) {
        this.chatMessages.addAll(chatMessages)
        notifyDataSetChanged()
    }

    fun setMessage(chatMessage: ChatMessage) {
        this.chatMessages.add(chatMessage)
        notifyDataSetChanged()
    }
}