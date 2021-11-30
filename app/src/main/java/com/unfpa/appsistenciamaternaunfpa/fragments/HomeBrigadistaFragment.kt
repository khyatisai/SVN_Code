package com.unfpa.appsistenciamaternaunfpa.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointment
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointmentByBrigadista
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant

import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * Created by KhyatiShah on 8/16/2019.
 */
class HomeBrigadistaFragment : androidx.fragment.app.Fragment() {

    val AppotmentByBrigadista = EndPoints.URL_HEROKU + "/api/v1/appointment/getbybrigadista"
    var getAppByBrigadista = EndPoints.URL_HEROKU + "/api/v1/appointments/getbybrigadista"

    private var adapter: RecyclerView.Adapter<CustomAdapterAppointment.MyViewHolder>? = null
    var getMyUserId = ""

    var HoraCitaPac: ArrayList<String> = ArrayList()
    var NombreCompletoPac: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_home_brigadista, container, false)

        val recyclerMyAppointment = rootView.findViewById<RecyclerView>(R.id.recyclerMyAppointment)
        val linearLayoutManagerApp = LinearLayoutManager(context!!)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        getMyUserId = AppUtils.getDataUser(this.context as Activity)

        /*****Get Appointment by toDay*****/
        /*****Get Appointment by toDay*****/
        /*****Get Appointment by toDay*****/
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        val date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val hm = "$hour:$minute:$second"

        val jsonobj = JSONObject()
        jsonobj.put("brigadistaid", getMyUserId)
        jsonobj.put("today", date)
        jsonobj.put("hour", hm)

        val que = Volley.newRequestQueue(this.context as Activity)
        val req = JsonObjectRequest(
            Request.Method.POST, AppotmentByBrigadista, jsonobj,
            Response.Listener { response ->

                var test = response.getString("message")

                if (response.getString("message") != "error" && response.getString("message") != "null") {
                    var horas = ""
                    var data = response.getJSONObject("data")
                    var paceinte = data.getJSONObject("patient")
                    var users = paceinte.getJSONObject("user")
                    var nombreCompleto =
                        users.getString("firstname") + " " + users.getString("lastname")

                    horas = data.getString("hour")
                    var fechaApp = data.getString("date")

                    val TypeAppointment = data.getString("typeAppointment")
                    var TypeAppointmentStr = "Tipo de cita"


                    if (TypeAppointment == "presencial")
                        TypeAppointmentStr = "Consulta Presencial"
                    else
                        TypeAppointmentStr = "Teleconsulta Médica"

                    rootView.findViewById<TextView>(R.id.txHour).setText(AppUtils.getTime12HourFormat(horas))
                    rootView.findViewById<TextView>(R.id.txtName).setText(nombreCompleto)
//                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
//                        .setBackgroundResource(R.drawable.text_view_border)
                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
                        .setText(TypeAppointmentStr)
                }
            },
            Response.ErrorListener { error ->
                println(error)
            })
        que.add(req)

        /******Get all Appointment by doctor******/
        /******Get all Appointment by doctor******/
        /******Get all Appointment by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("brigadistaid", getMyUserId)
        jsonobj2.put("today", dateTimeStr)

        try {
            val quee = Volley.newRequestQueue(this.context as Activity)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByBrigadista, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")
                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)
                        val horaFormat = AppUtils.getTime12HourFormat( dataArray.getJSONObject(i).getString("hour"))

                        HoraCitaPac.add(horaFormat)
                        var nombrecomplet =
                            dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(
                                i
                            ).getString("lastname")
                        NombreCompletoPac.add(nombrecomplet)
                    }
                    val customAdapter = CustomAdapterAppointmentByBrigadista(
                        activity,
                        HoraCitaPac,
                        NombreCompletoPac
                    )
                    recyclerMyAppointment.adapter = customAdapter
                    recyclerMyAppointment.adapter?.notifyDataSetChanged()

                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {


            //setting actionbar title
            //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
            val sharedPreference1 = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString("CountryCode", "")
            var field_enable_health_manager = "On"
            var field_enable_service_locator = "On"
            if (!CountryCode.isNullOrEmpty()) {
                val countryOfficeDAO =
                    MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO()
                val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode)
                field_enable_health_manager = countryCodeRequest.field_enable_health_manager
                field_enable_service_locator = countryCodeRequest.field_enable_service_locator
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 20, 0)//(left, top, right, bottom)


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}