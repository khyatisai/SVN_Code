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
import com.unfpa.appsistenciamaternaunfpa.ChatRoomActivityDoctorAndDoctor
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlin.collections.ArrayList

class CustomAdapterDoctorAndDoctorList(
    private var context: Context,
    private var nameDoctor: ArrayList<String> = ArrayList(),
    private var doctorId: ArrayList<String> = ArrayList(),
    private var totalMsg: ArrayList<String> = java.util.ArrayList()
) :
    RecyclerView.Adapter<CustomAdapterDoctorAndDoctorList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_doctor_list, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
//        nameDoctor.sort()
////        totalMsg.sort()
////        doctorId.sort()

        holder.nameDoctor.text = nameDoctor[position]
        if(totalMsg[position] != "0") {
            holder.rlNotification.visibility = View.VISIBLE
        }
        holder.edTmsg.text = totalMsg[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            val getMyUserId = AppUtils.getDataUser(context)
            val getMyUserName = AppUtils.getDataCompleteNomUser(context)
            val doctor = nameDoctor[position]
            val user = doctorId[position]

//            getToken("dajifuhidf", getMyUserId)

//          Toast.makeText(context,user+','+getMyUserId,Toast.LENGTH_SHORT).show()
            if(getMyUserId !="") {

                val intent =  Intent(context, ChatRoomActivityDoctorAndDoctor::class.java)
                val roomName1 = "$user,$getMyUserId"
                val roomName2 = "$getMyUserId,$user"
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("userName", getMyUserName)
                intent.putExtra("doctorName", doctor)
                intent.putExtra("receive", user)
                intent.putExtra("roomName1", roomName1)
                intent.putExtra("roomName2", roomName2)
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
        var edTmsg: TextView = itemView.findViewById<View>(R.id.tvmsg) as TextView
        var rlNotification: RelativeLayout = itemView.findViewById<View>(R.id.rlNotification) as RelativeLayout
    }

//    fun getToken(room:String, user:String){
//        var getChatToken =  "http://localhost:7000/api/v1/joinusers";
//        val gson: Gson = Gson()
//
//        val jsonobj = JSONObject()
//        val data = ownerArray(room,user)
//        jsonobj.put("ownerArray",data)
//        val jsonData = gson.toJson(data)
//        try {
//            val quee = Volley.newRequestQueue(context)
//            val reqq = JsonObjectRequest(
//                Request.Method.POST, getChatToken, jsonobj,
//                Response.Listener { response ->
//
//                },
//                Response.ErrorListener { error ->
//                    println(error)
//                })
//            quee.add(reqq)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
}