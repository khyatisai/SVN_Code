package com.unfpa.appsistenciamaternaunfpa.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.Toast
import com.unfpa.appsistenciamaternaunfpa.Mhealth
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.API_Controller
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash.*
import com.nostra13.universalimageloader.core.ImageLoader
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.dao.personal.CountryOfficeDAO
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.activity_splash.imgSplash
import kotlinx.android.synthetic.main.popup_welcome.*
import android.text.Html
import android.os.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SplashActivityOld : AppCompatActivity(), Listener {

    lateinit var sharedPreference: SharedPreferences
    var mainHandler: Handler = Handler(Looper.getMainLooper())
    lateinit var countryOfficeDAO: CountryOfficeDAO
    var isMessageShown = false
    var isPopupDisplayed = false
    var reqCat = false
    var reqContentList = false
    var reqServiceCenterDetail = false
    var reqServiceList = false
    var reqQuiz = false
    var reqCountryOfcie = false
    var reqCountryList = false

    private val updateTask = object : Runnable {
        override fun run() {

            checkInternet()
            mainHandler.postDelayed(this, 30000)

        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTask)
        //Logging firebase screen
        AppUtils.trackScreen(Constant.APP_OPEN, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        try {
            /* if (AppUtils.getIsFirstTime(this)) {
                 showWelcomePopup()
             }*/
            sharedPreference = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
            countryOfficeDAO =
                MhealthRoomDatabase.getAppDataBase(this.applicationContext)!!.countryOfficeDAO()
            if (countryOfficeDAO.getAllContent().isNotEmpty()) {
                val CountryCode = sharedPreference.getString(Constant.ITEM_COUNTRY_CODE, "")
                val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode.toString())

                if (CountryCode != null) {
                    if (CountryCode.isNotEmpty()) {
                        if (countryCodeRequest.field_image.isNotEmpty()) {
                            val imageLoader = ImageLoader.getInstance()
                            val strURL = (EndPoints.URL_ROOT + countryCodeRequest.field_image).replace("\\s".toRegex(), "")
                            imageLoader.displayImage(strURL, imgSplash, Mhealth.imageOptions)
                        }
                    }
                }
            }
            //mainHandler = Handler(Looper.getMainLooper())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showWelcomePopup() {
        isPopupDisplayed = true
        AppUtils.setIsFirstTime(this, false)
        val dialog = Dialog(this, android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_welcome)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            dialog.txtWelcomeDesc.setText(
//                Html.fromHtml(
//                    getString(R.string.desc_welcom_screen),
//                    Html.FROM_HTML_MODE_COMPACT
//                )
//            )
        } else {
//            dialog.txtWelcomeDesc.setText(Html.fromHtml(getString(R.string.desc_welcom_screen)))
        }
        val handler = Handler()
        handler.postDelayed({ dialog.show() }, 1000) // 3000 milliseconds delay
        dialog.btnContinue.setOnClickListener {
            isPopupDisplayed = false
            progressBar.visibility = View.VISIBLE
            dialog.dismiss()
        }
    }

    fun checkInternet() {
        if (AppUtils.getIsFirstTime(this)) {
            showWelcomePopup()
        }
        if (!isPopupDisplayed) {
            if (AppUtils.isConnectingToInternet(this@SplashActivityOld)) {
                API_Controller.getLatestTimestamp(this)
            } else {

                if (countryOfficeDAO.getAllContent().isNotEmpty()) { //condition to check if data is there in DB or not
                    mainHandler.removeCallbacks(updateTask)
                    redirectTONextScreen()
                } else {
                    if (!isMessageShown) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.please_connect_to_internet),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        isMessageShown = true
                    }
                }
            }
        }
    }

    override fun onComplete(response: String) {
        try {
            val strOldTimestemp = AppUtils.getStoredTimestemp(this)
            if (strOldTimestemp.isNotEmpty()) {
                val oldTimeStemp = strOldTimestemp.toLong()
                val newTimestamp = response.toLong()
                if (newTimestamp > oldTimeStemp) {
                    //wipe all data
                    AppUtils.wipeAllDataForSync(this)
                    FetchDataTask().execute()
                } else {
                    redirectTONextScreen()
                }

            } else {
                FetchDataTask().execute()
            }
            //add new timestemp to preference
            AppUtils.setTimestemp(this, response)
            mainHandler.removeCallbacks(updateTask)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun redirectTONextScreen() {
        val sharedPreference = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        val isInteroComplete = sharedPreference.getBoolean(Constant.INTRO_FLAG, false)
        intent = if (!isInteroComplete) {
            Intent(applicationContext, IntroductoryActivity::class.java)
        } else {
            Intent(applicationContext, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    val apiCompleteListener = object : Listener {
        override fun onComplete(response: String) {
            when (response) {
                Constant.REQ_CATEGORIES -> {
                    reqCat = true
                }
                Constant.REQ_CONTENT_LIST -> {
                    reqContentList = true
                }
                Constant.REQ_SERVICE_CENTER_DETAIL -> {
                    reqServiceCenterDetail = true
                }
                Constant.REQ_SERVICE_LIST -> {
                    reqServiceList = true
                }
                Constant.REQ_QUIZ -> {
                    reqQuiz = true
                }
                Constant.REQ_COUNT_OFFICE -> {
                    reqCountryOfcie = true
                }
                Constant.REQ_COUNTRY_LIST -> {
                    reqCountryList = true
                }

            }
            if (reqCat && reqContentList && reqServiceCenterDetail && reqServiceList && reqQuiz && reqCountryOfcie && reqCountryList) {
                progressBar.visibility = View.INVISIBLE
                redirectTONextScreen()
            }
        }

    }

    inner class FetchDataTask() : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            API_Controller.getCountryOfficeList(applicationContext, apiCompleteListener)
            API_Controller.getContentCategories(applicationContext, apiCompleteListener)
            API_Controller.getContentList(
                applicationContext, apiCompleteListener, AppUtils.getCountryOffice(this@SplashActivityOld),
                AppUtils.getLanguageCode(this@SplashActivityOld)
            )
//            API_Controller.getServiceCenterDetail(
////                applicationContext, apiCompleteListener, AppUtils.getCountryOffice(this@SplashActivityOld),
////                AppUtils.getLanguageCode(this@SplashActivityOld)
////            )
            API_Controller.getServiceCenterDetail(
                applicationContext, apiCompleteListener
            )
            API_Controller.getQuiz(applicationContext, apiCompleteListener)
            API_Controller.getMyServiceList(applicationContext, apiCompleteListener)
            API_Controller.getCountryList(applicationContext, apiCompleteListener)
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            /*progressBar.visibility = View.INVISIBLE
            redirectTONextScreen()*/
        }
    }
}


