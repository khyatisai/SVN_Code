package com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainDoctorActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.MainNurseActivity
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import io.github.inflationx.viewpump.*
import kotlinx.android.synthetic.main.activity_reprogramacion_paciente3_nurse.*
import java.util.*


class PacienteReprogramacion3Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    private var fechaAppointment = ""
    private var timeAppointment = ""

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reprogramacion_paciente3_nurse)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "Reprogramacion de cita"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                onBackPressed()
            }
        })

        val localeSpanish = Locale("es", "ES")
        val nombre = intent.getStringExtra("nombre")
        fechaAppointment = intent.getStringExtra("fecha")
        timeAppointment = intent.getStringExtra("time")

        val readerFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyy/MM/dd", localeSpanish)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val writerFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("d 'de' MMMM 'del' yyyy", localeSpanish)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val readDate: Date = readerFormatter.parse(fechaAppointment)
        val dateInSpanish: String = writerFormatter.format(readDate)
        println(dateInSpanish) // prints: 29 de enero del 2017 (as today :p)

        tvTimeAppointment.setText(timeAppointment)
        tvDateAppointment.setText(dateInSpanish)
        tvPacienteNombre.setText(nombre)

        btnReturnMain.setOnClickListener {
            onBackPressed()
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
                val intent = Intent()
                setResult(2, intent);
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        var intent = Intent(this, MainNurseActivity::class.java)
        startActivity(intent)
    }
}