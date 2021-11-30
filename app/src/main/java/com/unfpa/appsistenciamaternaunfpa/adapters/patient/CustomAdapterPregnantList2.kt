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
import com.unfpa.appsistenciamaternaunfpa.ChatRoomActivity
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import java.util.*

class CustomAdapterPregnantList2(
    private var context: Context,

    private var nombrePac: ArrayList<String> = ArrayList(),
    private var userId: ArrayList<String> = ArrayList(),
    private var totalMsg: ArrayList<String> = ArrayList()

) :
    RecyclerView.Adapter<CustomAdapterPregnantList2.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_doctorlistpaciente2, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
//        nombrePac.sort()
//        totalMsg.sort()
//        userId.sort()

        holder.nombrePregnant.text = nombrePac[position]
        if(totalMsg[position] != "0") {
            holder.rlNotification.visibility = View.VISIBLE
        }
        holder.edTmsg.text = totalMsg[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            val getMyUserId = AppUtils.getDataUser(context)
            val getMyUserName = AppUtils.getDataCompleteNomUser(context)
            val patient = nombrePac[position]
            val user = userId[position]

            if(getMyUserId !="") {

                var intent =  Intent(context, ChatRoomActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userName", getMyUserName)
                intent.putExtra("patientName", patient)
                intent.putExtra("receive", user)
                intent.putExtra("roomName", "$getMyUserId,$user")
                context.startActivity(intent)
                (context as Activity).finishAffinity()
            }else{
                //            Toast.makeText(this,"Nickname and Roomname should be filled!",Toast.LENGTH_SHORT)
            }
//            Toast.makeText(context, nombrePac[position], Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return nombrePac.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var nombrePregnant: TextView = itemView.findViewById<View>(R.id.nombrePac) as TextView
        var edTmsg: TextView = itemView.findViewById<View>(R.id.edtmsg) as TextView
        var rlNotification: RelativeLayout = itemView.findViewById<View>(R.id.rlNotification) as RelativeLayout
    }
}