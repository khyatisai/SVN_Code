package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAdd3Activity
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_add_patient_step2.*
import java.util.*

class AddPatientStep2Activity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1
    private var dpd: DatePickerDialog? = null

    private var formatterCedula: MaskedFormatter? = null
    private var formatterTelefono: MaskedFormatter? = null

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_step2)

        setMaskTelefono("####-####")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro de Paciente Nueva"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val fecha_nac = intent.getStringExtra("fecha_nac")
        val email = intent.getStringExtra("email")
        val telefono = intent.getStringExtra("telefono")
        val cedula = intent.getStringExtra("cedula")
        val password = intent.getStringExtra("password")

        val semana_gestacion = intent.getStringExtra("semana_gestacion")
        val antecedente_patologio = intent.getStringExtra("antecedente_patologio")
        val tratamientos_recibidos = intent.getStringExtra("tratamientos_recibidos")
        val observaciones_medicas = intent.getStringExtra("observaciones_medicas")

        etNombre.setText(nombre)
        etApellido.setText(apellido)
        etFechaNac.setText(fecha_nac)
        etEmail.setText(email)
        etTelefono.setText(telefono)
        etCedula.setText(cedula)
        etPass.setText(password)

        //val idBrigadista = intent.getStringExtra("idBrigadista")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dpd = DatePickerDialog(
            this,
            R.style.TimePickerTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                // Display Selected date in textbox
                var dateBirth = "" + dayOfMonth + "/" + (month + 1) + "/" + year
                etFechaNac.setText(dateBirth)
            },
            year,
            month,
            day
        )

        etFechaNac.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                dpd!!.show()
                return@OnTouchListener true
            }
            false
        })

//        etFechaNac.setOnClickListener {
//            dpd!!.show()
//        }

        btnProximo1.setOnClickListener {

            if (ValidateForm()) {

                var intent = Intent(this, AddPatientStep3Activity::class.java)
                intent.putExtra("nombre", etNombre.text.toString())
                intent.putExtra("apellido", etApellido.text.toString())
                intent.putExtra("fecha_nac", etFechaNac.text.toString())
                intent.putExtra("email", etEmail.text.toString())
                intent.putExtra("telefono", etTelefono.text.toString())
                intent.putExtra("cedula", etCedula.text.toString())
                intent.putExtra("password", etPass.text.toString())

                intent.putExtra("semana_gestacion", semana_gestacion)
                intent.putExtra("antecedente_patologio", antecedente_patologio)
                intent.putExtra("tratamientos_recibidos", tratamientos_recibidos)
                intent.putExtra("observaciones_medicas", observaciones_medicas)
                finish()
                startActivity(intent)


            }
        }
    }

    private fun setMaskTelefono(mask: String) {
        formatterTelefono = MaskedFormatter(mask)
        formatterTelefono?.let {
            etTelefono.addTextChangedListener(MaskedWatcher(it, etTelefono))
        }
        val s = formatterTelefono?.formatString(etTelefono.text.toString())?.unMaskedString
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
        if (!isValidPassword(etPass.text.toString())) {
            etPass.setError("La Contrase??a debe de tener al menos 4 caracteres")
            etPass.requestFocus()
            return false
        }

        if (isValidCedula(etCedula.text.toString())) {
            etCedula.setError("La Cedula no puede estar vacia")
            etCedula.requestFocus()
            return false
        }
        if (!isValidPassword(etPass.text.toString())) {
            etPass.setError("La Contrase??a debe de tener al menos 4 caracteres")
            etPass.requestFocus()
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

    private fun isValidPassword(value: String): Boolean {
        if (value.length >= 4) {
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}