package com.unfpa.appsistenciamaternaunfpa.activities.my_health

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainDoctorActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_resumen_endcall.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class ResumenEndCallActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    private var IdAppointment = ""
    private var nombre = ""

    private var birth = ""
    private var gestationWeeks = ""
    private var gestationWeeksDate = ""


    var addAppointment = EndPoints.URL_HEROKU + "/api/v1/appointment/update"
    val jsonobj = JSONObject()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen_endcall)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "Resumen de la consulta"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                //finish()
                val intent = Intent(applicationContext, MainDoctorActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })

        IdAppointment = intent.getStringExtra("IdAppointment")
        birth = intent.getStringExtra("birth")!!
        gestationWeeks = intent.getStringExtra("gestationWeeks")!!
        gestationWeeksDate = intent.getStringExtra("gestationWeeksDate")!!
        var subStrgestationWeeksDate = gestationWeeksDate.substring(0,10)

        val dateFormat = "yyyy-MM-dd"
        val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)

        if(birth != "0000-00-00" && IdAppointment != ""){
            val dateString = birth
            val d = java.time.LocalDate.parse(dateString, dtf)
            val year = d.year
            val month = d.monthValue
            val day = d.dayOfMonth
            etEddad.setText( getAge(year, month , day))
        }
        val gestationWeeksDateDTF = java.time.LocalDate.parse(subStrgestationWeeksDate, dtf)
        val yearGestation = gestationWeeksDateDTF.year
        val monthGestation = gestationWeeksDateDTF.monthValue
        val dayGestation = gestationWeeksDateDTF.dayOfMonth

        val weeksAndDay = weekendPregnant(yearGestation,monthGestation,dayGestation)
        //val gestationWeeksToInt = gestationWeeks.toInt() + weeks!!.toInt()

        val getweeksAndDayFun = weeksAndDay.toString().split(".")
        val getweeksFun = getweeksAndDayFun[0].toInt()
        val getDaysFun = getweeksAndDayFun[1].toInt()

        val getweeksAndDayDb  = gestationWeeks.split(".")
        val getweeksDb = getweeksAndDayDb[0].toInt()
        val getDayDb = getweeksAndDayDb[1].toInt()

        val diasFunDbTotal = getDaysFun + getDayDb
        val semanasFunDbTotal = getweeksFun + getweeksDb

        var semanasPordias = 0
        var restanteDias = 0
        var semanasydias = "0.0"
        var semanasTotal = 0
        if(diasFunDbTotal > 0){
            for(i in 0 until diasFunDbTotal){
                if(i == 6){
                    semanasPordias++
                }
                if(i>=6){
                    restanteDias = diasFunDbTotal-7
                }
                else{
                    restanteDias  = diasFunDbTotal
                }
                semanasTotal = semanasPordias + semanasFunDbTotal
                semanasydias = "${semanasTotal.toString()}.$restanteDias"
            }
        }
        else{
            semanasTotal = semanasPordias + semanasFunDbTotal
            semanasydias = "${semanasTotal.toString()}.$restanteDias"
        }

        var semanasydiasFloat: Float = semanasydias.toFloat()

        //val gestationWeeksToInt = weeksPac.toFloat() + weeksAndDay!!.toFloat()
        val gestationWeeksToInt = semanasydiasFloat

        etSemanasGestacion.setText(gestationWeeksToInt.toString())

        btnConfirmar.setOnClickListener {

            if(ValidateForm()){
                AddAppointmentPaciente()
            }
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
        var start1 = LocalDate.parse(DatePatient,formatter)
        var end1 = LocalDate.parse(DateCurrent,formatter)
        var daysTotals = ChronoUnit.DAYS.between(start1, end1)

        System.out.println("mehhhh:"+ ChronoUnit.DAYS.between(start1, end1)); // 28

        var contador = 0
        var contadorVuelta7 = 0
        var numeroSemana = 0
        var numeroDeDia = daysTotals
        for(i in 0 until daysTotals){
            if(contador == 7){
                contadorVuelta7++
                numeroSemana++
                numeroDeDia = daysTotals - (7 * contadorVuelta7)
                println("$contadorVuelta7 Semanas y $numeroDeDia dias")
                contador = 0
            }
            else if(contador < 7 && contadorVuelta7 == 0){
                numeroDeDia = daysTotals
                println("0 Semanas y $numeroDeDia")
            }

            contador++
        }

        var semanasYDiaTotales = "${contadorVuelta7.toString()}.$numeroDeDia"

        var SeamanasGestacion: Float = semanasYDiaTotales.toFloat()

        return  SeamanasGestacion
    }

    fun AddAppointmentPaciente(){
        val getMyUserId = AppUtils.getDataUser(this)

        jsonobj.put("appointment", IdAppointment)

        jsonobj.put("gestationWeeks",etSemanasGestacion.text.toString())
        jsonobj.put("reportOfFetalMovements",etReporteMovimientosFetales.text.toString())
        jsonobj.put("arObro",etAroBro.text.toString())
        jsonobj.put("mainReasonForTheConsultation",etMotivoPrincipalConsulta.text.toString())
        jsonobj.put("diagnostics",etDiagnostico.text.toString())
        jsonobj.put("plans",etPlanes.text.toString())
        jsonobj.put("otherRemarks",etOtrasObservaciones.text.toString())


        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(
            Request.Method.POST, addAppointment, jsonobj,
            Response.Listener {
                    response ->
                //Toast.makeText(this, "Cita finalizada", Toast.LENGTH_SHORT).show()
                val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                applicationContext?.let { it1 ->
                    AppUtils.createCustomToast("Cita finalizada.", it1, layoutToast,"success")
                }

                val obj = response.getJSONObject("data")

                var intent =  Intent(this, MainDoctorActivity::class.java)
                startActivity(intent)

                println(response)
            }, Response.ErrorListener {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            })
        que.add(req)
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
                val intent = Intent(this, MainDoctorActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainDoctorActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun ValidateForm(): Boolean {

//        if (TextUtils.isEmpty(etReporteMovimientosFetales.text)) {
//            etReporteMovimientosFetales.error = "Por favor ingrese un dato"
//            etReporteMovimientosFetales.requestFocus()
//            return false
//        }
//        if (TextUtils.isEmpty(etAroBro.text)) {
//            etAroBro.error = "Por favor ingrese un dato"
//            etAroBro.requestFocus()
//            return false
//        }
//        if (TextUtils.isEmpty(etMotivoPrincipalConsulta.text)) {
//            etMotivoPrincipalConsulta.error = "Por favor ingrese un dato"
//            etMotivoPrincipalConsulta.requestFocus()
//            return false
//        }
//        if (TextUtils.isEmpty(etDiagnostico.text)) {
//            etDiagnostico.error = "Por favor ingrese un dato"
//            etDiagnostico.requestFocus()
//            return false
//        }
//        if (TextUtils.isEmpty(etPlanes.text)) {
//            etPlanes.error = "Por favor ingrese un dato"
//            etPlanes.requestFocus()
//            return false
//        }
        if (TextUtils.isEmpty(etOtrasObservaciones.text)) {
            etOtrasObservaciones.error = "Por favor ingrese un dato"
            etOtrasObservaciones.requestFocus()
            return false
        }

        return true
    }
}