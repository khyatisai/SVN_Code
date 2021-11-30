package com.unfpa.appsistenciamaternaunfpa.adapters.patient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.ChatRoomActivityPatient
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import java.util.*

class CustomAdapterDoctorList(
    private var context: Context,
    private var nameDoctor: ArrayList<String> = ArrayList() ,
    private var doctorId: ArrayList<String> = ArrayList(),
    private var totalMsg: ArrayList<String> = ArrayList()

) :
    RecyclerView.Adapter<CustomAdapterDoctorList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_doctor_list, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
//        nameDoctor.sort()
//        doctorId.sort()
//        totalMsg.sort()
        holder.nameDoctor.text = nameDoctor[position]
        if(totalMsg[position] != "0") {
            holder.rlNotification.visibility = View.VISIBLE
        }
        holder.tvMsg.text = totalMsg[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            val getMyUserId = AppUtils.getDataUser(context)
            val getMyUserName = AppUtils.getDataCompleteNomUser(context)
            val doctor = nameDoctor[position]
            val user = doctorId[position]
            if(getMyUserId !="") {

                val intent =  Intent(context, ChatRoomActivityPatient::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("userName", getMyUserName)
                intent.putExtra("doctorName", doctor)
                intent.putExtra("receive", user)
                intent.putExtra("roomName", "$user,$getMyUserId")
                context.startActivity(intent)
                (context as Activity).finishAffinity()
            }else{
                //            Toast.makeText(this,"Nickname and Roomname should be filled!",Toast.LENGTH_SHORT)
            }
//            Toast.makeText(context, nombrePac[position], Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return nameDoctor.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var nameDoctor: TextView = itemView.findViewById<View>(R.id.nombrePac) as TextView
        var tvMsg:TextView = itemView.findViewById<View>(R.id.tvmsg) as TextView
        var rlNotification:RelativeLayout = itemView.findViewById<View>(R.id.rlNotification) as RelativeLayout
    }
}