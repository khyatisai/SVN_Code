package com.unfpa.appsistenciamaternaunfpa.adapters.my_voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat.ChatFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat.ChatPeopleFragment
import com.unfpa.appsistenciamaternaunfpa.models.KnowledgeablePerson
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_chat_knowledgeable_person_list.view.*

/**
 * Created by KhyatiShah on 12/6/2019.
 */
class KnowledgeablePersonAdapter(fragment: ChatPeopleFragment) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val knowledgeablePersonList = ArrayList<KnowledgeablePerson>()
    private var fragment: ChatPeopleFragment = fragment

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return ContentListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_knowledgeable_person_list,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = knowledgeablePersonList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val contentViewHolder = viewHolder as ContentListViewHolder
        contentViewHolder.bindView(knowledgeablePersonList.get(position))
    }

    inner class ContentListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(knowledgeablePerson: KnowledgeablePerson) {
            itemView.txtHeader.text = knowledgeablePerson.name
            itemView.txtShortDesc.text = knowledgeablePerson.countryCode
            itemView.cardSRHContent.setOnClickListener {
                //fragment.create_chat_channel(knowledgeablePerson.uId!!)
                var bundle = Bundle()
                bundle.putString("uId", knowledgeablePerson.uId)
                bundle.putString("userName", knowledgeablePerson.name)
                var frag = ChatFragment()
                frag.arguments = bundle
                AppUtils.addFragment(fragment.activity!!, frag, false, "")
            }
        }

    }

    fun setContentList(knowledgeablePersonList: ArrayList<KnowledgeablePerson>) {
        this.knowledgeablePersonList.clear()
        this.knowledgeablePersonList.addAll(knowledgeablePersonList)
        notifyDataSetChanged()
    }
}