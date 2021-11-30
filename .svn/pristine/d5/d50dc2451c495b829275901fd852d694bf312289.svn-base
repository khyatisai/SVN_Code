package com.unfpa.appsistenciamaternaunfpa.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.iid.FirebaseInstanceId
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_login_brigadista.*
import kotlinx.android.synthetic.main.activity_login_pregnant.etEmail
import kotlinx.android.synthetic.main.activity_login_pregnant.etPassword
import kotlinx.android.synthetic.main.activity_login_pregnant.loading
import kotlinx.android.synthetic.main.activity_login_pregnant.loginError
import kotlinx.android.synthetic.main.activity_login_pregnant.txtViewRegister
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException


import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

class LoginBrigadistaActivity : AppCompatActivity() {
    var LOGIN = EndPoints.URL_HEROKU + "/api/v1/auth/login"
    var UPDATETOKEN = EndPoints.URL_HEROKU + "/api/v1/auth/fcm/token"
    private val RC_SIGN_IN = 1
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_brigadista)

        val jsonobj = JSONObject()
        btnLogin.setOnClickListener{

            jsonobj.put("email", etEmail.text.toString().trim())
            jsonobj.put("password", etPassword.text.toString().trim())
            jsonobj.put("typeAuth", "email")
            jsonobj.put("typeUser", "brigadista")

            val que = Volley.newRequestQueue(this)
            val req = JsonObjectRequest(Request.Method.POST, LOGIN, jsonobj,
                Response.Listener {
                        response ->
                    try {

                        val obj = response.getJSONObject("response")
                        val id = obj.getString("id")
                        val firstname = obj.getString("firstname")
                        val lastname = obj.getString("lastname")
                        val email = obj.getString("email")
                        val typeuser = obj.getString("typeUser")
                        AppUtils.setDataUser(this, id,firstname,lastname,email,typeuser)
                        storeToken(id)
                        var intent = Intent(this, MainBrigadistaActivity::class.java)
                        startActivity(intent)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                        error ->
                    loginError.setText(getVolleyError(error));
        })
            que.add(req)
        }
        txtViewRegister.setOnClickListener{
            var intent = Intent(this, register_brigadista::class.java)
            startActivity(intent)
        }

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
//
//
//
//        //google sign in
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        googleButton.setOnClickListener {
//            loading.visibility = View.VISIBLE
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }
//        // facebook sign in
//        btLoginFacebook.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        //google sign in
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                loading.visibility = View.GONE
                //TODO: handle google sign in here
            } catch (e: ApiException) {
                e.printStackTrace()
                loading.visibility = View.GONE
                loginError.text = "test-log:" + e.message
            }
        }
    }
    fun LoginBrigadistaActivity.getVolleyError(error: VolleyError): String {
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

    fun storeToken(userId:String)
    {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val deviceToken = instanceIdResult.token
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
}