package com.unfpa.appsistenciamaternaunfpa.fragments.brigadista

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointment_dos
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointmentsByBrigadista
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_appointment_calendar_brigadista.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by KhyatiShah on 2/7/2020.
 */
class CalendarioFragment : androidx.fragment.app.Fragment() {

    var getAppByBrigadista = EndPoints.URL_HEROKU + "/api/v1/appointments/getbybrigadista"
    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    var bootomsheetFragment = DialogFromCalendarFragment()
    var HoraCitaPac: ArrayList<String> = ArrayList()
    private var adapter: RecyclerView.Adapter<CustomAdapterAppointment_dos.MyViewHolder>? = null
    var NombreCompletoPac: ArrayList<String> = ArrayList()
    var typeAppointment: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_appointment_calendar_brigadista, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerBrigadist = view.findViewById<RecyclerView>(R.id.rvAppointmentByBrigadist)
        val linearLayoutManagerApp = LinearLayoutManager(context)
        recyclerBrigadist.layoutManager = linearLayoutManagerApp

        calendarViewBrigadist?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            selectedDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", selectedDt)
            getAppByBrigadista(recyclerBrigadist,dateTimeStr)
        }
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr2 = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", dateToFormatStr)
        getAppByBrigadista(recyclerBrigadist, dateTimeStr2)
    }

    fun getAppByBrigadista(recyclerView: RecyclerView, today: String){

        rvAppointmentByBrigadist.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this!!.activity!!)


        var  getMyUserId = AppUtils.getDataUser(this!!.activity!!)

        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("brigadistaid", getMyUserId)
        jsonobj2.put("today", today)

        HoraCitaPac.clear()
        typeAppointment.clear()
        NombreCompletoPac.clear()

        try {
            val quee = Volley.newRequestQueue(this!!.activity!! )
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByBrigadista, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")
                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        val horaFormat = AppUtils.getTime12HourFormat( dataArray.getJSONObject(i).getString("hour"))

                        HoraCitaPac.add(horaFormat)
                        var nombrecomplet = dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(i).getString("lastname")
                        typeAppointment.add(dataArray.getJSONObject(i).getString("typeAppointment"))
                        NombreCompletoPac.add(nombrecomplet)
                    }
                    val customAdapter = CustomAdapterAppointmentsByBrigadista(
                        this!!.activity!!,
                        HoraCitaPac,
                        NombreCompletoPac
                    )
                    rvAppointmentByBrigadist.adapter = customAdapter
                    rvAppointmentByBrigadist.adapter?.notifyDataSetChanged()
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = false
        }
    }
}