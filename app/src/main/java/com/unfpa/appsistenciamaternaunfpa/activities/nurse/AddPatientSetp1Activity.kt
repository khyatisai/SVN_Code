package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd2Activity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_add_patient_setp1.*

class AddPatientSetp1Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_setp1)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        btnDeacuerdo.setOnClickListener {
            //var intent =  Intent(this, PacienteAddBrigadistActivity::class.java)
            var intent = Intent(this, AddPatientStep2Activity::class.java)
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}