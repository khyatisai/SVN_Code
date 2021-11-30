package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_paciente_detalle_cita.*
import kotlinx.android.synthetic.main.activity_paciente_mas_informacion.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import java.util.*


class PacienteMasInformacionActivity : AppCompatActivity() {


    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1


        override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_mas_informacion)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Consulta de hoy"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        val nameDoctor = intent.getStringExtra("nameDoctor")
        val horas = intent.getStringExtra("horas")
        val dateInSpanishForIntent = intent.getStringExtra("dateInSpanishForIntent")
        val pathologicalAntecedents = intent.getStringExtra("pathologicalAntecedents")
        val treatmentsReceived = intent.getStringExtra("treatmentsReceived")
        val medicalObservations = intent.getStringExtra("medicalObservations")
        //val note = intent.getStringExtra("note")

        if(intent.getStringExtra("note") == "null"){tvnoteValor.setText("")}
        else{ tvnoteValor.setText(intent.getStringExtra("note"))}


        tvHoraHoy.text = AppUtils.getTime12HourFormat(horas)
        tvFechaCitaHoy.text = dateInSpanishForIntent
        tvDoctorValor.text = nameDoctor
        tvpathologicalAntecedentsValor.text = pathologicalAntecedents
        tvtreatmentsReceivedValor.text = treatmentsReceived
        tvmedicalObservationsValor.text = medicalObservations
        //tvnoteValor.text = note

        callvideoPac.setOnClickListener {

            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
            this?.let { it1 ->
                AppUtils.createCustomToast("Recibirá la llamada en el horario programado", it1, layoutToast,"success")
            }
        }

    }


    private fun getUniqueID(): String{
        return UUID.randomUUID().toString()
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
            R.id.add -> {
                var intent = Intent(this, PacienteAdd1Activity::class.java)
                startActivity(intent)
                //finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}