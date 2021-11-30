package com.unfpa.appsistenciamaternaunfpa.adapters.my_health

import android.app.Dialog
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.dialog_delete_reminder.*
import kotlinx.android.synthetic.main.item_medicine_reminder.view.*


/**
 * Created by KhyatiShah on 10/17/2019.
 */
class MedicineReminderAdapter(activity: androidx.fragment.app.FragmentActivity, listener: Listener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    /*  init {
          setHasStableIds(true)
      }*/

    private lateinit var medicineReminderList: List<MedicineReminder>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    val listener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return MedicineReminderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_medicine_reminder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(itemView: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val medicineReminderViewHolder = itemView as MedicineReminderViewHolder
        var medicineReminder: MedicineReminder
        if (position != medicineReminderList.size) {
            medicineReminder = medicineReminderList.get(position)
        } else {
            medicineReminder = medicineReminderList.get(position - 1)
        }

        if (position != medicineReminderList.size) {
            medicineReminderViewHolder.cardContainer.visibility = View.VISIBLE
            medicineReminderViewHolder.addContainer.visibility = View.GONE
            val modVal: Double = (position.toDouble() + 1) % 4

            if (modVal == 1.0) {
                medicineReminderViewHolder.imgMedicine.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.ic_myhealth_med_red
                    )
                )
                medicineReminderViewHolder.cardContainer.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.colorLightOrange
                    )
                );
            } else if (modVal == 2.0) {
                medicineReminderViewHolder.imgMedicine.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.ic_myhealth_med_blue
                    )
                )
                medicineReminderViewHolder.cardContainer.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.colorBlue
                    )
                );
            } else if (modVal == 3.0) {
                medicineReminderViewHolder.imgMedicine.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.ic_myhealth_med_violet
                    )
                )
                medicineReminderViewHolder.cardContainer.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.colorLightPink
                    )
                );
            } else {
                medicineReminderViewHolder.imgMedicine.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.ic_myhealth_med_yellow
                    )
                )
                medicineReminderViewHolder.cardContainer.setCardBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.colorYellow
                    )
                );
            }

            try {
                medicineReminderViewHolder.imgDelete.setOnClickListener {
                    showDelPopup(medicineReminder)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            medicineReminderViewHolder.medicineName.text = medicineReminder.medicineName
            val time = AppUtils.getTime12HourFormat(medicineReminder.time)
            medicineReminderViewHolder.txtDoesage.text = medicineReminder.dose.toString() + " does at " + time
            if (medicineReminder.withFood)
                medicineReminderViewHolder.txtFoodDetail.text = activity.getString(R.string.with_food)
            else
                medicineReminderViewHolder.txtFoodDetail.text = activity.getString(R.string.without_food)
        } else {
            medicineReminderViewHolder.cardContainer.visibility = View.GONE
            medicineReminderViewHolder.addContainer.visibility = View.VISIBLE
        }
        itemView.addContainer.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.OTHER_HEALTH_TOOL, activity)
            //call listener
            listener.onComplete("add")
        }
    }

    override fun getItemCount(): Int {
        if (medicineReminderList.isEmpty()) {
            return 0
        } else {
            return medicineReminderList.size + 1
        }
    }

    /* override fun getItemId(position: Int): Long {
         return position.toLong()
     }

     override fun getItemViewType(position: Int): Int {
         return position
     }*/

    class MedicineReminderViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val imgMedicine = itemView.imgMedicine
        val cardContainer = itemView.cardContainer
        val addContainer = itemView.addContainer
        val txtFoodDetail = itemView.txtFoodDetail
        val txtDoesage = itemView.txtDoesage
        val imgDelete = itemView.imgDelete
        val medicineName = itemView.medicineName
    }

    fun setMedicineReminderList(medicineReminderList: List<MedicineReminder>) {
        this.medicineReminderList = medicineReminderList
        notifyDataSetChanged()
    }

    fun showDelPopup(medicineReminder: MedicineReminder) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_delete_reminder)
        dialog.txtNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.txtYes.setOnClickListener {
            dialog.dismiss()
            listener.onComplete(medicineReminder.medicineReminderId.toString())
        }
        dialog.show()
    }
}