package com.unfpa.appsistenciamaternaunfpa.adapters.my_service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import kotlinx.android.synthetic.main.item_srhcontent_topics.view.*

/**
 * Created by KhyatiShah on 8/27/2019.
 */
class MyServiceTagsAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private lateinit var topicList: List<String>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return ContentListViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.item_myservice_service_tags,
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

        }

    }

    fun setTopicList(topicList: List<String>) {
        this.topicList = topicList
        notifyDataSetChanged()
    }
}