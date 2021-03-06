package com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.CustomAdapterGetAllUserBySearch
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.DoctorSchedule
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.*
import kotlinx.android.synthetic.main.bottomsheet_list_users_paciente.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.dialog_schedule_list.*
import kotlinx.android.synthetic.main.dialog_schedule_list.view.*
import org.json.JSONObject
import java.lang.Exception

class DoctorScheduleList : BottomSheetDialogFragment() {

    val GetAppointmentSchedule = EndPoints.URL_HEROKU + "/api/v1/appointment/getbydoctor"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerMyAppointment = view.findViewById<RecyclerView>(R.id.recyclerSchedule)
        val linearLayoutManagerApp = GridLayoutManager(this.context, 2)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        val jsonobj = JSONObject()

        try {
            val today = arguments?.getString("today")
            val doctorid = arguments?.getString("doctorid").toString()
            val docName = arguments?.getString("DoctorName").toString()
            tvdocName.text = "Dr. " + docName
            date.text = today?.let { AppUtils.dateConverter("yyyy-MM-dd", "dd-MM-yyyy", it) }
            jsonobj.put("doctorid", doctorid)
            jsonobj.put("today", today)

            view.btn_button1.setOnClickListener {
                this.dismiss()
            }
            val quee = Volley.newRequestQueue(this.context as Activity)
            val reqq = JsonObjectRequest(
                Request.Method.POST, GetAppointmentSchedule, jsonobj,
                Response.Listener { response ->

                    if (response.getString("message") != "error") {
                        val obj = response.getJSONArray("data")
                        var customAdapter = DoctorSchedule(obj)
                        recyclerMyAppointment.adapter = customAdapter
                        recyclerMyAppointment.adapter?.notifyDataSetChanged()
                        if (obj.length() == 0) {
                            txtNoAppointment.visibility = View.VISIBLE
                            recyclerMyAppointment.visibility = View.GONE
                        } else {
                            txtNoAppointment.visibility = View.GONE
                            recyclerMyAppointment.visibility = View.VISIBLE
                        }
                    }

                },
                Response.ErrorListener { error ->

                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    context?.let { it1 ->
                        AppUtils.createCustomToast(
                            AppUtils.getVolleyError(
                                error,
                                this!!.requireActivity()
                            ), it1, layoutToast, "warning"
                        )
                    }
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}