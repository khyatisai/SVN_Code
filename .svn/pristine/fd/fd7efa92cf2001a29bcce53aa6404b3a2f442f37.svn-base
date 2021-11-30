package com.unfpa.appsistenciamaternaunfpa.api_controller

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.unfpa.appsistenciamaternaunfpa.Mhealth
import org.json.JSONArray
import org.json.JSONObject
import android.util.Base64
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest


class ServiceVolley {

    val TAG = ServiceVolley::class.java.simpleName

    val imageLoader: ImageLoader by lazy {
        ImageLoader(Mhealth.appInstance?.requestQueue, LruBtimapCache())
    }

    fun jsonStringRequest(
        url: String,
        params: JSONObject,
        feedback: String ,
        completionHandler: (response: String?) -> Unit
    ) {
        val jsonStringReq = object : StringRequest(Method.POST, url, Response.Listener {
                response ->
            Log.d(TAG, "/postJSON request OK! Response: $response")
            completionHandler(response.toString())
        },  Response.ErrorListener { error ->
            VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
            completionHandler(null)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/hal+json"
               // headers["Content-Type"] = "application/json"
                headers["X-CSRF-Token"] = feedback
                val credentials = "priya:priya"
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                headers["Authorization"] = auth //"Basic cHJpeWE6cHJpeWE="
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return params.toString().toByteArray()
            }
            override fun getBodyContentType():String {
                return "application/hal+json; charset=utf-8"
            }
        }
        Mhealth.appInstance?.addToRequestQueue(jsonStringReq, TAG)
    }


    fun jsonStringRequestForMyVoice(
        url: String,
        params: JSONObject,
        completionHandler: (response: String?) -> Unit
    ) {
        val jsonStringReq = object : StringRequest(Method.POST, url, Response.Listener {
                response ->
            Log.d(TAG, "/postJSON request OK! Response: $response")
            completionHandler(response.toString())
        },  Response.ErrorListener { error ->
            VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
            completionHandler(null)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/hal+json"
                //headers["Content-Type"] = "application/hal+json"
                val credentials = "priya:priya"
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                headers["Authorization"] = auth //"Basic cHJpeWE6cHJpeWE="
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return params.toString().toByteArray()
            }
            override fun getBodyContentType():String {
                return "application/hal+json; charset=utf-8"
            }
        }
        Mhealth.appInstance?.addToRequestQueue(jsonStringReq, TAG)
    }


    fun postJSON(url: String,
                 params: JSONObject,
                 feedback: String ,
                 completionHandler: (response: JSONObject?) -> Unit) {

        val jsonObjReq = object : JsonObjectRequest(Method.POST, url, params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/postJSON request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                error.stackTrace
                error.networkResponse
                error.printStackTrace()
                VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/hal+json"
                headers["Content-Type"] = "application/hal+json"
                /*headers["X-CSRF-Token"] = feedback*/
                val credentials = "priya:priya"
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                headers["Authorization"] = auth //"Basic cHJpeWE6cHJpeWE="
                return headers
            }
            /*override fun getBodyContentType():String {
                return "application/json; charset=utf-8"
            }*/
        }

        Mhealth.appInstance?.addToRequestQueue(jsonObjReq, TAG)
    }

    fun get(url: String, params: JSONArray, completionHandler: (response: JSONArray?) -> Unit) {
        val jsonObjReq = object : JsonArrayRequest(Method.GET, url, params,
            Response.Listener<JSONArray> { response ->
                Log.d(TAG, "/postJSON request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }

        Mhealth.appInstance?.addToRequestQueue(jsonObjReq, TAG)
    }

    fun jsonArrayRequest(
        method: Int,
        url: String,
        params: JSONArray,
        completionHandler: (response: JSONArray?) -> Unit
    ) {

        val jsonArrayReq = object : JsonArrayRequest(method, url, params, Response.Listener<JSONArray> { response ->
            Log.d(TAG, "/postJSON request OK! Response: $response")
            completionHandler(response)
        }, Response.ErrorListener { error ->
            VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
            completionHandler(null)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        Mhealth.appInstance?.addToRequestQueue(jsonArrayReq, TAG)
    }

    fun jsonArrayRequestForToken(
        method: Int,
        url: String,
        completionHandler: (response: String?) -> Unit
    ) {
        val jsonArrayReq = object : StringRequest(method, url, Response.Listener {
            response ->
            Log.d(TAG, "/postJSON request OK! Response: $response")
            completionHandler(response.toString())
        },  Response.ErrorListener { error ->
            VolleyLog.e(TAG, "/postJSON request fail! Error: ${error.message}")
            completionHandler(null)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        Mhealth.appInstance?.addToRequestQueue(jsonArrayReq, TAG)
    }

    fun jsonStringReq(
        method: Int,
        url: String,
        params: JSONArray,
        completionHandler: (response: String?) -> Unit
    ){
        val stringRequest = object : StringRequest(method, url, Response.Listener { s ->
            // Your success code here
        }, Response.ErrorListener { e ->
            // Your error code here
        }) {
            override fun getParams(): Map<String, String> = mapOf("idaccount" to "1")
        }
        Mhealth.appInstance?.addToRequestQueue(stringRequest, TAG)
    }

}