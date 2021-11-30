package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.iid.FirebaseInstanceId
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.ChangePassword
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_login_pregnant.etEmail
import kotlinx.android.synthetic.main.activity_login_pregnant.etPassword
import kotlinx.android.synthetic.main.activity_login_pregnant.loginError
import kotlinx.android.synthetic.main.activity_nurse_login.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * Created by KhyatiShah on 8/17/2021.
 */
class LoginNurseActivity : AppCompatActivity() {

    var LOGIN = EndPoints.URL_HEROKU + "/api/v1/auth/login"
    var UPDATETOKEN = EndPoints.URL_HEROKU + "/api/v1/auth/fcm/token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurse_login)

        var resetPass = intent.getStringExtra("reset")
        if (resetPass == "1") {
            loginError.setText("Contraseña actualizada correctamente")
        }
        val jsonobj = JSONObject()
        btnLoginNurse.setOnClickListener {

            jsonobj.put("email", etEmail.text.toString().trim())
            jsonobj.put("password", etPassword.text.toString().trim())
            jsonobj.put("typeAuth", "email")
            jsonobj.put("typeUser", "enfermera")

            val que = Volley.newRequestQueue(this)
            val req = JsonObjectRequest(
                Request.Method.POST, LOGIN, jsonobj,
                Response.Listener { response ->
                    try {
                        val obj = response.getJSONObject("response")
                        val id = obj.getString("id")
                        val firstname = obj.getString("firstname")
                        val lastname = obj.getString("lastname")
                        val email = obj.getString("email")
                        val typeuser = obj.getString("typeUser")
                        AppUtils.setDataUser(this, id, firstname, lastname, email, typeuser)
                        storeToken(id)
                        var intent = Intent(this, MainNurseActivity::class.java)
                        startActivity(intent)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    //Log.d("test", getVolleyError(error, this))
                    loginErrorNurse.setText(getVolleyError(error));
                })
            que.add(req)
        }
        txtCambioPassNurse.setOnClickListener {
            var intent = Intent(this, ChangePassword::class.java)
            intent.putExtra("typeUser", "enfermera")
            startActivity(intent)
        }
    }

    fun storeToken(userId: String) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            Log.d("FCMService", deviceToken)
            val jsonobj = JSONObject()
            jsonobj.put("userId", userId)
            jsonobj.put("token", deviceToken)


            val que = Volley.newRequestQueue(this)
            val req = JsonObjectRequest(Request.Method.POST, UPDATETOKEN, jsonobj,
                Response.Listener { response ->
                    try {
                        Log.d("SUCCESS", "update token success")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR UPDATE TOKEN", "error update token")
                })
            que.add(req)
        }
    }

    fun LoginNurseActivity.getVolleyError(error: VolleyError): String {
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
            errorMsg = "Error interno en el servidor, por favor pruebe otra vez...."
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