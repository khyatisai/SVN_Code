package com.unfpa.appsistenciamaternaunfpa.adapters.patient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import kotlin.collections.ArrayList

class NotificationAdapter(
    private var context: Context,

    private var title: ArrayList<String> = ArrayList(),
    private var text: ArrayList<String> = ArrayList(),
    private var hour: ArrayList<String> = ArrayList()
) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification2, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.title.text = title[position]
        holder.text.text = text[position]
        holder.hour.text = hour[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, title[position], Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return title.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.txtHeader) as TextView
        var text: TextView = itemView.findViewById<View>(R.id.txtShortDesc1) as TextView
        var hour: TextView = itemView.findViewById<View>(R.id.txtTime) as TextView
    }
}