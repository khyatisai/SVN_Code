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
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_paciente_detalle_cita.*


class PacienteDetalleCitaActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_paciente_detalle_cita)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Consulta última"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })


        tvFechaCita.setText(intent.getStringExtra("FechaAppointment"))
        tvHoraValor.setText(intent.getStringExtra("Hora"))
        tvTypeAppointment.setText(intent.getStringExtra("TypeAppointment"))


        if(intent.getStringExtra("weeksPac") == "null"){tvEdadGestacionalValor.setText("")}
        else{ tvEdadGestacionalValor.setText(intent.getStringExtra("weeksPac"))}

        if(intent.getStringExtra("diagnostics") == "null"){tvDiagnosticoValor.setText("")}
        else{ tvDiagnosticoValor.setText(intent.getStringExtra("diagnostics"))}

        if(intent.getStringExtra("plans") == "null"){tvPlanesValor.setText("")}
        else{ tvPlanesValor.setText(intent.getStringExtra("plans"))}

        if(intent.getStringExtra("reportOfFetalMovements") == "null"){tvRepMovimientoFetalValor.setText("")}
        else{ tvRepMovimientoFetalValor.setText(intent.getStringExtra("reportOfFetalMovements"))}

        if(intent.getStringExtra("arObro") == "null"){tvarObroValor.setText("")}
        else{ tvarObroValor.setText(intent.getStringExtra("arObro"))}

        if(intent.getStringExtra("otherRemarks") == "null"){tvOtrasObservacionesValor.setText("")}
        else{ tvOtrasObservacionesValor.setText(intent.getStringExtra("otherRemarks"))}

        if(intent.getStringExtra("mainReasonForTheConsultation") == "null"){tvMotivoConsultaValor.setText("")}
        else{ tvMotivoConsultaValor.setText(intent.getStringExtra("mainReasonForTheConsultation"))}



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