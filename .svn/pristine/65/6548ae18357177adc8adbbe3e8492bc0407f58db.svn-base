package com.unfpa.appsistenciamaternaunfpa.adapters.my_service

import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.MyServiceItemInfoFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_myservicelistitem_2.view.*

class MyServiceRecyclerViewAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var myServiceList: List<MyServiceEntity>
    private lateinit var contentList: List<MyServiceEntity>
    private var activity: androidx.fragment.app.FragmentActivity = activity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        return MyServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_myservicelistitem_2, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val myServiceListViewHolder = viewHolder as MyServiceViewHolder
        myServiceListViewHolder.bindView(myServiceList[position])
    }

    override fun getItemCount(): Int = myServiceList.size

    inner class MyServiceViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(myServiceList: MyServiceEntity) {
            itemView.txtMyServiceHead.text = HtmlCompat.fromHtml(myServiceList.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            val shortDesc = HtmlCompat.fromHtml(myServiceList.body, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            itemView.txtMyServiceShortDesc.text = shortDesc.replace("\n","")
            itemView.cardView.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("myServiceId", myServiceList.nid)
                bundle.putString("myServiceName", myServiceList.title)
                bundle.putString("myServiceDesc", myServiceList.body)
                var frag = MyServiceItemInfoFragment()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }
    }
    fun setMyServiceList(myServiceList: List<MyServiceEntity>) {
        this.myServiceList = myServiceList
        notifyDataSetChanged()
    }
    fun setContentList(contentList: List<MyServiceEntity>) {
        this.myServiceList = contentList
        notifyDataSetChanged()
    }
}
