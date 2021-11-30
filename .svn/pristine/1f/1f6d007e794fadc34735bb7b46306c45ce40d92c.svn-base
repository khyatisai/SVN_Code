package com.unfpa.appsistenciamaternaunfpa.adapters.my_health

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.dialog_delete_reminder.*
import kotlinx.android.synthetic.main.item_appointment_reminder.view.*
import java.text.SimpleDateFormat

/**
 * Created by KhyatiShah on 10/22/2019.
 */
class AppointmentReminderAdapter(activity: androidx.fragment.app.FragmentActivity, listener: Listener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var appointmentReminderList: List<AppointmentReminder>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    val listener = listener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return AppointReminderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_appointment_reminder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (appointmentReminderList.isEmpty()) {
            return 0
        } else {
            return appointmentReminderList.size + 1
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(itemView: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val appointmentReminderViewHolder = itemView as AppointReminderViewHolder
        if (position != appointmentReminderList.size) {
            //setting layout visibility
            appointmentReminderViewHolder.cardAddAppointment.visibility = View.GONE
            appointmentReminderViewHolder.cardContainer.visibility = View.VISIBLE

            appointmentReminderViewHolder.txtAppointmentName.text =
                appointmentReminderList.get(position).appointmentName
            appointmentReminderViewHolder.imgDelete.setOnClickListener {
                showDelPopup(appointmentReminderList.get(position))
            }
            val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
            val targetFormat = SimpleDateFormat(Constant.APPOINTMENT_DISPLAY_DATE_FORMAT)
            val date = inputDateFormat.parse(appointmentReminderList.get(position).date)
            val displayDate = targetFormat.format(date)
            val strTimeDate =
                displayDate + " " + AppUtils.getTime12HourFormat(appointmentReminderList.get(position).time)
            appointmentReminderViewHolder.txtDateTime.text = strTimeDate
            appointmentReminderViewHolder.txtCenterName.text = appointmentReminderList.get(position).serviceCenterName
        } else {
            appointmentReminderViewHolder.cardAddAppointment.visibility = View.VISIBLE
            appointmentReminderViewHolder.cardContainer.visibility = View.GONE
        }
        appointmentReminderViewHolder.cardAddAppointment.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.OTHER_HEALTH_TOOL, activity)
            //call listener
            listener.onComplete("add")
        }
    }

    class AppointReminderViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val txtAppointmentName = itemView.txtAppointmentName
        val imgDelete = itemView.imgDelete
        val txtDateTime = itemView.txtDateTime
        val txtCenterName = itemView.txtCenterName
        val cardAddAppointment = itemView.cardAddAppointment
        val cardContainer = itemView.cardContainer
    }

    fun showDelPopup(appointmentReminder: AppointmentReminder) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_delete_reminder)
        dialog.txtMessage.text = activity.getString(R.string.message_del_appointment)
        dialog.txtNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.txtYes.setOnClickListener {
            dialog.dismiss()
            listener.onComplete(appointmentReminder.appointmentId.toString())
        }
        dialog.show()
    }

    fun setAppointmnetList(appointmentReminderList: List<AppointmentReminder>) {
        this.appointmentReminderList = appointmentReminderList
        notifyDataSetChanged()
    }
}