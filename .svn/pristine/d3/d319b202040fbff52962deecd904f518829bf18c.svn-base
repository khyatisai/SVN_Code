package com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.input_summary_sheet.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AppointmentSummaryActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    var topicsJson: JSONObject? = null

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
                finish()
            }
        })

        var appointmentJSONStr = intent.getStringExtra("appointmentJson")
        var selectedAppointmentPos = intent.getIntExtra("selectedAppointmentPos", 0)
        var appointmentJSON = JSONArray(appointmentJSONStr)
        val selectedAppointment = appointmentJSON.getJSONObject(0).getJSONArray("appointments")
            .getJSONObject(selectedAppointmentPos)
        val selectedUser = appointmentJSON.getJSONObject(0).getJSONObject("user")

        val firstName = selectedUser.getString("firstname")
        val lstName = selectedUser.getString("lastname")

        if (!selectedAppointment.isNull("doctorname"))
            tvDoctorName.text = selectedAppointment.getString("doctorname")

        if (!selectedAppointment.isNull("nurseTopic"))
            topicsJson = selectedAppointment.getJSONObject("nurseTopic")

        chkContraception.isEnabled = false
        chkBreastfeeding.isEnabled = false
        chkDangerSigns.isEnabled = false
        chkOther.isEnabled = false

        if (topicsJson != null) {
            if (topicsJson!!.getString("contraception").equals("true")) {
                chkContraception.isChecked = true
            }
            if (topicsJson!!.getString("breastfeeding").equals("true")) {
                chkBreastfeeding.isChecked = true
            }
            if (topicsJson!!.getString("dangersignofpregnancy").equals("true")) {
                chkDangerSigns.isChecked = true
            }
            if (topicsJson!!.getString("other").equals("true")) {
                chkOther.isChecked = true
                etOther.setText(topicsJson!!.getString("othervalue"))
            }
        }
        tvPatientName.setText(firstName + " " + lstName)

        if (selectedUser.getString("birth") != "0000-00-00") {
            val dateString = selectedUser.getString("birth")
            val dateFormat = "yyyy-MM-dd"
            val dtf = java.time.format.DateTimeFormatter.ofPattern(dateFormat)
            val d = java.time.LocalDate.parse(dateString, dtf)
            val year = d.year
            val month = d.monthValue
            val day = d.dayOfMonth
            tvAge.setText(getAge(year, month, day))
        }
        if (!selectedAppointment.getString("gestationWeeks")
                .isNullOrEmpty() && !selectedAppointment.getString("gestationWeeks").equals("null")
        ) {
            tvGestationWeek.text = selectedAppointment.getString("gestationWeeks")
        }
        if (!selectedAppointment.getString("date")
                .isNullOrEmpty() && !selectedAppointment.getString("date").equals("null")
        ) {
            tvAppointmentDate.text = selectedAppointment.getString("date")
        }
        tvNurseName.text = AppUtils.getDataCompleteNomUser(this)

        if (!selectedAppointment.getString("callStart").isNullOrEmpty() && !selectedAppointment.getString("callStart").equals("null")) {
            tvClStartTime.text = selectedAppointment.getString("callStart")
        }
        if (!selectedAppointment.getString("callEnd").isNullOrEmpty() && !selectedAppointment.getString("callEnd").equals("null")) {
            tvCallEndTime.text = selectedAppointment.getString("callEnd")
        }
        etNotes.isEnabled = false
        etOther.isEnabled = false
        btnConfirmar.visibility = View.GONE
        if (!selectedAppointment.getString("nurseNote").isNullOrEmpty() && !selectedAppointment.getString("nurseNote").equals("null")) {
            etNotes.setText(selectedAppointment.getString("nurseNote"))
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}