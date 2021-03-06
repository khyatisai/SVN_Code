package com.unfpa.appsistenciamaternaunfpa.adapters.nurse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import org.json.JSONArray
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DoctorSchedule(
    doctorAppointment: JSONArray,
) :
    RecyclerView.Adapter<DoctorSchedule.MyViewHolder>() {

    val doctorAppointment: JSONArray = doctorAppointment

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DoctorSchedule.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(p0: DoctorSchedule.MyViewHolder, p1: Int) {
        var appointment = doctorAppointment.getJSONObject(p1)
        /*val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        var dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = sdf.parse(appointment.getString("date"))
        sdf.setTimeZone(TimeZone.getTimeZone("IST"))*/
        val _24HourSDF = SimpleDateFormat("HH:mm:ss")
        val _12HourSDF = SimpleDateFormat("hh:mm a")
        val _24HourDt = _24HourSDF.parse(appointment.getString("hour"))
        p0.tvTime.text = _12HourSDF.format(_24HourDt)

        //to time calculation
        var calendar = Calendar.getInstance();
        calendar.setTime(_24HourDt);
        calendar.add(Calendar.HOUR, 1)
        var endTime = calendar.time
        var strEndTime = _12HourSDF.format(endTime)
        p0.tvEnd.text = strEndTime
    }

    override fun getItemCount(): Int {
        return doctorAppointment.length()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById<View>(R.id.tvTime) as TextView
        var tvEnd: TextView = itemView.findViewById<View>(R.id.tvEndTime) as TextView
    }
}