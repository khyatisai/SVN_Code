package com.unfpa.appsistenciamaternaunfpa.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.iid.FirebaseInstanceId
import com.unfpa.appsistenciamaternaunfpa.BuildConfig
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_login_pregnant.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException


class LoginPregnantActivity : AppCompatActivity() {

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

    var LOGIN = EndPoints.URL_HEROKU + "/api/v1/auth/login"
    var REGISTER = EndPoints.URL_HEROKU + "/api/v1/auth/register"
    var UPDATETOKEN = EndPoints.URL_HEROKU + "/api/v1/auth/fcm/token"

    /*
       Don't forget to add your own App ID and Protocol Scheme in strings.xml to test this app
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pregnant)


        callbackManager = CallbackManager.Factory.create()


        if (isLoggedIn()) {
            Log.d("LoggedIn? :", "YES")

        } else {
            Log.d("LoggedIn? :", "NO")
            // Show the Home Activity
        }

//       Login Button made by Facebook
//
//
//        val loginButton = findViewById<LoginButton>(R.id.login_button)
//        loginButton.setReadPermissions(listOf("public_profile", "email"))
//        // If you are using in a fragment, call loginButton.setFragment(this)
//
//        // Callback registration
//        // If you are using in a fragment, call loginButton.setFragment(this)
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override fun onSuccess(loginResult: LoginResult?) {
//                // Get User's Info
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })

//      Custom Login Button



        val jsonobj = JSONObject()
        btnNuevaPaciente.setOnClickListener{

            jsonobj.put("email", etEmail.text)
            jsonobj.put("password", etPassword.text)
            jsonobj.put("typeAuth", "email")
            jsonobj.put("typeUser", "paciente")

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
                            var intent = Intent(this, MainPregnantActivity::class.java)
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
                    Toast.makeText(this@LoginPregnantActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@LoginPregnantActivity, exception.message, Toast.LENGTH_LONG).show()
                }
            })

        txtViewRegister.setOnClickListener{
            var intent = Intent(this, register_pregnant::class.java)
            startActivity(intent)
        }

        txtCambioPass2.setOnClickListener{
            var intent = Intent(this, ChangePasswordPregnant::class.java)
            startActivity(intent)
        }
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

                openDetailsActivity()
            }).executeAsync()
    }

    fun isLoggedIn(): Boolean {
//        val accessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired
        val id = AppUtils.getDataUser(this)
        val typeUser = AppUtils.getTypeUser(this)
        val isLoggedIn = id.isNotEmpty() && typeUser.isNotEmpty()
        Log.d("mi typo de usuario",typeUser)
        Log.d("mi id",id)
        Log.d("validacion",isLoggedIn.toString())
        return isLoggedIn
    }


    fun logOutUser() {
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(this, DetailsFacebookActivity::class.java)
        myIntent.putExtra("facebook_id", id)
        myIntent.putExtra("facebook_first_name", firstName)
        myIntent.putExtra("facebook_middle_name", middleName)
        myIntent.putExtra("facebook_last_name", lastName)
        myIntent.putExtra("facebook_name", name)
        myIntent.putExtra("facebook_picture", picture)
        myIntent.putExtra("facebook_email", email)
        myIntent.putExtra("facebook_access_token", accessToken)
        startActivity(myIntent)
    }

    private fun saveUserByFB(){
        jsonobj.put("firstname", firstName+middleName)
        jsonobj.put("lastname", lastName)
        jsonobj.put("email", email)
        jsonobj.put("cedula", "")
        jsonobj.put("password", id)
        jsonobj.put("phone", "")
        jsonobj.put("typeAuth", "facebook")
        jsonobj.put("typeUser", "paciente")

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(Request.Method.POST, REGISTER, jsonobj,
            Response.Listener {response ->
                try {

                    var intent = Intent(this, LoginPregnantActivity::class.java)
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

    fun LoginPregnantActivity.getVolleyError(error: VolleyError): String {
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
