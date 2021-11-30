package com.unfpa.appsistenciamaternaunfpa.activities.my_health

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
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
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_register_paciente7.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class PacienteReprogramacion2Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    private var IdAppointment = ""
    private var nombre = ""
    private var DateAppointment = ""
    private var TimeAppointment = ""
    private var TvTimeAppointment = ""

    private var selectedType = ""


    var reprogramationAppointment = EndPoints.URL_HEROKU + "/api/v1/appointment/reprogramation"
    val jsonobj = JSONObject()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reprogramacion_paciente2)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** terminos y condiciones ***/
        supportActionBar!!.title = "Reprogramacion de cita"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        nombre = intent.getStringExtra("nombre")
        IdAppointment = intent.getStringExtra("IdAppointment")
        DateAppointment = intent.getStringExtra("DateAppointment")
        TimeAppointment = intent.getStringExtra("TimeAppointment")
        TvTimeAppointment = intent.getStringExtra("TvTimeAppointment")

        selectedType = intent.getStringExtra("selectedType")

        textNombreCompleto.setText(nombre)
        textDateAppointment.setText(DateAppointment)
        textTimeAppointment.setText(TvTimeAppointment)

        btnConfirmar.setOnClickListener {

            if(ValidateForm()){
                ReprogAppointmentPaciente()
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

    fun ReprogAppointmentPaciente(){
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val fecha = DateAppointment
        val myDate: Date = sdf.parse(fecha)
        val getMyUserId = AppUtils.getDataUser(this)

        jsonobj.put("appointment", IdAppointment)
        jsonobj.put("note",etNotas.text.toString())
        jsonobj.put("typeAppointment", selectedType)
        jsonobj.put("date", fecha)
        jsonobj.put("hour", TimeAppointment)

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(
            Request.Method.POST, reprogramationAppointment, jsonobj,
            Response.Listener {
                    response ->
                Toast.makeText(this, "Cita reprogramada", Toast.LENGTH_SHORT).show()

                //val obj = response.getJSONObject("data")

                var intent =  Intent(this, PacienteReprogramacion3Activity::class.java)
                intent.putExtra("nombre", nombre)
                intent.putExtra("fecha",fecha)
                intent.putExtra("time",TvTimeAppointment)
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
    }

    private fun ValidateForm(): Boolean {

        if (TextUtils.isEmpty(etNotas.text)) {
            etNotas.error = "Por favor ingrese un dato"
            etNotas.requestFocus()
            return false
        }

        return true
    }
}