package com.unfpa.appsistenciamaternaunfpa.activities.my_health

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.DialogAddBrigadistToPacFragment
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_register_paciente2.*
import kotlinx.android.synthetic.main.activity_register_paciente2.btnProximo1
import kotlinx.android.synthetic.main.activity_register_paciente_to_brigadist.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONException
import org.json.JSONObject


class PacienteAddBrigadistActivity : AppCompatActivity() {

    var add_brigadistToPac = EndPoints.URL_HEROKU + "/api/v1/patients/addbrigadist"

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    private var dpd: DatePickerDialog? = null

    private var idBrigadista = ""

    var fromTo = ""
    var nombre = ""
    var apellido = ""
    var idPaciente = ""
    var idPacienteUser = ""
    var  birth = ""
    var  gestationWeeks = ""

    var addPacToBrigadist = DialogAddBrigadistToPacFragment()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_paciente_to_brigadist)

        fromTo = intent.getStringExtra("from")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_white)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                //finish()
                //onBackPressed()
                if(fromTo == "fromToRegister"){
                    PacienteRegistroCita()
                }
                else{
                    onBackPressed()
                }
            }
        })


        if(fromTo == "fromRegister"){
            nombre = intent.getStringExtra("nombre")
            apellido = intent.getStringExtra("apellido")
            idPaciente = intent.getStringExtra("idPaciente")
            idPacienteUser = intent.getStringExtra("idPacienteUser")
            birth = intent.getStringExtra("birth")
            gestationWeeks = intent.getStringExtra("gestationWeeks")
        }

        idPacienteUser = intent.getStringExtra("idPacienteUser")
        idBrigadista = intent.getStringExtra("idBrigadista")
        val nombreBrigadist = intent.getStringExtra("nombreBrigadist")

        if(nombreBrigadist != null){
            nameBrigadista.text = "Brigadista: $nombreBrigadist"
        }

//        AddToBrigadist.setOnClickListener{
//            addPacToBrigadist.show(supportFragmentManager,"Bottom sheet dialog")
//        }
//        tvAddToBrigadist.setOnClickListener {
//            addPacToBrigadist.show(supportFragmentManager, "Bottom sheet dialog")
//        }

        btnProximo1.setOnClickListener {
            if(fromTo == "fromRegister"){
                PacienteRegistroCita()
            }else{
                if(idBrigadista == "0"){
                    val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                    applicationContext?.let { it1 ->
                        AppUtils.createCustomToast("Seleccione a un brigadista.", it1, layoutToast,"warning")
                    }
                }
                else{
                    PacienteListaActivity()
                }
            }
        }
    }

    private fun PacienteRegistroCita(){
        if(idBrigadista.isNullOrBlank() || idBrigadista == "0" ){
            var intent =  Intent(this, PacienteAdd5Activity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("apellido", apellido)
            intent.putExtra("idPaciente", idPaciente)
            intent.putExtra("birth", birth)
            intent.putExtra("gestationWeeks", gestationWeeks)
            startActivity(intent)
            finish()
        }
        else{
            AddBrigadistToPac(idPacienteUser)
        }
    }

    private fun PacienteListaActivity(){

        AddBrigadistToPac(idPacienteUser)
    }

    private fun AddBrigadistToPac(idPacienteUser: String) {

        val jsonobj = JSONObject()
        jsonobj.put("brigadistaid", idBrigadista)
        jsonobj.put("userid", idPacienteUser)

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(
            Request.Method.POST, add_brigadistToPac, jsonobj,
            Response.Listener { response ->

                try {
                    if(fromTo == "fromRegister"){
                        var intent =  Intent(this, PacienteAdd5Activity::class.java)
                        intent.putExtra("nombre", nombre)
                        intent.putExtra("apellido", apellido)
                        intent.putExtra("idPaciente", idPaciente)

                        intent.putExtra("birth", birth)
                        intent.putExtra("gestationWeeks", gestationWeeks)
                        startActivity(intent)
                        println(response)
                    }
                    else{
                        var intent =  Intent(this, PacienteActivity::class.java)
                        startActivity(intent)
                        val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                        this?.let { it1 ->
                            AppUtils.createCustomToast("Paciente asignado a brigadista", it1, layoutToast,"success")
                        }
                        println(response)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error -> error.printStackTrace() })
        que?.add(req)
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
        if(fromTo == "fromToRegister"){
            PacienteRegistroCita()
        }
        else{
            finish()
        }
    }

    private fun ValidateForm(): Boolean {

        if (TextUtils.isEmpty(etNombre.text)) {
            etNombre.error = "Campo obligatorio"
            etNombre.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etApellido.text)) {
            etApellido.error = "Campo obligatorio"
            etApellido.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etFechaNac.text)) {
            etFechaNac.error = "Campo obligatorio"
            etFechaNac.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etEmail.text)) {
            etEmail.error = "Campo obligatorio"
            etEmail.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etTelefono.text)) {
            etTelefono.error = "Campo obligatorio"
            etTelefono.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etCedula.text)) {
            etCedula.error = "Campo obligatorio"
            etCedula.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etPass.text)) {
            etPass.error = "Campo obligatorio"
            etPass.requestFocus()
            return false
        }

        return true
    }
}