package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.unfpa.appsistenciamaternaunfpa.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_register_paciente3.*


class PacienteAdd3Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    var nombre = ""
    var apellido = ""
    var fecha_nac = ""
    var email = ""
    var telefono = ""
    var cedula = ""
    var password = ""
    //var idBrigadista = ""

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_paciente3)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                //finish()
                Backpresses()
            }
        })

        nombre = intent.getStringExtra("nombre")
        apellido = intent.getStringExtra("apellido")
        fecha_nac = intent.getStringExtra("fecha_nac")
        email = intent.getStringExtra("email")
        telefono = intent.getStringExtra("telefono")
        cedula = intent.getStringExtra("cedula")
        password = intent.getStringExtra("password")


        etSemanaGestacion.setText(intent.getStringExtra("semana_gestacion"))
        etAntecedentePatologio.setText(intent.getStringExtra("antecedente_patologio"))
        etTratamientosRecibidos.setText(intent.getStringExtra("tratamientos_recibidos"))
        etObservacionesMedicas.setText(intent.getStringExtra("observaciones_medicas"))

        val infoSaludo = "Información de salud de $nombre $apellido";

        tvInformacionSalud.setText(infoSaludo)


        btnProximo2.setOnClickListener {

            if(ValidateForm()){
                var intent =  Intent(this, PacienteAdd4Activity::class.java)
                /**campos de activity 2**/
                intent.putExtra("nombre", nombre)
                intent.putExtra("apellido", apellido)
                intent.putExtra("fecha_nac", fecha_nac)
                intent.putExtra("email", email)
                intent.putExtra("telefono", telefono)
                intent.putExtra("cedula", cedula)
                intent.putExtra("password", password)

                intent.putExtra("semana_gestacion", etSemanaGestacion.text.toString())
                intent.putExtra("antecedente_patologio", etAntecedentePatologio.text.toString())
                intent.putExtra("tratamientos_recibidos", etTratamientosRecibidos.text.toString())
                intent.putExtra("observaciones_medicas", etObservacionesMedicas.text.toString())

                //intent.putExtra("idBrigadista", idBrigadista)

                startActivity(intent)
            }
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
        Backpresses()
    }
    private fun Backpresses(){
        var intent =  Intent(this, PacienteAdd2Activity::class.java)
        intent.putExtra("nombre", nombre)
        intent.putExtra("apellido", apellido)
        intent.putExtra("fecha_nac", fecha_nac)
        intent.putExtra("email", email)
        intent.putExtra("telefono", telefono)
        intent.putExtra("cedula", cedula)
        intent.putExtra("password", password)

        intent.putExtra("semana_gestacion", etSemanaGestacion.text.toString())
        intent.putExtra("antecedente_patologio", etAntecedentePatologio.text.toString())
        intent.putExtra("tratamientos_recibidos", etTratamientosRecibidos.text.toString())
        intent.putExtra("observaciones_medicas", etObservacionesMedicas.text.toString())
        startActivity(intent)
        finish()

    }

    private fun ValidateForm(): Boolean {

        if (TextUtils.isEmpty(etSemanaGestacion.text)) {
            etSemanaGestacion.error = "Campo obligatorio"
            etSemanaGestacion.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etAntecedentePatologio.text)) {
            etAntecedentePatologio.error = "Campo obligatorio"
            etAntecedentePatologio.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etTratamientosRecibidos.text)) {
            etTratamientosRecibidos.error = "Campo obligatorio"
            etTratamientosRecibidos.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etObservacionesMedicas.text)) {
            etObservacionesMedicas.error = "Campo obligatorio"
            etObservacionesMedicas.requestFocus()
            return false
        }
        return true
    }
}