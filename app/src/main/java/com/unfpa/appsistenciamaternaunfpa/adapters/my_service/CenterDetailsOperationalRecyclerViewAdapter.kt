package com.unfpa.appsistenciamaternaunfpa.adapters.my_service

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.OperationalHours
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CenterDetailsOperationalRecyclerViewAdapter( mContext: Context, private val hoursList: ArrayList<OperationalHours>) : androidx.recyclerview.widget.RecyclerView.Adapter<CenterDetailsOperationalRecyclerViewAdapter.HoursViewHolder>() {
    //this method is returning the view for each item in the list
    private var mContext: Context = mContext
    //private var context1: Context? = context1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_myservice_operational_hours_content, parent, false)
        return HoursViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        holder.bindItems(hoursList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return hoursList.size }

    class HoursViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(hours: OperationalHours) {
            val textViewDay = itemView.findViewById(R.id.txtDay) as TextView
            val textViewTiming  = itemView.findViewById(R.id.txtTiming) as TextView
            textViewDay.text = hours.day
            //val replaceStr:String = hours.timing.replace("-","   to   ")
            textViewTiming.text = hours.timing
            //textViewTiming.text = hours.timing.replace("p.m.","PM")
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val abc = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)
            if(textViewDay.text.contains(abc)){
                textViewDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                textViewTiming.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                //textViewDay.text = ContextCompat.getColor(itemView.context, R.color.colorBlue).toString()
                //textViewTiming.text = ContextCompat.getColor(itemView.context, R.color.colorBlue).toString()
            }
        }
    }
}