package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd1Activity
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.CustomAdapterPregnantList
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.DialogAddPacienteToDoctorFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_patient_nurse.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PatientActivityNurse : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    var nombrePac: ArrayList<String> = ArrayList()
    var tvCitasPersona: ArrayList<String> = ArrayList()
    var tvCitasTelemedicina: ArrayList<String> = ArrayList()
    var IdUserPac: ArrayList<String> = ArrayList()

    var menuitemAddVisibled = false
    //var itemAdd: MenuItem? = 1

    //var getPregn antByDoctor = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/andtotalappointment"
    var getPregnantByDoctor = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/totalpatient"

    var DialogAddPacienteToDoctor = DialogAddPacienteToDoctorFragment()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_nurse)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Mis Pacientes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        getAllPacient()

        //seeting min and max dates for period tracker

        btnAgregarPaciente.setOnClickListener {
            var intent = Intent(this, AddPatientSetp1Activity::class.java)
            startActivity(intent)
        }
        btnAsignarmePaciente.setOnClickListener {
            DialogAddPacienteToDoctor.show(supportFragmentManager, "bottomSheetFragment1")
        }
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
            btnAgregarPaciente.isEnabled = false
            btnAsignarmePaciente.isEnabled = false

            //jsonobj2.put("nurseid", getMyUserId)

            val quee = Volley.newRequestQueue(this)
            val reqq = JsonObjectRequest(
                Request.Method.GET, getPregnantByDoctor, null,
                { response ->

                    var dataArray = response.getJSONArray("patient")
                    recyclerMyPacient.layoutManager =
                        LinearLayoutManager(this)

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)

                        var nombrecomplet =
                            dataArray.getJSONObject(i)
                                .getString("user_firstname") + " " + dataArray.getJSONObject(
                                i
                            ).getString("user_lastname")
                        nombrePac.add(nombrecomplet)
                        tvCitasPersona.add(dataArray.getJSONObject(i).getString("totalPresencial"))
                        tvCitasTelemedicina.add(dataArray.getJSONObject(i).getString("totalRemoto"))
                        IdUserPac.add(dataArray.getJSONObject(i).getString("user_id"))
                    }

                    val customAdapter = CustomAdapterPregnantList(
                        applicationContext,
                        nombrePac,
                        tvCitasPersona,
                        tvCitasTelemedicina,
                        IdUserPac
                    )
                    recyclerMyPacient.adapter = customAdapter
                    llProgressBar.visibility = View.GONE
                    btnAgregarPaciente.isEnabled = true
                    btnAsignarmePaciente.isEnabled = true

                },
                { error ->
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
                    btnAgregarPaciente.isEnabled = false
                    btnAsignarmePaciente.isEnabled = false

                    println(error)
                })
            quee.add(reqq)

        } catch (e: JSONException) {
            e.printStackTrace()
            llProgressBar.visibility = View.GONE
            btnAgregarPaciente.isEnabled = false
            btnAsignarmePaciente.isEnabled = false
        }
    }
}