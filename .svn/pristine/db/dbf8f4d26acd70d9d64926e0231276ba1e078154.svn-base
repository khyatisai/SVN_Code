package com.unfpa.appsistenciamaternaunfpa.api_controller

import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.database.DBHelper
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryList
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import org.json.JSONArray
import org.json.JSONObject

class API_Controller {
    companion object {
        fun getContentCategories(context: Context, listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()

                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_SRH_CONTENT_CATGORY, params) { response ->
                    if (response != null) {
                        val contentList: List<SRHContentCategory> =
                            Gson().fromJson(response.toString(), Array<SRHContentCategory>::class.java).toList()

                        //inserting category master data one by one
                        DBHelper.insertContentCategory(contentList, context)
                        listener.onComplete(Constant.REQ_CATEGORIES)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_CATEGORIES)
                e.printStackTrace()
            }
            return responseStr
        }

        fun getContentList(context: Context, listener: Listener, strCOCode: String, strLanguageCode: String): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()

                //adding country code & language code with URL
                val url: String
                if (!TextUtils.isEmpty(strCOCode) && !TextUtils.isEmpty(strLanguageCode))
                    url = EndPoints.URL_SRH_CONTENT_LIST + "/" + strCOCode + "/" + strLanguageCode
                //url = EndPoints.URL_SRH_CONTENT_LIST + "/" + "109" + "/" + strLanguageCode
                else
                    url = EndPoints.URL_SRH_CONTENT_LIST
                service.jsonArrayRequest(
                    Request.Method.GET,
                    url,
                    params
                ) { response ->
                    if (response != null) {
                        val contentList: List<SRHContent> =
                            Gson().fromJson(response.toString(), Array<SRHContent>::class.java).toList()

                        //inserting content master data one by one
                        DBHelper.insertContentMaster(contentList, context)
                        listener.onComplete(Constant.REQ_CONTENT_LIST)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_CONTENT_LIST)
                e.printStackTrace()
            }
            return responseStr
        }

        //added by 35251
        fun getServiceCenterDetail(
            context: Context,
            listener: Listener
//            strCOCode: String,
//            strLanguageCode: String
        ): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()
                val url: String
//                if (!TextUtils.isEmpty(strCOCode) && !TextUtils.isEmpty(strLanguageCode))
//                    url = EndPoints.URL_SERVICE_CENTER_DETAILS + "/" + strCOCode + "/" + strLanguageCode
//                //url = EndPoints.URL_SRH_CONTENT_LIST + "/" + "109" + "/" + strLanguageCode
//                else
                    url = EndPoints.URL_SERVICE_CENTER_DETAILS
                service.jsonArrayRequest(Request.Method.GET, url, params) { response ->
                    if (response != null) {
                        println(response[0])
                        val serviceCenterDetailList: List<ServiceCenterDetailEntity> =
                            Gson().fromJson(response.toString(), Array<ServiceCenterDetailEntity>::class.java).toList()
                        DBHelper.insertCenterContentDetails(serviceCenterDetailList, context)
                        listener.onComplete(Constant.REQ_SERVICE_CENTER_DETAIL)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_SERVICE_CENTER_DETAIL)
                e.printStackTrace()
            }
            return responseStr
        }

        //added by 35251
        fun getMyServiceList(context: Context, listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()
                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_MY_SERVICE_DETAILS, params) { response ->
                    if (response != null) {
                        val myServiceList: List<MyServiceEntity> =
                            Gson().fromJson(response.toString(), Array<MyServiceEntity>::class.java).toList()
                        DBHelper.insertMyServiceDetails(myServiceList, context)
                        listener.onComplete(Constant.REQ_SERVICE_LIST)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_SERVICE_LIST)
                e.printStackTrace()
            }
            return responseStr
        }


        //added by 35251
        fun getToken(context: Context): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                service.jsonArrayRequestForToken(Request.Method.GET, EndPoints.URL_GET_TOKEN) { response ->
                    if (response != null) {
                        val sharedPreference = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        editor.putString("Feedback_Token", response.toString())
                        editor.apply()
                        editor.commit()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return responseStr
        }

        //added by 35251
        fun postFeedbackRating(context: Context, jsonObject: JSONObject): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val sharedPreference = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val feedbackToken: String? = sharedPreference.getString("Feedback_Token", "")
                service.jsonStringRequest(
                    EndPoints.URL_POST_FOR_FEEDBACK,
                    jsonObject,
                    feedbackToken.toString()
                ) { response ->
                    if (response != null) {

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return responseStr
        }

        fun postWithToken(context: Context, jsonObject: JSONObject, strURL: String, listener: Listener): String {
            var responseStr = ""
            try {
                getToken(context!!.applicationContext)
                val service = ServiceVolley()
                val sharedPreference = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val feedbackToken: String? = sharedPreference.getString("Feedback_Token", "")
                service.jsonStringRequest(
                    strURL,
                    jsonObject,
                    feedbackToken.toString()
                ) { response ->
                    if (response != null) {
                        listener.onComplete(response.toString())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return responseStr
        }

        fun getQuiz(context: Context, listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()

                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_QUIZ_LISTING, params) { response ->
                    if (response != null) {
                        DBHelper.insertQuizRequest(response.toString(), context)
                        listener.onComplete(Constant.REQ_QUIZ)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_QUIZ)
                e.printStackTrace()
            }
            return responseStr
        }

        //added by 35251
        fun postMyVoiceStory(context: Context, jsonObject: JSONObject): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                service.jsonStringRequestForMyVoice(
                    EndPoints.URL_MY_VOICE,
                    jsonObject
                ) { response ->
                    if (response != null) {

                    }
                }
                /*service.postJSON(
                    EndPoints.URL_MY_VOICE,
                    jsonObject,""
                ) { response ->
                    if (response != null) {

                    }
                }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return responseStr
        }


        //added by 35251
        fun getCountryOfficeList(context: Context, listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()
                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_COUNTRY_OFFICE_LISTING, params) { response ->
                    if (response != null) {
                        //val countryOfficeList: List<CountryOfficeSettingEntity> = Gson().fromJson(response.toString(), Array<CountryOfficeSettingEntity>::class.java).toList()
                        DBHelper.insertCountryOfficeDetails(response.toString(), context)
                        listener.onComplete(Constant.REQ_COUNT_OFFICE)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_COUNT_OFFICE)
                e.printStackTrace()
            }
            return responseStr
        }

        fun getLatestTimestamp(listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()
                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_SYNC_TIMESTEMP, params) { response ->
                    if (response != null) {
                        //val countryOfficeList: List<CountryOfficeSettingEntity> = Gson().fromJson(response.toString(), Array<CountryOfficeSettingEntity>::class.java).toList()
                        responseStr = response.getJSONObject(0).getString("changed")
                        listener.onComplete(responseStr)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return responseStr
        }

        fun getCountryList(context: Context, listener: Listener): String {
            var responseStr = ""
            try {
                val service = ServiceVolley()
                val params = JSONArray()
                service.jsonArrayRequest(Request.Method.GET, EndPoints.URL_COUNTRY_LIST, params) { response ->
                    if (response != null) {
                        val countryList: List<CountryList> =
                            Gson().fromJson(response.toString(), Array<CountryList>::class.java).toList()
                        DBHelper.insertCountries(countryList, context)
                        listener.onComplete(Constant.REQ_COUNTRY_LIST)
                    }
                }
            } catch (e: Exception) {
                listener.onComplete(Constant.REQ_COUNTRY_LIST)
                e.printStackTrace()
            }
            return responseStr
        }


    }
}