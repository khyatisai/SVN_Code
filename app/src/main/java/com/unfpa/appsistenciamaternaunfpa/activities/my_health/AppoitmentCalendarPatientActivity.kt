package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointment_dos
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_appointment_calendar_patient.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class AppoitmentCalendarPatientActivity : AppCompatActivity() {
    var getAppByDoctor = EndPoints.URL_HEROKU + "/api/v1/appointments/getbypatient"
    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    var bootomsheetFragment = DialogFromCalendarFragment()
    var HoraCitaPac: ArrayList<String> = ArrayList()
    private var adapter: RecyclerView.Adapter<CustomAdapterAppointment_dos.MyViewHolder>? = null
    var NombreCompletoPac: ArrayList<String> = ArrayList()
    var typeAppointment: ArrayList<String> = ArrayList()

        override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_calendar_patient)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.CalendarioCita)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.citas_arrow_back_calendar)
            toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                override
                fun onClick(v: View) {
                    finish()
                }
        })
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(20);
//        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
//            numberOfDays = newVal
//        }
        //seeting min and max dates for period tracker
        var c = Calendar.getInstance()
        selectedDt = DateFormat.format(Constant.PERIOD_DATE_FORMAT, c).toString()
        //c.add(Calendar.MONTH, -1)
        //c.add(Calendar.DAY_OF_WEEK, -28)
        c.add(Calendar.DAY_OF_WEEK, -45)
        calendarViewPac.minDate = c.timeInMillis
        c.add(Calendar.MONTH, 2)
        calendarViewPac.maxDate = c.timeInMillis

        val recyclerMyAppointment = findViewById<RecyclerView>(R.id.recyclerListMyAppointment)
        val linearLayoutManagerApp = LinearLayoutManager(this)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        calendarViewPac?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            selectedDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", selectedDt)
            getAppointmentByToday(recyclerMyAppointment,dateTimeStr)
        }


        var  getMyUserId = AppUtils.getDataUser(this)
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("patient", getMyUserId)
        jsonobj2.put("today", dateTimeStr)

        try {
            val quee = Volley.newRequestQueue(this )
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByDoctor, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")

                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        HoraCitaPac.add(AppUtils.getTime12HourFormat(dataArray.getJSONObject(i).getString("hour")))
                        var nombrecomplet = "Dr. " + dataArray.getJSONObject(i).getJSONObject("doctor").getString("firstname") + " " + dataArray.getJSONObject(i).getJSONObject("doctor").getString("lastname")
                        typeAppointment.add(dataArray.getJSONObject(i).getString("typeAppointment"))
                        NombreCompletoPac.add(nombrecomplet)
                    }

                    val customAdapter = CustomAdapterAppointment_dos(
                        this,
                        HoraCitaPac,
                        NombreCompletoPac,
                        typeAppointment
                    )
                    recyclerMyAppointment.adapter = customAdapter

                    recyclerMyAppointment.adapter?.notifyDataSetChanged()

                    // only create and set a new adapter if there isn't already one
//                    if (adapter !== null) {
//
//                        adapter = CustomAdapterAppointment_dos(this.context as Activity, HoraCitaPac, NombreCompletoPac)
//                        recyclerMyAppointment.setAdapter(adapter);
//                    }


                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private  fun getAppointmentByToday(recyclerView: RecyclerView, today: String){

        var  getMyUserId = AppUtils.getDataUser(this)
        val jsonobj2 = JSONObject()
        jsonobj2.put("patient", getMyUserId)
        jsonobj2.put("today", today)
        HoraCitaPac.clear()
        typeAppointment.clear()
        NombreCompletoPac.clear()

        try {
            val quee = Volley.newRequestQueue(this )
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByDoctor, jsonobj2,
                Response.Listener { response ->

                    var dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        HoraCitaPac.add(dataArray.getJSONObject(i).getString("hour"))
                        var nombrecomplet = "Dr. " + dataArray.getJSONObject(i).getJSONObject("doctor").getString("firstname") + " " + dataArray.getJSONObject(i).getJSONObject("doctor").getString("lastname")
                        typeAppointment.add(dataArray.getJSONObject(i).getString("typeAppointment"))
                        NombreCompletoPac.add(nombrecomplet)
                    }
                    val customAdapter = CustomAdapterAppointment_dos(
                        this,
                        HoraCitaPac,
                        NombreCompletoPac,
                        typeAppointment
                    )
                    recyclerView.adapter = customAdapter
                    recyclerView.adapter?.notifyDataSetChanged()
                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = true
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.calendar).isVisible = false
            menu.findItem(R.id.add).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent()
                setResult(2, intent);
                finish()
            }
            R.id.add -> {
                bootomsheetFragment.show(supportFragmentManager,"Bottom sheet dialog")
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}