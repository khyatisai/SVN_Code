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
import kotlinx.android.synthetic.main.activity_appointment_calendar_doctor.*

import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppoitmentCalendarDoctorActivity : AppCompatActivity() {
    var getAppByDoctor = EndPoints.URL_HEROKU + "/api/v1/appointment/getbydoctor"
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
        setContentView(R.layout.activity_appointment_calendar_doctor)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.CalendarioCita)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
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
            getAppointmentByDate(recyclerMyAppointment,dateTimeStr)
        }

        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", dateToFormatStr)


        val simpleDateFormat = SimpleDateFormat("MMM YYYY")
//        txtMonthName.text = simpleDateFormat.format(compactcalendar_view.getFirstDayOfCurrentMonth())
//
//        //set title on calendar scroll
//        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
//            override fun onDayClick(dateClicked: Date) {}
//
//            override fun onMonthScroll(firstDayOfNewMonth: Date) {
//                txtMonthName.text = simpleDateFormat.format(compactcalendar_view.getFirstDayOfCurrentMonth())
//            }
//        })

       //compactcalendar_view.addEvent(Event(Color.GREEN, c.timeInMillis+1), true)

        getAppointmentByDate(recyclerMyAppointment,dateTimeStr)

    }

    private fun getAppointmentByDate(recyclerView: RecyclerView, today: String){
        var  getMyUserId = AppUtils.getDataUser(this)

        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("doctorid", getMyUserId)
        jsonobj2.put("today", today)

        HoraCitaPac.clear()
        NombreCompletoPac.clear()
        typeAppointment.clear()

        try {

            llProgressBar.visibility = View.VISIBLE
            val quee = Volley.newRequestQueue(this )
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByDoctor, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")
                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        HoraCitaPac.add(AppUtils.getTime12HourFormat(dataArray.getJSONObject(i).getString("hour")))
                        var nombrecomplet = dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(i).getString("lastname")
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
                    llProgressBar.visibility = View.GONE
                },
                Response.ErrorListener { error ->
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    this?.let { it1 ->
                        AppUtils.createCustomToast(AppUtils.getVolleyError(error, this), it1, layoutToast, "warning")
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
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
                bootomsheetFragment.show(supportFragmentManager,"tagdialogcalendar")
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}