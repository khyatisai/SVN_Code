package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd3Activity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAddBrigadistActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.api_controller.ServiceVolley
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_add_patient_step4.*
import org.json.JSONObject
import java.text.SimpleDateFormat

class AddPatientStep4Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    val TAG = ServiceVolley::class.java.simpleName
    var isRegistered = false
    var registrar_paciente = EndPoints.URL_HEROKU + "/api/v1/nurse/patients/register"


    //var idBrigadista: String = ""

    val jsonobj = JSONObject()

    var password = ""

    private var formatterCedula: MaskedFormatter? = null
    private var formatterTelefono: MaskedFormatter? = null


    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_step4)
        setMaskTelefono("####-####")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                //finish()
                Backpresses()
            }
        })

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val fecha_nac = intent.getStringExtra("fecha_nac")
        val email = intent.getStringExtra("email")
        val telefono = intent.getStringExtra("telefono")
        val cedula = intent.getStringExtra("cedula")
        password = intent.getStringExtra("password")

        val semana_gestacion = intent.getStringExtra("semana_gestacion")
        val antecedente_patologio = intent.getStringExtra("antecedente_patologio")
        val tratamientos_recibidos = intent.getStringExtra("tratamientos_recibidos")
        val observaciones_medicas = intent.getStringExtra("observaciones_medicas")

        //idBrigadista = intent.getStringExtra("idBrigadista")

        etNombre.setText(nombre)
        etApellido.setText(apellido)
        etFechaNac.setText(fecha_nac)
        etCedula.setText(cedula)
        etEmail.setText(email)
        etTelefono.setText(telefono)
        etSemanaGestacion.setText(semana_gestacion)
        etAntecedentePatologio.setText(antecedente_patologio)
        etTratamientosRecibidos.setText(tratamientos_recibidos)
        etObservacionesMedicas.setText(observaciones_medicas)

        btnProximo3.setOnClickListener {

            if (ValidateForm()) {

                val cedulaReplace = cedula.replace("-", "")
                val telefonoReplace = etTelefono.text.toString().replace("-", "")

                val fecha = etFechaNac.text.toString()
                val sdf = SimpleDateFormat("yyyy-MM-dd")

                val FechaFormat = AppUtils.dateConverter(
                    "dd/MM/yyyy",
                    "yyyy/MM/dd",
                    fecha
                )
                val getMyUserId = AppUtils.getDataUser(this)

                jsonobj.put("nurseid", getMyUserId)
                jsonobj.put("firstname", etNombre.text.toString())
                jsonobj.put("lastname", etApellido.text.toString())
                jsonobj.put("email", etEmail.text.toString())
                jsonobj.put("cedula", cedulaReplace)
                jsonobj.put("password", password)
                jsonobj.put("birth", FechaFormat)
                jsonobj.put("phone", telefonoReplace)
                jsonobj.put("typeAuth", "email")
                jsonobj.put("typeUser", "paciente")
                jsonobj.put("gestationWeeks", etSemanaGestacion.text.toString())
                jsonobj.put("pathologicalAntecedents", etAntecedentePatologio.text.toString())
                jsonobj.put("treatmentsReceived", etTratamientosRecibidos.text.toString())
                jsonobj.put("medicalObservations", etObservacionesMedicas.text.toString())

                val que = Volley.newRequestQueue(this)
                val req = JsonObjectRequest(Request.Method.POST, registrar_paciente, jsonobj,
                    Response.Listener { response ->
                        if (response.getBoolean("success")) {

                            //Toast.makeText(this, "Registro agregado", Toast.LENGTH_SHORT).show()
                            isRegistered = true

                            val obj = response.getJSONObject("data")
                            val user = obj.getJSONObject("user")
                            val idPaciente = obj.getString("id")
                            val idPacienteUser = user.getString("id")
                            val birth = user.getString("birth")
                            val gestationWeeks = obj.getString("gestationWeeks")

                            //val desc = obj.getString("description")

//                                if(idBrigadista != "0"){
//                                    AddBrigadistToPac(idPacienteUser)
//                                }

                            //DESHABILTADO TEMPORALMENTE PORQUE NO VAN A ARRANCAR CON BRIGADISTA
                            var intent = Intent(this, PatientBrigadistActivity::class.java)
                            //var intent = Intent(this, PacienteAdd5Activity::class.java)
                            intent.putExtra("from", "fromRegister")
                            intent.putExtra("nombre", nombre)
                            intent.putExtra("apellido", apellido)
                            intent.putExtra("idPaciente", idPaciente)
                            intent.putExtra("idPacienteUser", idPacienteUser)
                            intent.putExtra("idBrigadista", "0")
                            intent.putExtra("birth", birth)
                            intent.putExtra("gestationWeeks", gestationWeeks)

                            finish()
                            startActivity(intent)
                            println(response)
                        } else {
                            Toast.makeText(this, "Usuario ya existe", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener { error ->
                        VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
                        //Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                    })
                que.add(req)


            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isRegistered) {
            finish()
        } else {
            Backpresses()
        }
    }

    private fun setMaskTelefono(mask: String) {
        formatterTelefono = MaskedFormatter(mask)
        formatterTelefono?.let {
            etTelefono.addTextChangedListener(MaskedWatcher(it, etTelefono))
        }
        val s = formatterTelefono?.formatString(etTelefono.text.toString())?.unMaskedString
    }

    private fun Backpresses() {
        finish()
        var intent = Intent(this, AddPatientStep3Activity::class.java)
        intent.putExtra("nombre", etNombre.text.toString())
        intent.putExtra("apellido", etApellido.text.toString())
        intent.putExtra("fecha_nac", etFechaNac.text.toString())
        intent.putExtra("email", etEmail.text.toString())
        intent.putExtra("telefono", etTelefono.text.toString())
        intent.putExtra("cedula", etCedula.text.toString())
        intent.putExtra("password", password)

        intent.putExtra("semana_gestacion", etSemanaGestacion.text.toString())
        intent.putExtra("antecedente_patologio", etAntecedentePatologio.text.toString())
        intent.putExtra("tratamientos_recibidos", etTratamientosRecibidos.text.toString())
        intent.putExtra("observaciones_medicas", etObservacionesMedicas.text.toString())
        startActivity(intent)


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
        if (isValidCedula(etCedula.text.toString())) {
            etCedula.setError("La Cedula es incorrecta")
            etCedula.requestFocus()
            return false
        }

        if (!isValidEmail(etEmail.text.toString())) {
            etEmail.setError("Correo inv??lido")
            etEmail.requestFocus()
            return false
        }
        if (!isValidPhone(etTelefono.text.toString())) {
            etTelefono.setError("Tel??fono inv??lido")
            etTelefono.requestFocus()
            return false
        }

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

    private fun isValidCedula(cedula: String): Boolean {
        //etCedula.addTextChangedListener(Mask.mask("###.###.###-##", etCedula))
        val meh = cedula.length
        if (cedula == "") {
            return true
        }
        return false
    }

    private fun isValidEmail(value: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    private fun isValidPhone(value: String): Boolean {
        if (value.length == 9) {
            return true
        }
        return false
    }

}