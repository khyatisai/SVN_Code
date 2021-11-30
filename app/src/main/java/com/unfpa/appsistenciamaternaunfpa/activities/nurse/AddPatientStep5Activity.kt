package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd6Activity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment.AppointmentCalenderNurseActivity
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_add_patient_step5.*

class AddPatientStep5Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_step5)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                onBackPressed()
            }
        })
        val nombre = intent.getStringExtra("nombre") + " " + intent.getStringExtra("apellido")
        val idPaciente = intent.getStringExtra("idPaciente")

        val birth = intent.getStringExtra("birth")
        val gestationWeeks = intent.getStringExtra("gestationWeeks")

        tvPacienteNombre.setText(nombre)

        btnReservar.setOnClickListener {
            /* var intent = Intent(this, AddPatientStep6Activity::class.java)
             intent.putExtra("nombre", nombre)
             intent.putExtra("idPaciente", idPaciente)
             intent.putExtra("birth", birth)
             intent.putExtra("gestationWeeks", gestationWeeks)
             startActivity(intent)*/
            /*val intent = Intent(this, AppointmentCalenderNurseActivity::class.java)
            startActivity(intent)*/
            var bootomsheetFragment = DialogFromCalendarFragment()
            val bundle = Bundle()
            bundle.putString("nombre", intent.getStringExtra("nombre"))
            bundle.putString("id", idPaciente)
            bootomsheetFragment.arguments = bundle
            bootomsheetFragment.show(supportFragmentManager, "tagdialogcalendar")
        }
        btnVolverr.setOnClickListener {
            var intent = Intent(this, MainNurseActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this, MainNurseActivity::class.java)
        finish()
        startActivity(intent)
    }
}