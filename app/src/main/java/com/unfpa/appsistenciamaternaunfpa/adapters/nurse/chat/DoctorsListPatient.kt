package com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat

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
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.DialogAddPacienteToDoctorFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogListUsersFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DoctorScheduleList
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONArray
import org.json.JSONObject

class DoctorsListPatient(
    doctorList: JSONArray,
    dialogAddPacienteToDoctorFragment: DialogAddPacienteToDoctorFragment
) :
    RecyclerView.Adapter<DoctorsListPatient.MyViewHolder>() {

    var selectedPos = -1
    val docList = doctorList
    val dialogAddPacienteToDoctorFragment: DialogAddPacienteToDoctorFragment =
        dialogAddPacienteToDoctorFragment

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorsListPatient.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(p0: DoctorsListPatient.MyViewHolder, p1: Int) {
        var doctor: JSONObject = docList.getJSONObject(p1)
        p0.tvDocName.text = doctor.getString("firstname") + " " + doctor.getString("lastname")
        p0.btnSelecDoc.isChecked = p1 == selectedPos
        p0.btnSelecDoc.setOnClickListener {
            selectedPos = p1
            dialogAddPacienteToDoctorFragment.selectedDoctorId = doctor.getString("id")
            notifyDataSetChanged()
        }
        p0.llScheduleLink.visibility = View.GONE

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