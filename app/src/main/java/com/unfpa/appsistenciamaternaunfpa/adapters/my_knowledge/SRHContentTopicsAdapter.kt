package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.TagedSRHListFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.item_srhcontent_topics.view.*

/**
 * Created by KhyatiShah on 8/27/2019.
 */
class SRHContentTopicsAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private lateinit var topicList: List<String>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return ContentListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_srhcontent_topics,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = topicList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val movieViewHolder = viewHolder as ContentListViewHolder
        movieViewHolder.bindView(topicList.get(position))
    }

    inner class ContentListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(topic: String) {
            itemView.txtTopic.text = topic
            itemView.llParentTag.setOnClickListener {
                //                /Toast.makeText(activity, "Clicked", Toast.LENGTH_LONG).show()
                var bundle = Bundle()
                bundle.putString(Constant.TAG_NAME, topic)
                var frag = TagedSRHListFragment()
                frag.arguments = bundle
                AppUtils.clearbackStackforFrag(Constant.FRAG_TAGED_NAME, activity)
                AppUtils.addFragment(activity, frag, true, "")
            }
        }

    }

    fun setTopicList(topicList: List<String>) {
        this.topicList = topicList
        notifyDataSetChanged()
    }
}