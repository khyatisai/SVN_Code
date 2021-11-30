package com.unfpa.appsistenciamaternaunfpa.activities


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import kotlinx.android.synthetic.main.activity_login_pregnant.etEmail
import kotlinx.android.synthetic.main.activity_login_pregnant.etPassword
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_change_password.btnResetPassword
import kotlinx.android.synthetic.main.activity_change_password.etPassword2
import kotlinx.android.synthetic.main.activity_change_password.msgError
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException


import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException


class ChangePasswordPregnant : AppCompatActivity() {
    var LOGIN = EndPoints.URL_HEROKU + "/api/v1/auth/resetpassword"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_pregnant)
//        callbackManager = CallbackManager.Factory.create()
//        LoginManager.getInstance()
//            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//                override fun onSuccess(result: LoginResult?) {
//                    Log.e("facebook", "onSuccess")
//                    //TODO: handle facebook sign in here
//                }
//
//                override fun onCancel() {
//                    Log.e("facebook", "onCancel")
//
//                }
//
//                override fun onError(error: FacebookException?) {
//                    loginError.text = error?.message
//                }
//            })



        //google sign in
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
       // googleSignInClient = GoogleSignIn.getClient(this, gso)

//        googleButton.setOnClickListener {
//            loading.visibility = View.VISIBLE
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }
        // facebook sign in
//        btLoginFacebook.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
//        }

        val jsonobj = JSONObject()
        btnResetPassword.setOnClickListener{
            var password1 = etPassword.text.toString()
            var password = etPassword2.text.toString()

            if(!password.equals(password1)){
                etPassword2.setError("Contraseñas deben ser iguales")
                Toast.makeText(this, "Contraseña deben ser iguales", Toast.LENGTH_LONG)
            }else{

                jsonobj.put("email", etEmail.text.toString().trim())
                jsonobj.put("password", etPassword.text.toString().trim())

                val que = Volley.newRequestQueue(this)
                val req = JsonObjectRequest(Request.Method.POST, LOGIN, jsonobj,
                    Response.Listener { response ->
                        try {
                            if (response.getString("message") == "success") {
                                var intent = Intent(this, LoginPregnantActivity::class.java)
                                startActivity(intent);
                                //Toast.makeText(this, "Contraseña actualizada correctamente",Toast.LENGTH_LONG).show()
                                val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                                applicationContext?.let { it1 ->
                                    AppUtils.createCustomToast("Se ha enviado un mensaje a su correo para cambiar la contraseña", it1, layoutToast,"success")
                                }
                            } else if(response.getString("message") == "error") {
                                msgError.setText("Correo electrónico no valido")
                            }else{
                                msgError.setText("Error al cambiar la contraseña, verifique que el correo sea el correcto")
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        msgError.setText(getVolleyError(error))
                    })
                que.add(req)
            }
        }
    }


    fun ChangePasswordPregnant.getVolleyError(error: VolleyError): String {
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





