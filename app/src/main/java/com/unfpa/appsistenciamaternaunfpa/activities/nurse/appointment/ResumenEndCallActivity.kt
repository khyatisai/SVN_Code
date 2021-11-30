package com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.MainNurseActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.input_summary_sheet.*
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import android.widget.CompoundButton
import com.android.volley.VolleyLog
import com.unfpa.appsistenciamaternaunfpa.activities.MainDoctorActivity
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class ResumenEndCallActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    private var IdAppointment = ""
    private var nombre = ""

    private var birth = ""
    private var gestationWeeks = ""
    private var gestationWeeksDate = ""
    private var doctorName = ""
    var nombreCompleto = ""
    var horas = ""
    var fechaApp = ""
    var calStartTime = ""
    var calEndTime = ""
    var addAppointment = EndPoints.URL_HEROKU + "/api/v1/nurse/appointment/update"
    val jsonobj = JSONObject()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.input_summary_sheet)
            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            /** terminos y condiciones ***/
            supportActionBar!!.title = "Resumen de la consulta"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
            toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                override
                fun onClick(v: View) {
                    //finish()
                    val intent = Intent(applicationContext, MainNurseActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            })

            IdAppointment = intent.getStringExtra("IdAppointment")
            birth = intent.getStringExtra("birth")!!
            gestationWeeks = intent.getStringExtra("gestationWeeks")!!
            gestationWeeksDate = intent.getStringExtra("gestationWeeksDate")!!
            doctorName = intent.getStringExtra("doctor_name")
            var subStrgestationWeeksDate = gestationWeeksDate.substring(0, 10)

            nombreCompleto = intent.getStringExtra("patient_name")
            horas = intent.getStringExtra("hour")
            fechaApp = intent.getStringExtra("date")
            calStartTime = AppUtils.getCallTime(this.applicationContext, "Start")
            calEndTime = AppUtils.getCallTime(this.applicationContext, "End")

            tvDoctorName.text = doctorName
            val dateFormat = "yyyy-MM-dd"
            val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)
            tvPatientName.text = nombreCompleto
            tvNurseName.text = AppUtils.getDataCompleteNomUser(this)
            //converting 24 hour to 12 hour format
            val _24HourSDF = SimpleDateFormat("HH:mm:ss")
            val _12HourSDF = SimpleDateFormat("hh:mm a")
            val _24HrStartDt = _24HourSDF.parse(horas)
            val strStartHr = _12HourSDF.format(_24HrStartDt)
            tvAppointmentDate.text = fechaApp + " " + strStartHr
            tvClStartTime.text = calStartTime
            tvCallEndTime.text = calEndTime
            if (birth != "0000-00-00" && IdAppointment != "") {
                val dateString = birth
                val d = java.time.LocalDate.parse(dateString, dtf)
                val year = d.year
                val month = d.monthValue
                val day = d.dayOfMonth
                tvAge.setText(getAge(year, month, day))
            }
            etOther.isEnabled = false
            val chkOther: CheckBox = findViewById(R.id.chkOther)
            chkOther.setOnCheckedChangeListener { buttonView, isChecked ->
                etOther.isEnabled = isChecked
            }

            val gestationWeeksDateDTF = java.time.LocalDate.parse(subStrgestationWeeksDate, dtf)
            val yearGestation = gestationWeeksDateDTF.year
            val monthGestation = gestationWeeksDateDTF.monthValue
            val dayGestation = gestationWeeksDateDTF.dayOfMonth

            val weeksAndDay = weekendPregnant(yearGestation, monthGestation, dayGestation)
            //val gestationWeeksToInt = gestationWeeks.toInt() + weeks!!.toInt()

            val getweeksAndDayFun = weeksAndDay.toString().split(".")
            val getweeksFun = getweeksAndDayFun[0].toInt()
            val getDaysFun = getweeksAndDayFun[1].toInt()

            val getweeksAndDayDb = gestationWeeks.split(".")
            val getweeksDb = getweeksAndDayDb[0].toInt()
            val getDayDb = getweeksAndDayDb[1].toInt()

            val diasFunDbTotal = getDaysFun + getDayDb
            val semanasFunDbTotal = getweeksFun + getweeksDb

            var semanasPordias = 0
            var restanteDias = 0
            var semanasydias = "0.0"
            var semanasTotal = 0
            if (diasFunDbTotal > 0) {
                for (i in 0 until diasFunDbTotal) {
                    if (i == 6) {
                        semanasPordias++
                    }
                    if (i >= 6) {
                        restanteDias = diasFunDbTotal - 7
                    } else {
                        restanteDias = diasFunDbTotal
                    }
                    semanasTotal = semanasPordias + semanasFunDbTotal
                    semanasydias = "${semanasTotal.toString()}.$restanteDias"
                }
            } else {
                semanasTotal = semanasPordias + semanasFunDbTotal
                semanasydias = "${semanasTotal.toString()}.$restanteDias"
            }

            var semanasydiasFloat: Float = semanasydias.toFloat()

            //val gestationWeeksToInt = weeksPac.toFloat() + weeksAndDay!!.toFloat()
            val gestationWeeksToInt = semanasydiasFloat

            tvGestationWeek.setText(gestationWeeksToInt.toString())

            btnConfirmar.setOnClickListener {

                if (ValidateForm()) {
                    AddAppointmentPaciente()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun weekendPregnant(year: Int, month: Int, day: Int): Float {
        val date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val dateFormat = "yyyy-MM-dd"
        val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)
        val d = java.time.LocalDate.parse(date.toString(), dtf)
        val yearCurrent = d.year
        val monthCurrent = d.monthValue
        val dayCurrent = d.dayOfMonth

        var DatePatient = "$day/$month/$year"
        var DateCurrent = "$dayCurrent/$monthCurrent/$yearCurrent"

        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        var start1 = LocalDate.parse(DatePatient, formatter)
        var end1 = LocalDate.parse(DateCurrent, formatter)
        var daysTotals = ChronoUnit.DAYS.between(start1, end1)

        System.out.println("mehhhh:" + ChronoUnit.DAYS.between(start1, end1)); // 28

        var contador = 0
        var contadorVuelta7 = 0
        var numeroSemana = 0
        var numeroDeDia = daysTotals
        for (i in 0 until daysTotals) {
            if (contador == 7) {
                contadorVuelta7++
                numeroSemana++
                numeroDeDia = daysTotals - (7 * contadorVuelta7)
                println("$contadorVuelta7 Semanas y $numeroDeDia dias")
                contador = 0
            } else if (contador < 7 && contadorVuelta7 == 0) {
                numeroDeDia = daysTotals
                println("0 Semanas y $numeroDeDia")
            }

            contador++
        }

        var semanasYDiaTotales = "${contadorVuelta7.toString()}.$numeroDeDia"

        var SeamanasGestacion: Float = semanasYDiaTotales.toFloat()

        return SeamanasGestacion
    }

    fun AddAppointmentPaciente() {
        try {
            val getMyUserId = AppUtils.getDataUser(this)
            val jsonTopic = JSONObject()
            jsonTopic.put("contraception", chkContraception.isChecked.toString())
            jsonTopic.put("breastfeeding", chkBreastfeeding.isChecked.toString())
            jsonTopic.put("dangersignofpregnancy", chkDangerSigns.isChecked.toString())
            jsonTopic.put("other", chkOther.isChecked.toString())
            jsonTopic.put("othervalue", etOther.text.toString())

            //converting time
            val _24HourSDF = SimpleDateFormat("HH:mm")
            val _12HourSDF = SimpleDateFormat("hh:mm a")
            val _12HrStartDt = _12HourSDF.parse(calStartTime)
            val strStartHr = _24HourSDF.format(_12HrStartDt)

            val _12HrEndDt = _12HourSDF.parse(calEndTime)
            val strEndHr = _24HourSDF.format(_12HrEndDt)
            jsonobj.put("appointment", IdAppointment)
            /*jsonobj.put("callStart", calStartTime)
            jsonobj.put("callEnd", calEndTime)*/
            jsonobj.put("callStart", strStartHr)
            jsonobj.put("callEnd", strEndHr)
            jsonobj.put("nurseNote", etNotes.text)
            jsonobj.put("nurseTopic", jsonTopic.toString())

            val que = Volley.newRequestQueue(this)
            val req = JsonObjectRequest(
                Request.Method.POST, addAppointment, jsonobj,
                Response.Listener { response ->
                    //Toast.makeText(this, "Cita finalizada", Toast.LENGTH_SHORT).show()
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    applicationContext?.let { it1 ->
                        AppUtils.createCustomToast("Cita finalizada.", it1, layoutToast, "success")
                    }

                    // val obj = response.getJSONObject("data")

                    var intent = Intent(this, MainNurseActivity::class.java)
                    startActivity(intent)

                    println(response)
                }, Response.ErrorListener {
                    Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                })

            que.add(req)
        } catch (e: Exception) {
            e.printStackTrace()
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
//                val intent = Intent()
//                setResult(2, intent);
//                finish()
                val intent = Intent(this, MainNurseActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainNurseActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun ValidateForm(): Boolean {

        if (TextUtils.isEmpty(etNotes.text)) {
            etNotes.error = getString(R.string.EnterNotes)
            etNotes.requestFocus()
            return false
        }
        if (!chkContraception.isChecked && !chkBreastfeeding.isChecked && !chkDangerSigns.isChecked && !chkOther.isChecked) {
            Toast.makeText(this, getString(R.string.selectTopic), Toast.LENGTH_LONG).show()
            return false
        }
        if (chkOther.isChecked && TextUtils.isEmpty(etOther.text)) {
            Toast.makeText(this, getString(R.string.otherTopic), Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(etNotes.text)) {
            Toast.makeText(this, getString(R.string.AddNotes), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}