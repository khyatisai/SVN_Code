package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd7Activity
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_add_patient_step6.*
import kotlinx.android.synthetic.main.activity_add_patient_step6.btnProximo5
import kotlinx.android.synthetic.main.activity_add_patient_step6.rbMeet
import kotlinx.android.synthetic.main.activity_add_patient_step6.rbTelMedicine
import kotlinx.android.synthetic.main.activity_appointment_calendar_for_pregnant.*
import kotlinx.android.synthetic.main.activity_register_paciente6.*
import kotlinx.android.synthetic.main.activity_register_paciente6.calendarView3
import kotlinx.android.synthetic.main.activity_register_paciente6.txtTime2
import java.util.*

class AddPatientStep6Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    var selectedTime: String = ""
    var TimeAppointment = ""
    var DateAppointment = ""
    var selectedTypeRb1: Int = 0
    var selectedTypeRb2: Int = 0

    var shape: Drawable? = null
    var shape2: Drawable? = null

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_step6)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "¿Para cuándo?"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        DateAppointment = DateFormat.format("yyyy/MM/dd", Calendar.getInstance()).toString()

        val nombre = intent.getStringExtra("nombre")
        val idPaciente = intent.getStringExtra("idPaciente")

        val calendar: Calendar = Calendar.getInstance()
        calendar[Calendar.DATE] = Calendar.getInstance().getActualMinimum(Calendar.DATE)
        val date = calendar.time.time

        calendarView3?.minDate = Calendar.getInstance().timeInMillis
        calendarView3?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            DateAppointment = ""+ year + "/" + (month + 1) + "/" + dayOfMonth
            Toast.makeText(this@AddPatientStep6Activity, DateAppointment, Toast.LENGTH_SHORT).show()
        }

        txtTime2.setOnClickListener {
            showTImePopup()
        }

//        shape = activity?.let { getDrawable(it,R.drawable.boton_redondo_relleno) }
        shape = getDrawable(R.drawable.boton_redondo_relleno)
        shape2 = getDrawable(R.drawable.boton_redondo)
        //shape2 = activity?.let { getDrawable(it,R.drawable.boton_redondo) }

        rbMeet.setHintTextColor(resources.getColor(R.color.nurse_green_color))
        rbMeet.setPadding(31, 0, 0, 0)
        rbMeet.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                rbMeet.background = shape
                rbMeet.setHintTextColor(Color.WHITE)
//                rbMeet.setPadding(31, 0, 0, 0)
                rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb1 = 1;
            }else {
                rbMeet.background = shape2
                rbMeet.setHintTextColor(resources.getColor(R.color.nurse_green_color))
//                rbMeet.setPadding(31, 0, 0, 0)
                rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb1 = 0;
            }
        }
        rbTelMedicine.setHintTextColor(resources.getColor(R.color.nurse_green_color))
        rbTelMedicine.setPadding(31, 0, 0, 0)
        rbTelMedicine.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                rbTelMedicine.background = shape
                rbTelMedicine.setHintTextColor(Color.WHITE)
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb2 = 2;
            }else {
                rbTelMedicine.background = shape2
                rbTelMedicine.setHintTextColor(resources.getColor(R.color.nurse_green_color))
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb2 = 0;
            }
        }

        btnProximo5.setOnClickListener {

            if(ValidateForm()){
                var intent =  Intent(this, AddPatientStep7Activity::class.java)
                intent.putExtra("nombre", nombre)
                intent.putExtra("idPaciente", idPaciente)
                intent.putExtra("DateAppointment", DateAppointment)
                intent.putExtra("TimeAppointment", TimeAppointment)
                intent.putExtra("TvTimeAppointment", txtTime2.text.toString())

                intent.putExtra("selectedType", if(selectedTypeRb1 == 1)  "1" else "2")

                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showTImePopup() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            try {
                //Toast.makeText(this, h.toString() + " : " + m + " : ", Toast.LENGTH_LONG).show()
                selectedTime = h.toString() + ":" + m
                txtTime2.text = AppUtils.getTime12HourFormat(selectedTime)
                //TimeAppointment = txtTime2.text.toString()
                TimeAppointment = String.format("%d:%d", h, m,":00")
                txtTime2.error = null

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }), hour, minute, false)

        tpd.show()
    }
    @SuppressLint("ResourceType")
    private fun ValidateForm(): Boolean {
        if (TextUtils.isEmpty(txtTime2.text)) {
            txtTime2.error = "Seleccione un horario"
            txtTime2.requestFocus()
            return false
        }
        else if(selectedTypeRb1 == 0 && selectedTypeRb2 == 0){
            rbMeet.setError("Seleccione el tipo de cita presencial o remoto")
            return false
        }

        return true
    }
}