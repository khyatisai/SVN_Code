package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_medicine_reminder.view.*

/**
 * Created by KhyatiShah on 10/25/2019.
 */
class MedicineReminderAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    /*  init {
          setHasStableIds(true)
      }*/

    private lateinit var medicineReminderList: List<MedicineReminder>
    private var activity: androidx.fragment.app.FragmentActivity = activity

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
        val medicineReminder = medicineReminderList.get(position)

        medicineReminderViewHolder.imgDelete.visibility = View.GONE
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

        medicineReminderViewHolder.medicineName.text = medicineReminder.medicineName
        val time = AppUtils.getTime12HourFormat(medicineReminder.time)
        medicineReminderViewHolder.txtDoesage.text = medicineReminder.dose.toString() + " does at " + time
        if (medicineReminder.withFood)
            medicineReminderViewHolder.txtFoodDetail.text = activity.getString(R.string.with_food)
        else
            medicineReminderViewHolder.txtFoodDetail.text = activity.getString(R.string.without_food)


    }

    override fun getItemCount(): Int {
        return medicineReminderList.size
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

}