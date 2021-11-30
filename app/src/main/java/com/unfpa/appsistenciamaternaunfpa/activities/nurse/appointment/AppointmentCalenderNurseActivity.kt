package com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.CustomAdapterAppointment_dos
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_appointment_calendar_doctor.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentCalenderNurseActivity : AppCompatActivity() {

    // var getAppByDoctor = EndPoints.URL_HEROKU + "/api/v1/appointment/getbynurse"
    var getAppByDoctor = EndPoints.URL_HEROKU + "/api/v1/appointment/getbynursefordate"
    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    var bootomsheetFragment = DialogFromCalendarFragment()
    var HoraCitaPac: ArrayList<String> = ArrayList()
    private var adapter: RecyclerView.Adapter<CustomAdapterAppointment_dos.MyViewHolder>? = null
    var NombreCompletoPac: ArrayList<String> = ArrayList()
    var typeAppointment: ArrayList<String> = ArrayList()
    var docName: ArrayList<String> = ArrayList()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_calender_nurse)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.CalendarioCita)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })
        var c = Calendar.getInstance()
        selectedDt = DateFormat.format(Constant.PERIOD_DATE_FORMAT, c).toString()
        //c.add(Calendar.MONTH, -1)
        //c.add(Calendar.DAY_OF_WEEK, -28)
        c.add(Calendar.DAY_OF_WEEK, -45)
        calendarView2.minDate = c.timeInMillis
        c.add(Calendar.MONTH, 2)
        calendarView2.maxDate = c.timeInMillis

        val recyclerMyAppointment = findViewById<RecyclerView>(R.id.recyclerListMyAppointment)
        val linearLayoutManagerApp = LinearLayoutManager(this)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        calendarView2?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            selectedDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", selectedDt)
            getAppointmentByDate(recyclerMyAppointment, dateTimeStr)
        }
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", dateToFormatStr)
        getAppointmentByDate(recyclerMyAppointment, dateTimeStr)
    }

    private fun getAppointmentByDate(recyclerView: RecyclerView, today: String) {
        var getMyUserId = AppUtils.getDataUser(this)

        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("nurseid", getMyUserId)
        jsonobj2.put("today", today)

        HoraCitaPac.clear()
        NombreCompletoPac.clear()
        typeAppointment.clear()
        docName.clear()
        try {

            llProgressBar.visibility = View.VISIBLE
            val quee = Volley.newRequestQueue(this)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByDoctor, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")
                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        HoraCitaPac.add(
                            AppUtils.getTime12HourFormat(
                                dataArray.getJSONObject(i).getString("hour")
                            )
                        )
                        var nombrecomplet = dataArray.getJSONObject(i)
                            .getString("firstname") + " " + dataArray.getJSONObject(i)
                            .getString("lastname")
                        typeAppointment.add(dataArray.getJSONObject(i).getString("typeAppointment"))
                        NombreCompletoPac.add(nombrecomplet)
                        docName.add(dataArray.getJSONObject(i).getString("doctorname"))
                    }
                    val customAdapter = CustomAdapterAppointment_dos(
                        this,
                        HoraCitaPac,
                        NombreCompletoPac,
                        typeAppointment, docName
                    )
                    recyclerView.adapter = customAdapter
                    recyclerView.adapter?.notifyDataSetChanged()
                    llProgressBar.visibility = View.GONE
                },
                Response.ErrorListener { error ->
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    this?.let { it1 ->
                        AppUtils.createCustomToast(
                            AppUtils.getVolleyError(error, this),
                            it1,
                            layoutToast,
                            "warning"
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_nurse, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = false
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.calendar).isVisible = true
            menu.findItem(R.id.add).isVisible = true
            menu.findItem(R.id.calendar).isVisible = false
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
                bootomsheetFragment.show(supportFragmentManager, "tagdialogcalendar")
            }

        }
        return super.onOptionsItemSelected(item)
    }
}