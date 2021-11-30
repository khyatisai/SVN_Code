package com.unfpa.appsistenciamaternaunfpa.fragments.brigadista

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.CustomAdapterPregnantListByBrigadista
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_embarazadas.*
import org.json.JSONObject
import java.util.*


class EmbarazadasFragment : androidx.fragment.app.Fragment() {

    var getPregnantByBrigadista = EndPoints.URL_HEROKU + "/api/v1/patients/totalappointmentbybrigadist"
    var nombrePac: ArrayList<String> = ArrayList()
    var tvCitasPersona: ArrayList<String> = ArrayList()
    var tvCitasTelemedicina: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_embarazadas, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllPacient()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = false
        }
    }

    fun getAllPacient(){
        /******Get Pregnant by brigadista******/
        val dateToFormatStr = DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        val getMyUserId = AppUtils.getDataUser(this!!.activity!!)

        jsonobj2.put("brigadistaid", getMyUserId)

        val quee = Volley.newRequestQueue(this!!.activity!!)
        val reqq = JsonObjectRequest(
            Request.Method.POST, getPregnantByBrigadista, jsonobj2,
            Response.Listener { response ->

                var dataArray  = response.getJSONArray("patient")
                recyclerPacientByBrigadista.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this!!.activity!!)

                for (i in 0 until dataArray.length()) {
                    val userDetail = dataArray.getJSONObject(i)

                    var nombrecomplet = dataArray.getJSONObject(i).getString("user_firstname") +" "+ dataArray.getJSONObject(i).getString("user_lastname")
                    nombrePac.add(nombrecomplet)
                    tvCitasPersona.add(dataArray.getJSONObject(i).getString("totalPresencial"))
                    tvCitasTelemedicina.add(dataArray.getJSONObject(i).getString("totalRemoto"))
                }

                val customAdapter = CustomAdapterPregnantListByBrigadista(this!!.activity!!, nombrePac, tvCitasPersona,tvCitasTelemedicina)
                recyclerPacientByBrigadista.adapter = customAdapter
            },
            Response.ErrorListener { error ->
                println(error)
            })
        quee.add(reqq)
    }
}