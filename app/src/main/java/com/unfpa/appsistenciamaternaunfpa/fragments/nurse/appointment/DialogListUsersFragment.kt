package com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.CustomAdapterGetAllUserBySearch
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.btn_button1
import kotlinx.android.synthetic.main.bottomsheet_list_users_paciente.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.lang.Exception
import kotlin.collections.ArrayList

class DialogListUsersFragment : BottomSheetDialogFragment() {
    //val GetUserList = EndPoints.URL_HEROKU + "/api/v1/user/getuserlike"
    val GetUserList = EndPoints.URL_HEROKU + "/api/v1/nurse/user/getuserlike"

    var IdUser: ArrayList<String> = ArrayList()
    var IdBrigadista: ArrayList<String> = ArrayList()
    var cedulaList: ArrayList<String> = ArrayList()
    var NombreCompletoBrigadista: ArrayList<String> = ArrayList()
    var GestationWeeks: ArrayList<String> = ArrayList()
    var GestationWeeksDate: ArrayList<String> = ArrayList()
    var PathologicalAntecedents: ArrayList<String> = ArrayList()
    var TreatmentsReceived: ArrayList<String> = ArrayList()
    var MedicalObservations: ArrayList<String> = ArrayList()

    var tipo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_list_users_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        IdBrigadista.clear()
        cedulaList.clear()
        NombreCompletoBrigadista.clear()
        GestationWeeks.clear()
        PathologicalAntecedents.clear()
        TreatmentsReceived.clear()
        MedicalObservations.clear()

        val recyclerMyAppointment = view.findViewById<RecyclerView>(R.id.recyclerBrigadista)
        val linearLayoutManagerApp = LinearLayoutManager(this.context as Activity)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        val busqueda = arguments?.getString("busqueda")
        tipo = arguments?.getString("tipo").toString()

        btn_button1.setOnClickListener {
            this.dismiss()
        }
        val jsonobj = JSONObject()

        try {
            llProgressBar.visibility = View.VISIBLE
            val getMyUserId = context?.let { AppUtils.getDataUser(it) }
            jsonobj.put("params", busqueda)
            //jsonobj.put("type", "1")
            jsonobj.put("nurseId", getMyUserId)

            val quee = Volley.newRequestQueue(this.context as Activity)
            val reqq = JsonObjectRequest(
                Request.Method.POST, GetUserList, jsonobj,
                Response.Listener { response ->

                    if (response.getString("message") != "error") {
                        var dataArray = response.getJSONArray("response")


                        var idPaciente = "0"
                        var gestationWeeks = ""
                        var gestationWeeksDate = ""
                        for (i in 0 until dataArray.length()) {
                            var idUser = dataArray.getJSONObject(i).getString("id")
                            if (tipo == "1") {
                                idPaciente = dataArray.getJSONObject(i).getString("patientId")
                            }
                            gestationWeeks = dataArray.getJSONObject(i).getString("gestationWeeks")
                            gestationWeeksDate =
                                dataArray.getJSONObject(i).getString("gestationWeeksDate")
                            var pathologicalAntecedents =
                                dataArray.getJSONObject(i).getString("pathologicalAntecedents")
                            var treatmentsReceived =
                                dataArray.getJSONObject(i).getString("treatmentsReceived")
                            var medicalObservation =
                                dataArray.getJSONObject(i).getString("medicalObservations")
                            var cedula = dataArray.getJSONObject(i).getString("cedula")
                            var nombrecomplet =
                                dataArray.getJSONObject(i)
                                    .getString("firstname") + " " + dataArray.getJSONObject(
                                    i
                                ).getString("lastname")

                            IdUser.add(idUser)
                            IdBrigadista.add(idPaciente)
                            cedulaList.add(cedula)
                            NombreCompletoBrigadista.add(nombrecomplet)
                            GestationWeeks.add(gestationWeeks)
                            GestationWeeksDate.add(gestationWeeksDate)
                            PathologicalAntecedents.add(pathologicalAntecedents)
                            TreatmentsReceived.add(treatmentsReceived)
                            MedicalObservations.add(medicalObservation)
                        }
                        val customAdapter = CustomAdapterGetAllUserBySearch(
                            this!!.activity!!,
                            IdUser,
                            IdBrigadista,
                            cedulaList,
                            NombreCompletoBrigadista,
                            GestationWeeks,
                            GestationWeeksDate,
                            PathologicalAntecedents,
                            TreatmentsReceived,
                            MedicalObservations,
                            tipo,
                            this
                        )
                        recyclerMyAppointment.adapter = customAdapter
                        recyclerMyAppointment.adapter?.notifyDataSetChanged()
                        llProgressBar.visibility = View.GONE
                    } else {
                        val layoutToast =
                            layoutInflater.inflate(
                                R.layout.custom_toast_layout,
                                custom_toast_container
                            )
                        context?.let { it1 ->
                            AppUtils.createCustomToast(
                                "No se encontraron coincidencias",
                                 it1, layoutToast, "warning"
                            )
                        }
                        llProgressBar.visibility = View.GONE
                    }

                },
                Response.ErrorListener { error ->

                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    context?.let { it1 ->
                        AppUtils.createCustomToast(
                            AppUtils.getVolleyError(
                                error,
                                this!!.activity!!
                            ), it1, layoutToast, "warning"
                        )
                    }
                    llProgressBar.visibility = View.GONE
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
            llProgressBar.visibility = View.GONE
        }

    }

}