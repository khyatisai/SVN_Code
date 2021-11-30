package com.unfpa.appsistenciamaternaunfpa.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_register_brigadist2.*
import kotlinx.android.synthetic.main.activity_register_brigadist2.etApellido
import kotlinx.android.synthetic.main.activity_register_brigadist2.etCedula
import kotlinx.android.synthetic.main.activity_register_brigadist2.etContrasena
import kotlinx.android.synthetic.main.activity_register_brigadist2.etCorreo
import kotlinx.android.synthetic.main.activity_register_brigadist2.etNombre
import kotlinx.android.synthetic.main.activity_register_brigadist2.etTelefono
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

class register_brigadista : AppCompatActivity() {
    val jsonobj = JSONObject()
    var etfirtsName = ""
    var etLastName = ""
    var etEmail = ""
    var etPassword = ""
    var etPhone = ""
    var etID = ""
    var REGISTER = EndPoints.URL_HEROKU + "/api/v1/auth/register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_brigadist2)

        btnRegistrarBrigadista.setOnClickListener {
            etfirtsName = etNombre.text.toString().trim()
            etLastName = etApellido.text.toString().trim()
            etEmail = etCorreo.text.toString().trim()
            etPassword = etContrasena.text.toString().trim()
            etPhone = etTelefono.text.toString().trim()
            etID = etCedula.text.toString().trim()

            if (areFieldsValid(etfirtsName, etLastName, etEmail, etPassword, etPhone, etID)) {

                try {

                    progressBar.visibility = View.VISIBLE
                    btnRegistrarBrigadista.isEnabled = false

                    jsonobj.put("firstname", etfirtsName)
                    jsonobj.put("lastname", etLastName)
                    jsonobj.put("email", etEmail)
                    jsonobj.put("cedula", etID)
                    jsonobj.put("password", etPassword)
                    jsonobj.put("phone", etPhone)
                    jsonobj.put("typeAuth", "email")
                    jsonobj.put("typeUser", "brigadista")

                    val que = Volley.newRequestQueue(this)
                    val req = JsonObjectRequest(Request.Method.POST, REGISTER, jsonobj,
                        Response.Listener { response ->

                            //Toast.makeText(this, "Brigadista Registrado", Toast.LENGTH_SHORT).show()
                            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                            applicationContext?.let { it1 ->
                                AppUtils.createCustomToast("Brigadista Registrado", it1, layoutToast,"success")
                            }
                            var intent = Intent(this, LoginBrigadistaActivity::class.java)
                            startActivity(intent)
                            println(response)
                        }, Response.ErrorListener { error ->
                            registerBError.setText(getVolleyError(error))
                            progressBar.visibility = View.GONE
                            btnRegistrarBrigadista.isEnabled = true
                        })
                    que.add(req)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressBar.visibility = View.GONE
                    btnRegistrarBrigadista.isEnabled = true
                }
            }

        }

        tvIniciaSesion3.setOnClickListener{
            var intent = Intent(this, TypeAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun areFieldsValid(
        firtsname: String,
        lastname: String,
        email: String,
        password: String,
        telefono: String,
        cedula: String
    ): Boolean {
        if (firtsname.isEmpty()) {
            etNombre.setError("Campo Obligatorio")
            etNombre.requestFocus()
            return false;
        }
        if (lastname.isEmpty()) {
            etApellido.setError("Campo Obligatorio")
            etApellido.requestFocus()
            return false
        }
        if (cedula.isEmpty()) {
            etCedula.setError("Campo Obligatorio")
            etCedula.requestFocus()
            return false
        }
        if (!isValidEmail(email)) {
            etCorreo.setError("Correo inválido")
            etCorreo.requestFocus()
            return false
        }
        if (!isValidPhone(telefono)) {
            etTelefono.setError("Teléfono inválido")
            etTelefono.requestFocus()
            return false
        }

        if (!isValidPassword(password)) {
            etContrasena.setError("La Contraseña debe de tener al menos 4 caracteres")
            etContrasena.requestFocus()
            return false
        }


        return true
    }

    private fun isValidPhone(value: String): Boolean {
        if (value.length >= 8) {
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

    private fun isValidEmail(value: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    fun register_brigadista.getVolleyError(error: VolleyError): String {
        var errorMsg = ""
        if (error is NoConnectionError) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = cm.activeNetworkInfo
            errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                "Servidor no esta conectado a internet. Por favor vuelva a intentar"
            } else {
                "Tu dispositivo no esta conectado a internet. Por favor pruebe nuevamente cuando active la conexión a internet"
            }
        } else if (error is NetworkError || error.cause is ConnectException) {
            errorMsg =
                "Tu dispositivo no esta conectado a internet. Por favor pruebe nuevamente cuando active la conexion a internet"
        } else if (error.cause is MalformedURLException) {
            errorMsg = "Ha ocurrido algo mal en la solicitud, pruebe de nuevo…"
        } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
            errorMsg = "Humbo un error parseando la información …"
        } else if (error.cause is OutOfMemoryError) {
            errorMsg = "Dispositivo sin memoria"
        } else if (error is AuthFailureError) {
            errorMsg = "Error en la autenticación, Por favor verifique usuario y su contraseña"
        } else if (error is ServerError || error.cause is ServerError) {
            errorMsg =
                "El correo que intenta registrar ya fue registrado, verifique con soporte o vuelva a intentar"
        } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
                "Tu conexion se agoto, pruebe de nuevo"
            ))
        ) {
            errorMsg = "Se agotó el tiempo de conexión, Vuelva a intentarlo"
        } else {
            errorMsg = "Se agotó el tiempo de conexión. Vuelva a intentarlo. Vuelva a intentarlo."
        }

        return errorMsg
    }

}