package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd6Activity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAddBrigadistActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment.AppointmentCalenderNurseActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment.PacienteReprogramacionActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.CustomAdapterPregnantDetails
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.call
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_patient_detail.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class PatientDetailActivity : AppCompatActivity() {

    /***var for age****/
    private var date: Date? = null
    val cal = Calendar.getInstance()
    var sdf: java.text.SimpleDateFormat? = null

    var year = cal.get(Calendar.YEAR)
    var month = cal.get(Calendar.MONTH)
    var day = cal.get(Calendar.DAY_OF_MONTH)

    val today = cal.time

    /***end var for age****/

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    var hour = ""
    var dateStr = ""
    var tvCitaCancelada: ArrayList<String> = ArrayList()
    var tvFechaAppointment: ArrayList<String> = ArrayList()
    var tvHora: ArrayList<String> = ArrayList()
    var tvTypeAppointment: ArrayList<String> = ArrayList()
    var tvWeeksPac2: ArrayList<String> = ArrayList()
    var tvDiagnostics: ArrayList<String> = ArrayList()
    var tvPlans: ArrayList<String> = ArrayList()
    var tvreportOfFetalMovements: ArrayList<String> = ArrayList()
    var tvarObro: ArrayList<String> = ArrayList()

    var tvotherRemarks: ArrayList<String> = ArrayList()
    var tvmainReasonForTheConsultation: ArrayList<String> = ArrayList()


    var getPregnantDetail = EndPoints.URL_HEROKU + "/api/v1/patients/detail"
    var URLcancelAppointment = EndPoints.URL_HEROKU + "/api/v1/appointment/cancel"


    var idUserPac = ""
    var idPac = ""
    var namePac = ""
    var agePac = ""
    var weeksPac = ""
    var gestationWeeksDate = ""
    var pathologicalAntecedentsPac = ""
    var CallpacientUserId = ""
    var CallIdAppointment = ""
    var IdAppointmentForCallCancelRepro = ""
    var Callbirth = ""
    var CallgestationWeeks = ""
    var CallgestationWeeksDate = ""
    lateinit var dataArrayAppointment: JSONArray

    var mostrarBtn = 0


    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        var MyIdUserDoctor = AppUtils.getDataUser(this)

        idUserPac = intent.getStringExtra("idUserPac")

        /******Get detail pregnant******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        val getMyUserId = AppUtils.getDataUser(this)

        try {

            llProgressBar.visibility = View.VISIBLE
            btnReservar.isEnabled = false

            jsonobj2.put("userid", idUserPac)

            val quee = Volley.newRequestQueue(this)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getPregnantDetail, jsonobj2,
                { response ->

                    var dataArray = response.getJSONArray("patient")

                    recyclerMyPacient.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(this)


                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)
                        idPac = dataArray.getJSONObject(i).getString("id")
                        //namePac = dataArray.getJSONObject(i).getString("firstname") +" "+ dataArray.getJSONObject(i).getString("lastname")
                        namePac = dataArray.getJSONObject(i).getJSONObject("user")
                            .getString("firstname") + " " + dataArray.getJSONObject(i)
                            .getJSONObject("user").getString("lastname")

                        agePac = dataArray.getJSONObject(i).getJSONObject("user").getString("birth")
                        weeksPac = dataArray.getJSONObject(i).getString("gestationWeeks")
                        /***CALCULAR SEMANAS DE GESTACION**/
                        weeksPac = dataArray.getJSONObject(0).getString("gestationWeeks")
                        gestationWeeksDate =
                            dataArray.getJSONObject(0).getString("gestationWeeksDate")
                        val dateFormat = "yyyy-MM-dd"
                        val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)
                        var subStrgestationWeeksDate = gestationWeeksDate.substring(0, 10)
                        val gestationWeeksDateDTF =
                            java.time.LocalDate.parse(subStrgestationWeeksDate, dtf)
                        val yearGestation = gestationWeeksDateDTF.year
                        val monthGestation = gestationWeeksDateDTF.monthValue
                        val dayGestation = gestationWeeksDateDTF.dayOfMonth
                        val weeksAndDay =
                            weekendPregnant(yearGestation, monthGestation, dayGestation)
                        //val gestationWeeksToInt = gestationWeeks.toInt() + weeks!!.toInt()

                        val getweeksAndDayFun = weeksAndDay.toString().split(".")
                        val getweeksFun = getweeksAndDayFun[0].toInt()
                        val getDaysFun = getweeksAndDayFun[1].toInt()

                        val getweeksAndDayDb = weeksPac.split(".")
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
                        val semanasString = "$semanasTotal Semanas y $restanteDias días"

                        //val gestationWeeksToInt = weeksPac.toFloat() + weeksAndDay!!.toFloat()
                        val gestationWeeksToInt = semanasydiasFloat
                        tvSemanaValor.setText(semanasString)
                        /***FIN CALCULAR SEMANAS DE GESTACION**/

                        pathologicalAntecedentsPac =
                            dataArray.getJSONObject(i).getString("pathologicalAntecedents")

                        dataArrayAppointment =
                            dataArray.getJSONObject(i).getJSONArray("appointments")

                        CallpacientUserId =
                            dataArray.getJSONObject(i).getJSONObject("user").getString("id")
                        Callbirth =
                            dataArray.getJSONObject(i).getJSONObject("user").getString("birth")
                        CallgestationWeeks = weeksPac


                        for (i in 0 until dataArrayAppointment.length()) {
                            var FechaAppointment =
                                dataArrayAppointment.getJSONObject(i).getString("date")
                            var Hora = dataArrayAppointment.getJSONObject(i).getString("hour")

                            CallIdAppointment =
                                dataArrayAppointment.getJSONObject(i).getString("id")
                            CallgestationWeeksDate = dataArrayAppointment.getJSONObject(i)
                                .getString("gestationWeeksDate")

                            val WeeksPac2 =
                                dataArrayAppointment.getJSONObject(i).getString("gestationWeeks")
                            val Diagnostics =
                                dataArrayAppointment.getJSONObject(i).getString("diagnostics")
                            val Plans = dataArrayAppointment.getJSONObject(i).getString("plans")
                            val reportOfFetalMovements = dataArrayAppointment.getJSONObject(i)
                                .getString("reportOfFetalMovements")
                            val arObro = dataArrayAppointment.getJSONObject(i).getString("arObro")
                            val otherRemarks =
                                dataArrayAppointment.getJSONObject(i).getString("otherRemarks")
                            val mainReasonForTheConsultation = dataArrayAppointment.getJSONObject(i)
                                .getString("mainReasonForTheConsultation")

                            var TypeAppointment =
                                dataArrayAppointment.getJSONObject(i).getString("typeAppointment")

                            var CanceledAppointment =
                                dataArrayAppointment.getJSONObject(i).getString("cancel")


                            val c = Calendar.getInstance()
                            val sdtf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                            val getCurrentDateTime = sdtf.format(c.getTime())
                            // val getMyTime ="2021-01-20 03:26"
                            val getTimeAppointment = FechaAppointment + " " + Hora.substring(0, 5)
                            Log.d("getCurrentDateTime", getCurrentDateTime);
                            // getCurrentDateTime: 05/23/2016 18:49 PM

                            val localeSpanish = Locale("es", "ES")
                            val readerFormatter =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    SimpleDateFormat("yyyy-MM-dd", localeSpanish)
                                } else {
                                    TODO("VERSION.SDK_INT < N")
                                }
                            val writerFormatter =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    SimpleDateFormat("E d 'de' MMMM 'del' yyyy", localeSpanish)
                                } else {
                                    TODO("VERSION.SDK_INT < N")
                                }
                            val readDate: Date =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    readerFormatter.parse(FechaAppointment)
                                } else {
                                    TODO("VERSION.SDK_INT < N")
                                }
                            val dateInSpanish: String =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    writerFormatter.format(readDate)
                                } else {
                                    TODO("VERSION.SDK_INT < N")
                                }

                            val date =
                                DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()

                            val diff = getCurrentDateTime.compareTo(getTimeAppointment)


                            val IdUserDoctorApp =
                                dataArrayAppointment.getJSONObject(i).getString("doctor")
                            if (diff < 0 && FechaAppointment >= date && !CanceledAppointment.toBoolean()) {
                                IdAppointmentForCallCancelRepro = CallIdAppointment
                                lyCitaCall.visibility = View.VISIBLE
                                tvFechaCitaHoy.setText(dateInSpanish)
                                dateStr = dateInSpanish
                                hour = AppUtils.getTime12HourFormat(Hora)
                                tvHoraHoy.setText(AppUtils.getTime12HourFormat(Hora))
                                mostrarBtn = 1
                                btnReservar.visibility = View.VISIBLE
                                println("getCurrentDateTime menor que getTimeAppointment")
                            } else if (diff > 0 && mostrarBtn == 0) {
                                btnReservar.visibility = View.VISIBLE
                                println("getCurrentDateTime mayor que getTimeAppointment")
                            } else {
//                            if(FechaAppointment != ""){
//                                println("getCurrentDateTime igual que getTimeAppointment")
//                                if(TypeAppointment == "presencial"){tvTypeAppointment.add("Consulta Presencial")}
//                                else{tvTypeAppointment.add("Teleconsulta Médica")}
//
//                                tvFechaAppointment.add(dateInSpanish.capitalize())
//                                tvHora.add(AppUtils.getTime12HourFormat(Hora))
//                            }
                                Log.d("Return", "getMyTime older than getCurrentDateTime ");
                            }

                            if (diff > 0 || CanceledAppointment.toBoolean()) {
                                if (TypeAppointment == "presencial") {
                                    tvTypeAppointment.add("Consulta Presencial")
                                } else {
                                    tvTypeAppointment.add("Teleconsulta Médica")
                                }

                                tvFechaAppointment.add(dateInSpanish.capitalize())
                                tvHora.add(AppUtils.getTime12HourFormat(Hora))
                                if (CanceledAppointment.toBoolean()) {
                                    tvCitaCancelada.add("true")
                                } else {
                                    tvCitaCancelada.add("false")
                                }
                            }

                            tvWeeksPac2.add(WeeksPac2)
                            tvDiagnostics.add(Diagnostics)
                            tvPlans.add(Plans)
                            tvreportOfFetalMovements.add(reportOfFetalMovements)
                            tvarObro.add(arObro)
                            tvotherRemarks.add(otherRemarks)
                            tvmainReasonForTheConsultation.add(mainReasonForTheConsultation)

//                        if(CanceledAppointment.toBoolean()){
//                            tvCitaCancelada.add("true")
//                        }
//                        else{
//                            tvCitaCancelada.add("false")
//                        }

                        }

                    }
//                dateFormat = "yyyy-MM-dd"
//                dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)

                    if (agePac != "0000-00-00" && agePac != "") {
                        val dateString = agePac
                        var simpleFormat2 = DateTimeFormatter.ISO_DATE;
                        val meh = LocalDate.parse(dateString, simpleFormat2)
                        val myFormat = "yyyy-MM-dd"

                        sdf = java.text.SimpleDateFormat(myFormat, Locale.US)

                        year = meh.year
                        month = meh.monthValue
                        day = meh.dayOfMonth

                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month - 1)
                        cal.set(Calendar.DAY_OF_MONTH, day)

                        year = cal.get(Calendar.YEAR)
                        month = cal.get(Calendar.MONTH)
                        day = cal.get(Calendar.DAY_OF_MONTH)
                        date = cal.time
                        calculate_age()

                    }
                    supportActionBar!!.title = namePac.capitalize()
                    tvNamePaciente.setText(namePac.capitalize())
                    //tvSemanaValor.setText(weeksPac)
                    tvEnfermedadValor.setText(pathologicalAntecedentsPac.capitalize())

                    val customAdapter = CustomAdapterPregnantDetails(
                        applicationContext,
                        tvCitaCancelada,
                        tvFechaAppointment,
                        tvHora,
                        tvTypeAppointment,
                        tvWeeksPac2,
                        tvDiagnostics,
                        tvPlans,
                        tvreportOfFetalMovements,
                        tvarObro,
                        tvotherRemarks,
                        tvmainReasonForTheConsultation,
                        dataArray
                    )
                    recyclerMyPacient.adapter = customAdapter

                    llProgressBar.visibility = View.GONE
                    btnReservar.isEnabled = true
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
                    btnReservar.isEnabled = false
                    println(error)
                })
            quee.add(reqq)

        } catch (e: JSONException) {
            e.printStackTrace()
            llProgressBar.visibility = View.GONE
            btnReservar.isEnabled = false
        }

        btnReservar.setOnClickListener {
            /*var intent = Intent(this, AppointmentCalenderNurseActivity::class.java)
            *//*intent.putExtra("nombre", namePac.capitalize())
            intent.putExtra("idPaciente", idPac)*//*
            startActivity(intent)*/
            var bootomsheetFragment = DialogFromCalendarFragment()
            val bundle = Bundle()
            bundle.putString("nombre", namePac)
            bundle.putString("id", idPac)
            bootomsheetFragment.arguments = bundle
            bootomsheetFragment.show(supportFragmentManager, "tagdialogcalendar")
        }
        btnAsignarBrigadista.setOnClickListener {
            var intent = Intent(this, PacienteAddBrigadistActivity::class.java)
            intent.putExtra("from", "fromDetalle")
            intent.putExtra("idPacienteUser", CallpacientUserId)
            intent.putExtra("idBrigadista", "0")
            startActivity(intent)
        }
        callvideo.setOnClickListener {

            var getMyUserId = AppUtils.getDataUser(this)

            var intent = Intent(this, call::class.java)
            intent.putExtra("username", getMyUserId)
            intent.putExtra("calluser", CallpacientUserId)
            intent.putExtra("IdAppointment", IdAppointmentForCallCancelRepro)
            intent.putExtra("doctor", "")
            intent.putExtra("doctor_name", "")
            intent.putExtra("birth", Callbirth)
            intent.putExtra("gestationWeeks", CallgestationWeeks)
            intent.putExtra("gestationWeeksDate", CallgestationWeeksDate)
            intent.putExtra("uniqueId", getUniqueID())
            intent.putExtra("patient_name", namePac)
            intent.putExtra("hour", hour)
            intent.putExtra("date", dateStr)
            val dateFormat = SimpleDateFormat("hh:mm a")
            val formattedDate = dateFormat.format(Date()).toString()
            AppUtils.setCallTime(this!!, formattedDate, "Start")

            startActivity(intent)
        }

        Reprogramation.setOnClickListener {


            var intent = Intent(this, PacienteReprogramacionActivity::class.java)
            intent.putExtra("IdAppointment", IdAppointmentForCallCancelRepro)
            intent.putExtra("nombrePac", namePac.capitalize())
            startActivity(intent)
        }
        CancelartCita.setOnClickListener {
            //cancelAppointment()
            showPostDialog("Motivo de cancelación")
        }
    }

    fun showPostDialog(title: String) {
        val alert = AlertDialog.Builder(this)

        val edittext = EditText(this)
        edittext.hint = "Ingrese un motivo de cancelación"
        edittext.maxLines = 1

        var layout = this?.let { FrameLayout(it) }

        //set padding in parent layout
//        layout.isPaddingRelative(45,15,45,0)
        layout?.setPadding(45, 15, 45, 0)
        alert.setTitle(title)
        layout?.addView(edittext)
        alert.setView(layout)
        alert.setPositiveButton("Guardar", DialogInterface.OnClickListener { dialog, which ->
            run {
                val qName = edittext.text.toString()
                cancelAppointment(qName)
            }
        })
        alert.setNegativeButton("Cerrar", DialogInterface.OnClickListener { dialog, which ->
            run {
                dialog.dismiss()
            }
        })
        alert.show()
    }

    fun cancelAppointment(qName: String) {
        val jsonobj2 = JSONObject()

        jsonobj2.put("appointment", IdAppointmentForCallCancelRepro)
        jsonobj2.put("reason", qName)

        val quee2 = Volley.newRequestQueue(this)
        val reqq2 = JsonObjectRequest(
            Request.Method.POST, URLcancelAppointment, jsonobj2,
            { response ->

                val layoutToast =
                    layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                this?.let { it1 ->
                    AppUtils.createCustomToast("Cita cancelada", it1, layoutToast, "success")
                }
                finish();
                startActivity(intent);
            },
            { error ->
                println(error)
            })
        quee2.add(reqq2)
    }

    fun calculate_age() {
        val years = today.time - date!!.time
        val c = Calendar.getInstance();
        c.timeInMillis = years
        var month = c.get(Calendar.MONTH) - 1;
        if (month < 0)
            month = c.get(Calendar.MONTH);

        tvEdadValor.setText("" + (c.get(Calendar.YEAR) - 1970) + " Años")

//        tv_result.setText("Your age is: \n"
//                + (c.get(Calendar.YEAR) - 1970) + " years "
//                + (month) + " months "
//                + (c.get(Calendar.DAY_OF_MONTH)) + " days")
    }

    private fun getUniqueID(): String {
        return UUID.randomUUID().toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun weekendPregnant(year: Int, month: Int, day: Int): Float {
        val start = Calendar.getInstance()
        val mesGestacion = month - 1
        start[year, mesGestacion] = day

        val date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val dateFormat = "yyyy-MM-dd"
        val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)
        val d = java.time.LocalDate.parse(date.toString(), dtf)
        val yearCurrent = d.year
        val monthCurrent = d.monthValue
        val dayCurrent = d.dayOfMonth

        val end = Calendar.getInstance()
        end[year, month] = day

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
                println("$contadorVuelta7 Semanas y $numeroDeDia día")
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}