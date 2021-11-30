package com.unfpa.appsistenciamaternaunfpa.adapters.my_voice

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.MyVoiceListDetails
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.MyVoiceShareStoryFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_voice_list_content.view.*

class MyVoiceRecyclerViewAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var myVoiceList: List<MyVoiceEntity>
    private var activity: androidx.fragment.app.FragmentActivity = activity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        return MyVoiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_voice_list_content, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val myVoiceListViewHolder = viewHolder as MyVoiceViewHolder
        myVoiceListViewHolder.bindView(myVoiceList[position])
    }

    override fun getItemCount(): Int = myVoiceList.size

    inner class MyVoiceViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(myVoiceList: MyVoiceEntity) {
            itemView.txtVoiceStoryHeader.text = HtmlCompat.fromHtml(myVoiceList.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            itemView.txtVoiceStoryDesc.text = HtmlCompat.fromHtml(myVoiceList.story, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            if(myVoiceList.story_mode == "edit"){
                itemView.imgTick.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_myvoice_edit))
            }else{
                itemView.imgTick.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_myvoice_tick))
            }
            itemView.cardViewCenter.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("unique_id", myVoiceList.unique_id)
                bundle.putString("title", myVoiceList.title)
                bundle.putString("story", myVoiceList.story)
                bundle.putString("story_mode", myVoiceList.story_mode)
                if(myVoiceList.story_mode == "edit"){
                    var frag = MyVoiceShareStoryFragment()
                    frag.arguments = bundle
                    AppUtils.addFragment(activity, frag, true, "")
                }else{
                    var frag = MyVoiceListDetails()
                    frag.arguments = bundle
                    AppUtils.addFragment(activity, frag, true, "")
                }

            }
        }
    }
    fun setMyVoiceList(myVoiceList: List<MyVoiceEntity>) {
        this.myVoiceList = myVoiceList
        notifyDataSetChanged()
    }
}
