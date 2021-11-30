package com.unfpa.appsistenciamaternaunfpa.activities.nurse.chat

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
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainDoctorActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.MainNurseActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.CustomAdapterDoctorAndDoctorList
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.CustomAdapterPregnantList2
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_paciente.recyclerMyPacient
import kotlinx.android.synthetic.main.activity_paciente_list.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ListPacienteActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null


    var nombrePac: ArrayList<String> = ArrayList()
    var userId: ArrayList<String> = ArrayList()

    var nombreDoc: ArrayList<String> = ArrayList()
    var docId: ArrayList<String> = ArrayList()
    var totalMsg: ArrayList<String> = ArrayList()
    var totalMsgDoc: ArrayList<String> = ArrayList()

    private var requestQueue: RequestQueue? = null


    //var getContactList = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/patientlist"
    var getContactList = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/patientlistmessage"
    var getDoctors = EndPoints.URL_HEROKU + "/api/v1/nurse/user/getdoctors"
    /*
    var getContactList = EndPoints.URL_HEROKU + "/api/v1/patients/patientlist"
    var getDoctors = EndPoints.URL_HEROKU + "/api/v1/user/getdoctors"

    var getContactList = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/andtotalappointment"
    var getDoctors = EndPoints.URL_HEROKU + "/api/v1/nurse/getdoctors"*/

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_list_nurse)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Mensajes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                val intent = Intent(application, MainNurseActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })

        getAllPacient()

        getAllDoctor()

        //seeting min and max dates for period tracker

    }


    fun getAllPacient() {
        /******Get all Pregnant by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()
        val getMyUserId = AppUtils.getDataUser(this)

        try {

            llProgressBar.visibility = View.VISIBLE

            jsonobj2.put("nurseid", getMyUserId)

            val quee = Volley.newRequestQueue(this)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getContactList, jsonobj2,
                Response.Listener { response ->

                    var dataArray = response.getJSONArray("patient")
                    recyclerMyPacient.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(this)

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        val nombrecomplet = dataArray.getJSONObject(i)
                            .getString("firstname") + " " + dataArray.getJSONObject(i)
                            .getString("lastname")
                        val userid = dataArray.getJSONObject(i).getString("userId")
                        val msg = dataArray.getJSONObject(i).getString("contador")

                        nombrePac.add(nombrecomplet)
                        userId.add(userid)
                        totalMsg.add(msg)

                    }
                    if (!userId.isEmpty()) {
                        val customAdapter =
                            CustomAdapterPregnantList2(
                                this,
                                nombrePac,
                                userId,
                                totalMsg
                            )
                        recyclerMyPacient.adapter = customAdapter
                    }
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
        } catch (e: JSONException) {
            e.printStackTrace()
            llProgressBar.visibility = View.GONE
        }
    }


    fun getAllDoctor() {
        /******Get all Pregnant by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj = JSONObject()

        val getMyUserId = AppUtils.getDataUser(this)

        try {

            llProgressBar.visibility = View.VISIBLE

            jsonobj.put("nurseid", getMyUserId)


            val quee = Volley.newRequestQueue(this)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getDoctors, jsonobj,
                Response.Listener { response ->

                    var dataArray = response.getJSONArray("doctors")

                    recyclerDoctor.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(this)

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        var nombrecomplet = dataArray.getJSONObject(i)
                            .getString("firstname") + " " + dataArray.getJSONObject(i)
                            .getString("lastname")
                        var userid = dataArray.getJSONObject(i).getString("id")
                        val msg = dataArray.getJSONObject(i).getString("contador")

                        totalMsgDoc.add(msg)
                        nombreDoc.add(nombrecomplet)
                        docId.add(userid)
                    }
                    if (!docId.isEmpty()) {
                        val customAdapter =
                            CustomAdapterDoctorAndDoctorList(
                                this,
                                nombreDoc,
                                docId,
                                totalMsgDoc
                            )
                        recyclerDoctor.adapter = customAdapter
                    }
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
        } catch (e: JSONException) {
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
//            R.id.add -> {
//                var intent = Intent(this, AppoitmentCalendarDoctorActivity::class.java)
//                startActivity(intent)
//                //finish()
//            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(application, MainNurseActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}