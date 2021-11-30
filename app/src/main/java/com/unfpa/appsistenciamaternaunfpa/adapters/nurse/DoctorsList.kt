package com.unfpa.appsistenciamaternaunfpa.adapters.nurse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogListUsersFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DoctorScheduleList
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONArray
import org.json.JSONObject

class DoctorsList(doctorList: JSONArray, dialogFromCalendarFragment: DialogFromCalendarFragment) :
    RecyclerView.Adapter<DoctorsList.MyViewHolder>() {

    var selectedPos = -1
    val docList = doctorList
    val dialogFromCalendarFragment: DialogFromCalendarFragment = dialogFromCalendarFragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorsList.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(p0: DoctorsList.MyViewHolder, p1: Int) {
        var doctor: JSONObject = docList.getJSONObject(p1)
        p0.tvDocName.text = doctor.getString("firstname") + " " + doctor.getString("lastname")
        p0.btnSelecDoc.isChecked = p1 == selectedPos
        p0.btnSelecDoc.setOnClickListener {
            selectedPos = p1
            dialogFromCalendarFragment.selectedDoctorId = doctor.getString("id")
            notifyDataSetChanged()
        }
        p0.llScheduleLink.setOnClickListener {
            if (dialogFromCalendarFragment.enteredDt != "") {
                val bottomSheetFragment = DoctorScheduleList()
                val bundle = Bundle()
                bundle.putString("doctorid", doctor.getString("id"))
                bundle.putString("today", dialogFromCalendarFragment.dateFormat)
                bundle.putString(
                    "DoctorName",
                    doctor.getString("firstname") + " " + doctor.getString("lastname")
                )
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(
                    dialogFromCalendarFragment.childFragmentManager,
                    "bottomSheetFragmentFromCalendar"
                )
            } else {

                Toast.makeText(
                    dialogFromCalendarFragment.context, "Please Select date",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return docList.length()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDocName: TextView = itemView.findViewById<View>(R.id.tvDocName) as TextView
        var tvSpecialization: TextView =
            itemView.findViewById<View>(R.id.tvSpecialization) as TextView
        var llScheduleLink: LinearLayout =
            itemView.findViewById<View>(R.id.llScheduleLink) as LinearLayout
        var btnSelecDoc: RadioButton = itemView.findViewById<View>(R.id.btnSelecDoc) as RadioButton
    }
}