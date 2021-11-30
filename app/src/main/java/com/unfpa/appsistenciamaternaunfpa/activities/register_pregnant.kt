package com.unfpa.appsistenciamaternaunfpa.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.unfpa.appsistenciamaternaunfpa.BuildConfig
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_register_pregnant2.*
import kotlinx.android.synthetic.main.activity_register_pregnant2.btLoginFacebook
import kotlinx.android.synthetic.main.activity_register_pregnant2.etApellido
import kotlinx.android.synthetic.main.activity_register_pregnant2.etCedula
import kotlinx.android.synthetic.main.activity_register_pregnant2.etContrasena
import kotlinx.android.synthetic.main.activity_register_pregnant2.etCorreo
import kotlinx.android.synthetic.main.activity_register_pregnant2.etNombre
import kotlinx.android.synthetic.main.activity_register_pregnant2.etTelefono
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

import android.view.View.OnTouchListener
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import java.util.*


@SuppressLint("SetTextI18n")
class register_pregnant : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    val jsonobj = JSONObject()

    var id = ""
    var firstName = ""
    var middleName = ""
    var lastName = ""
    var name = ""
    var picture = ""
    var email = ""
    var accessToken = ""

    var etfirtsName = ""
    var etLastName = ""
    var etEmail = ""
    var etPassword = ""
    var etPhone = ""
    var etID = ""
    var REGISTER = EndPoints.URL_HEROKU + "/api/v1/auth/register"

    private var dpd: DatePickerDialog? = null

    private var formatterCedula: MaskedFormatter? = null
    private var formatterTelefono: MaskedFormatter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pregnant2)

//        ANYTHING KEY = *
//        DIGIT KEY = #
//        UPPERCASE KEY = U
//        LOWERCASE KEY = L
//        ALPHA NUMERIC KEY = A
//        CHARACTER KEY = ?
//        HEX KEY = H

//        setMaskCedula("###-######-####?")
        setMaskTelefono("####-####")

        callbackManager = CallbackManager.Factory.create()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            // Display Selected date in textbox
            var dateBirth = "" + year + "/" + (month + 1) + "/" + dayOfMonth
            etFechaNacimiento.setText(dateBirth)
        }, year, month, day)

        etFechaNacimiento.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                dpd!!.show()
                return@OnTouchListener true
            }
            false
        })

        btnLogin.setOnClickListener{
            etfirtsName = etNombre.text.toString().trim()
            etLastName = etApellido.text.toString().trim()
            etEmail = etCorreo.text.toString().trim()
            etPassword = etContrasena.text.toString().trim()
            etPhone = etTelefono.text.toString().trim()
            etID = etCedula.text.toString().trim()

            if(areFieldsValid(etfirtsName, etLastName, etEmail, etPassword, etPhone, etID)) {

                val cedulaReplace  = etID.replace("-","")
                val telefonoReplace = etPhone.replace("-","")

                jsonobj.put("firstname", etfirtsName)
                jsonobj.put("lastname", etLastName)
                jsonobj.put("birth", etFechaNacimiento.text.toString())
                jsonobj.put("email", etEmail)
                jsonobj.put("cedula", cedulaReplace)
                jsonobj.put("password", etPassword)
                jsonobj.put("phone", telefonoReplace)
                jsonobj.put("typeAuth", "email")
                jsonobj.put("typeUser", "paciente")

                val que = Volley.newRequestQueue(this)
                val req = JsonObjectRequest(Request.Method.POST, REGISTER, jsonobj,
                    Response.Listener {
                            response ->
                        //Toast.makeText(this, "Paciente registrado", Toast.LENGTH_SHORT).show()
                        val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                        applicationContext?.let { it1 ->
                            AppUtils.createCustomToast("Paciente registrada", it1, layoutToast,"success")
                        }
                        var intent = Intent(this, LoginPregnantActivity::class.java)
                        startActivity(intent)
                        println(response)
                    }, Response.ErrorListener {
                            error ->
                        registerPError.setText(getVolleyError(error))
                    })
                que.add(req)
            }
        }

        tvIniciaSesion1.setOnClickListener{
            var intent = Intent(this, TypeAccountActivity::class.java)
            startActivity(intent)
        }

        btLoginFacebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        // Callback registration
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("TAG", "Success Login")
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@register_pregnant, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@register_pregnant, exception.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setMaskCedula(mask: String) {
        formatterCedula = MaskedFormatter(mask)
        formatterCedula?.let{
            etCedula.addTextChangedListener(MaskedWatcher(it, etCedula))
        }
        val s = formatterCedula?.formatString(etCedula.text.toString())?.unMaskedString
    }
    private fun setMaskTelefono(mask: String) {
        formatterTelefono = MaskedFormatter(mask)
        formatterTelefono?.let{
            etTelefono.addTextChangedListener(MaskedWatcher(it, etTelefono))
        }
        val s = formatterTelefono?.formatString(etTelefono.text.toString())?.unMaskedString
    }
    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }
                accessToken = token.toString()

                // Facebook Id
                if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                    id = facebookId.toString()
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                    id = "Not exists"
                }


                // Facebook First Name
                if (jsonObject.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                    firstName = facebookFirstName
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                    firstName = "Not exists"
                }


                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                    middleName = facebookMiddleName
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                    middleName = "Not exists"
                }


                // Facebook Last Name
                if (jsonObject.has("last_name")) {
                    val facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                    lastName = facebookLastName
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                    lastName = "Not exists"
                }


                // Facebook Name
                if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                    name = facebookName
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                    name = "Not exists"
                }


                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            picture = facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                    picture = "Not exists"
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                    email = facebookEmail
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                    email = "Not exists"
                }


            }).executeAsync()
    }


    private fun areFieldsValid(firtsname:String, lastname:String, email:String, password:String, telefono:String, cedula:String):Boolean{

        if(firtsname.isEmpty()){
            etNombre.setError("Campo Obligatorio")
            etNombre.requestFocus()
            return false;
        }
        if(lastname.isEmpty()){
            etApellido.setError("Campo Obligatorio")
            etApellido.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etFechaNacimiento.text)) {
            etFechaNacimiento.error = "Campo obligatorio"
            etFechaNacimiento.requestFocus()
            return false
        }
        if(isValidCedula(cedula)){
            etCedula.setError("La Cedula es incorrecta")
            etCedula.requestFocus()
            return false
        }
        if(!isValidPhone(telefono)){
            etTelefono.setError("Teléfono inválido")
            etTelefono.requestFocus()
            return false
        }
        if(!isValidEmail(email)){
            etCorreo.setError("Correo inválido")
            etCorreo.requestFocus()
            return false
        }


        if(!isValidPassword(password)){
            etContrasena.setError("La Contraseña debe de tener al menos 4 caracteres")
            etContrasena.requestFocus()
            return false
        }


        return true
    }



    private fun isValidCedula(cedula: String): Boolean {
        //etCedula.addTextChangedListener(Mask.mask("###.###.###-##", etCedula))
        val meh = cedula.length
        if(cedula != ""){
            return true
        }
        return false
    }

    private fun isValidPhone(value: String): Boolean {
        if(value.length == 9){
            return  true
        }
        return false
    }

    private fun isValidPassword(value: String): Boolean {
        if(value.length >= 4){
            return  true
        }
        return false
    }

    private fun isValidEmail(value: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    fun register_pregnant.getVolleyError(error: VolleyError): String {
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
            errorMsg = "Tu dispositivo no esta conectado a internet. Por favor pruebe nuevamente cuando active la conexion a internet"
        } else if (error.cause is MalformedURLException) {
            errorMsg = "Ha ocurrido algo mal en la solicitud, pruebe de nuevo…"
        } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
            errorMsg = "Humbo un error parseando la información …"
        } else if (error.cause is OutOfMemoryError) {
            errorMsg = "Dispositivo sin memoria"
        } else if (error is AuthFailureError) {
            errorMsg = "Error en la autenticación, Por favor verifique usuario y su contraseña"
        } else if (error is ServerError || error.cause is ServerError) {
            errorMsg = "El correo que intenta registrar ya fue registrado, verifique con soporte o vuelva a intentar"
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