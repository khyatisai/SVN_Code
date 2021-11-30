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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainPregnantActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.CustomAdapterDoctorList
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_doctors_list.*
import kotlinx.android.synthetic.main.activity_paciente.recyclerMyPacient
import kotlinx.android.synthetic.main.activity_period_tracker_input.numberPicker
import org.json.JSONObject
import java.util.*


class ListDoctorsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    var totalMsgDoc: ArrayList<String> = ArrayList()
    var namedoctor: ArrayList<String> = ArrayList()
    var docId: ArrayList<String> = ArrayList()

    //Nurse
    var totalMsgNurse: ArrayList<String> = ArrayList()
    var nameNurse: ArrayList<String> = ArrayList()
    var nurseId: ArrayList<String> = ArrayList()


    var getDoctorByPregnant = EndPoints.URL_HEROKU + "/api/v1/patients/getdoctors"

    //var getNurse = EndPoints.URL_HEROKU + "/api/v1/user/getnurse"
    var getNurse = EndPoints.URL_HEROKU + "/api/v1/patients/getnurse"

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_list)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Mensajes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                val intent = Intent(application, MainPregnantActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })

        getDoctor()
        getNurse()

        /*numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            numberOfDays = newVal
        }*/
        //seeting min and max dates for period tracker

    }

    fun getNurse() {
        /******Get all Pregnant by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        val getMyUserId = AppUtils.getDataUser(this)

        jsonobj2.put("userid", getMyUserId)

        val quee = Volley.newRequestQueue(this)
        val reqq = JsonObjectRequest(
            Request.Method.POST, getNurse, jsonobj2,
            Response.Listener { response ->
                /* var dataArray = response.getJSONArray("patient")

                 recyclerNurses.layoutManager =
                     androidx.recyclerview.widget.LinearLayoutManager(this)

                 for (i in 0 until dataArray.length()) {
                     val userDetail = dataArray.getJSONObject(i)

                     var nombrecomplet = dataArray.getJSONObject(i)
                         .getString("nurse_firstname") + " " + dataArray.getJSONObject(i)
                         .getString("nurse_lastname")
                     var doctorid = dataArray.getJSONObject(i).getString("nurse_id")
                     val msg = dataArray.getJSONObject(i).getString("contador")

                     totalMsgNurse.add(msg)
                     nameNurse.add(nombrecomplet)
                     nurseId.add(doctorid)
                 }

                 if (!nurseId.isEmpty()) {
                     val customAdapter =
                         com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.CustomAdapterDoctorList(
                             this,
                             nameNurse,
                             nurseId,
                             totalMsgNurse
                         )
                     recyclerNurses.adapter = customAdapter*/
                var patientObj = response.getJSONArray("patient").getJSONObject(0)
                var nurseArray = patientObj.getJSONArray("nurseData")
                recyclerNurses.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this)

                for (i in 0 until nurseArray.length()) {
                    val nurseDetail = nurseArray.getJSONObject(i)

                    var nombrecomplet = nurseDetail
                        .getString("nurse_firstname") + " " + nurseDetail
                        .getString("nurse_lastname")
                    var doctorid = nurseDetail.getString("nurse_id")
                    val msg = nurseDetail.getString("contador")

                    totalMsgNurse.add(msg)
                    nameNurse.add(nombrecomplet)
                    nurseId.add(doctorid)
                }

                if (!nurseId.isEmpty()) {
                    val customAdapter =
                        com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.CustomAdapterDoctorList(
                            this,
                            nameNurse,
                            nurseId,
                            totalMsgNurse
                        )
                    recyclerNurses.adapter = customAdapter
                }
            },
            Response.ErrorListener { error ->
                println(error)
            })
        quee.add(reqq)
    }

    fun getDoctor() {
        /******Get all Pregnant by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        val getMyUserId = AppUtils.getDataUser(this)

        jsonobj2.put("userid", getMyUserId)

        val quee = Volley.newRequestQueue(this)
        val reqq = JsonObjectRequest(
            Request.Method.POST, getDoctorByPregnant, jsonobj2,
            Response.Listener { response ->
                var dataArray = response.getJSONArray("patient")

                recyclerMyPacient.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this)

                for (i in 0 until dataArray.length()) {
                    val userDetail = dataArray.getJSONObject(i)

                    var nombrecomplet = dataArray.getJSONObject(i)
                        .getString("doctors_firstname") + " " + dataArray.getJSONObject(i)
                        .getString("doctors_lastname")
                    var doctorid = dataArray.getJSONObject(i).getString("doctors_id")
                    val msg = dataArray.getJSONObject(i).getString("contador")

                    totalMsgDoc.add(msg)
                    namedoctor.add(nombrecomplet)
                    docId.add(doctorid)
                }

                if (!docId.isEmpty()) {
                    val customAdapter =
                        CustomAdapterDoctorList(
                            this,
                            namedoctor,
                            docId,
                            totalMsgDoc
                        )
                    recyclerMyPacient.adapter = customAdapter
                }
            },
            Response.ErrorListener { error ->
                println(error)
            })
        quee.add(reqq)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = false
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
                var intent = Intent(this, AppoitmentCalendarDoctorActivity::class.java)
                startActivity(intent)
                //finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(application, MainPregnantActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}