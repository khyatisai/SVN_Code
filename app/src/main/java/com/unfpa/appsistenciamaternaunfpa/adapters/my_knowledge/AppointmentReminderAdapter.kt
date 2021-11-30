package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.item_appointment_reminder.view.*
import java.text.SimpleDateFormat

/**
 * Created by KhyatiShah on 10/25/2019.
 */
class AppointmentReminderAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var appointmentReminderList: List<AppointmentReminder>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return AppointReminderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_appointment_reminder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appointmentReminderList.size
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(itemView: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val appointmentReminderViewHolder = itemView as AppointReminderViewHolder

        //setting layout visibility

        appointmentReminderViewHolder.txtAppointmentName.text =
            appointmentReminderList.get(position).appointmentName

        val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
        val targetFormat = SimpleDateFormat(Constant.APPOINTMENT_DISPLAY_DATE_FORMAT)
        val date = inputDateFormat.parse(appointmentReminderList.get(position).date)
        val displayDate = targetFormat.format(date)
        val strTimeDate =
            displayDate + " " + AppUtils.getTime12HourFormat(appointmentReminderList.get(position).time)
        appointmentReminderViewHolder.txtDateTime.text = strTimeDate
        appointmentReminderViewHolder.txtCenterName.text = appointmentReminderList.get(position).serviceCenterName

        appointmentReminderViewHolder.cardAddAppointment.visibility = View.GONE
        appointmentReminderViewHolder.cardContainer.visibility = View.VISIBLE
        appointmentReminderViewHolder.imgDelete.visibility = View.GONE

    }

    class AppointReminderViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val txtAppointmentName = itemView.txtAppointmentName
        val imgDelete = itemView.imgDelete
        val txtDateTime = itemView.txtDateTime
        val txtCenterName = itemView.txtCenterName
        val cardAddAppointment = itemView.cardAddAppointment
        val cardContainer = itemView.cardContainer
    }


    fun setAppointmnetList(appointmentReminderList: List<AppointmentReminder>) {
        this.appointmentReminderList = appointmentReminderList
        notifyDataSetChanged()
    }
}