package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.DialogAddBrigadistToPacFragment
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_patient_brigadist.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONException
import org.json.JSONObject

class PatientBrigadistActivity : AppCompatActivity() {
    var add_brigadistToPac = EndPoints.URL_HEROKU + "/api/v1/patients/addbrigadist"

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    private var dpd: DatePickerDialog? = null

    private var idBrigadista = ""

    var fromTo = ""
    var nombre = ""
    var apellido = ""
    var idPaciente = ""
    var idPacienteUser = ""
    var birth = ""
    var gestationWeeks = ""

    var addPacToBrigadist = DialogAddBrigadistToPacFragment()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_brigadist)
        fromTo = intent.getStringExtra("from")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                //finish()
                //onBackPressed()
                if (fromTo == "fromRegister") {
                    PacienteRegistroCita()
                } else {
                    onBackPressed()
                }
            }
        })


        if (fromTo == "fromRegister") {
            nombre = intent.getStringExtra("nombre")
            apellido = intent.getStringExtra("apellido")
            idPaciente = intent.getStringExtra("idPaciente")
            idPacienteUser = intent.getStringExtra("idPacienteUser")
            birth = intent.getStringExtra("birth")
            gestationWeeks = intent.getStringExtra("gestationWeeks")
        }

        idPacienteUser = intent.getStringExtra("idPacienteUser")
        idBrigadista = intent.getStringExtra("idBrigadista")
        val nombreBrigadist = intent.getStringExtra("nombreBrigadist")

        if (nombreBrigadist != null) {
            nameBrigadista.text = "Brigadista: $nombreBrigadist"
        }

//        AddToBrigadist.setOnClickListener{
//            addPacToBrigadist.show(supportFragmentManager,"Bottom sheet dialog")
//        }
//        tvAddToBrigadist.setOnClickListener {
//            addPacToBrigadist.show(supportFragmentManager, "Bottom sheet dialog")
//        }

        btnProximo1.setOnClickListener {
            if (fromTo == "fromRegister") {
                PacienteRegistroCita()
            } else {
                if (idBrigadista == "0") {
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    applicationContext?.let { it1 ->
                        AppUtils.createCustomToast(
                            "Seleccione a un brigadista.",
                            it1,
                            layoutToast,
                            "warning"
                        )
                    }
                } else {
                    PacienteListaActivity()
                }
            }
        }
    }

    private fun PacienteRegistroCita() {
        if (idBrigadista.isNullOrBlank() || idBrigadista == "0") {
            var intent = Intent(this, AddPatientStep5Activity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("apellido", apellido)
            intent.putExtra("idPaciente", idPaciente)
            intent.putExtra("birth", birth)
            intent.putExtra("gestationWeeks", gestationWeeks)
            finish()
            startActivity(intent)

        } else {
            AddBrigadistToPac(idPacienteUser)
        }
    }

    private fun PacienteListaActivity() {

        AddBrigadistToPac(idPacienteUser)
    }

    private fun AddBrigadistToPac(idPacienteUser: String) {

        val jsonobj = JSONObject()
        jsonobj.put("brigadistaid", idBrigadista)
        jsonobj.put("userid", idPacienteUser)

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(
            Request.Method.POST, add_brigadistToPac, jsonobj,
            Response.Listener { response ->

                try {
                    if (fromTo == "fromRegister") {
                        var intent = Intent(this, AddPatientStep5Activity::class.java)
                        intent.putExtra("nombre", nombre)
                        intent.putExtra("apellido", apellido)
                        intent.putExtra("idPaciente", idPaciente)

                        intent.putExtra("birth", birth)
                        intent.putExtra("gestationWeeks", gestationWeeks)
                        startActivity(intent)
                        println(response)
                    } else {
                        var intent = Intent(this, PatientActivityNurse::class.java)
                        startActivity(intent)
                        val layoutToast = layoutInflater.inflate(
                            R.layout.custom_toast_layout,
                            custom_toast_container
                        )
                        this?.let { it1 ->
                            AppUtils.createCustomToast(
                                "Paciente asignado a brigadista",
                                it1,
                                layoutToast,
                                "success"
                            )
                        }
                        println(response)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error -> error.printStackTrace() })
        que?.add(req)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fromTo == "fromRegister") {
            PacienteRegistroCita()
        } else {
            finish()
        }
    }
}