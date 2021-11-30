package com.unfpa.appsistenciamaternaunfpa.fragments.settings


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.*
import android.widget.Toast
import com.github.angads25.toggle.widget.LabeledSwitch
import com.nostra13.universalimageloader.core.ImageLoader

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.API_Controller
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : androidx.fragment.app.Fragment(), Listener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.action_settings)

            moduleVisibiltiy()

            btnClearMyHealthData.setOnClickListener {
                clearData("health")
            }

            btnUpdateAppData.setOnClickListener {
                updateData()
            }
            btnClearAppData.setOnClickListener {
                clearData("all")
            }
            btnClearMyKnowledgeData.setOnClickListener {
                clearData("knowledge")
            }
            btnClearMyServiceData.setOnClickListener {
                clearData("service")
            }
            btnClearMyVoiceData.setOnClickListener {
                clearData("voice")
            }

            /*For Language selection*/
            val languages = arrayOf("English", "Arabic", "Hindi", "Spanish", "Urdu")
            val spnSelectLanguage: Spinner = view.findViewById(R.id.spnSelectLanguage)
            //val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, languages)
            val languageArrayAdapter = ArrayAdapter(activity!!, R.layout.spinner_content, languages)
            spnSelectLanguage.adapter = languageArrayAdapter
            spnSelectLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //Toast.makeText(activity, "Selected " + languages[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }

            /*For Cache memory selection selection*/
            val cacheMemory = arrayOf("250", "200", "150", "100", "50")
            val spnCacheSize: Spinner = view.findViewById(R.id.spnCacheSize)
            //val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, languages)
            val cacheArrayAdapter = ArrayAdapter(activity!!, R.layout.spinner_content, cacheMemory)
            spnCacheSize.adapter = cacheArrayAdapter
            spnCacheSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //Toast.makeText(activity, "Selected " + cacheMemory[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }


            val sharedPreference = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            val switchMyHealth1 = sharedPreference.getBoolean("switchMyHealth", true)
            val switchMyServices1 = sharedPreference.getBoolean("switchMyServices", true)
            val switchMyVoice1 = sharedPreference.getBoolean("switchMyVoice", true)
            val switchMyVoiceCall1 = sharedPreference.getBoolean("switchMyVoiceCall", true)
            val switchMyVoiceChat1 = sharedPreference.getBoolean("switchMyVoiceChat", true)
            val switchAllowNotification1 = sharedPreference.getBoolean("switchAllowNotification", true)

            val switchMyHealth: LabeledSwitch = view.findViewById(R.id.switchMyHealth)
            val switchMyServices: LabeledSwitch = view.findViewById(R.id.switchMyServices)
            val switchMyVoice: LabeledSwitch = view.findViewById(R.id.switchMyVoice)
            val switchMyVoiceCall: LabeledSwitch = view.findViewById(R.id.switchMyVoiceCall)
            val switchMyVoiceChat: LabeledSwitch = view.findViewById(R.id.switchMyVoiceChat)
            val switchAllowNotification: LabeledSwitch = view.findViewById(R.id.switchAllowNotification)


            switchMyHealth.isOn = switchMyHealth1
            switchMyServices.isOn = switchMyServices1
            switchMyVoice.isOn = switchMyVoice1
            switchMyVoiceCall.isOn = switchMyVoiceCall1
            switchMyVoiceChat.isOn = switchMyVoiceChat1
            switchAllowNotification.isOn = switchAllowNotification1

            /*for My Health*/
            switchMyHealth.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchMyHealth", true)
                } else {
                    editor.putBoolean("switchMyHealth", false)
                }
                editor.apply()
                editor.commit()
            }

            /*for My Services*/
            switchMyServices.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchMyServices", true)
                } else {
                    editor.putBoolean("switchMyServices", false)
                }
                editor.apply()
                editor.commit()
            }

            /*for My Voice*/
            switchMyVoice.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchMyVoice", true)
                    linearLayoutMyVoiceCall.visibility = View.VISIBLE
                    viewMyVoiceCall.visibility = View.VISIBLE
                    linearLayoutMyVoiceChat.visibility = View.VISIBLE
                    viewMyVoiceChat.visibility = View.VISIBLE
                } else {
                    editor.putBoolean("switchMyVoice", false)
                    linearLayoutMyVoiceCall.visibility = View.GONE
                    viewMyVoiceCall.visibility = View.GONE
                    linearLayoutMyVoiceChat.visibility = View.GONE
                    viewMyVoiceChat.visibility = View.GONE
                }
                editor.apply()
                editor.commit()
            }

            /*for My Voice Call*/
            switchMyVoiceCall.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchMyVoiceCall", true)
                } else {
                    editor.putBoolean("switchMyVoiceCall", false)
                }
                editor.apply()
                editor.commit()
            }

            /*for My Voice Chat*/
            switchMyVoiceChat.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchMyVoiceChat", true)
                } else {
                    editor.putBoolean("switchMyVoiceChat", false)
                }
                editor.apply()
                editor.commit()
            }

            /*for Allow Notification*/
            switchAllowNotification.setOnToggledListener { toggleableView, isOn ->
                if (isOn) {
                    editor.putBoolean("switchAllowNotification", true)
                } else {
                    editor.putBoolean("switchAllowNotification", false)
                }
                editor.apply()
                editor.commit()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onComplete(response: String) {
        try {
            /* val strOldTimestemp = AppUtils.getStoredTimestemp(this.activity!!)
           if (strOldTimestemp.isNotEmpty()) {
                val oldTimeStemp = strOldTimestemp.toLong()
                val newTimestamp = response.toLong()
                if (newTimestamp > oldTimeStemp) {*/
            //wipe all data
            AppUtils.wipeAllDataForSync(this.activity!!)
            FetchDataTask().execute()
            /*}

        } else {
            FetchDataTask().execute()
        }*/
            //add new timestemp to preference
            AppUtils.setTimestemp(this.activity!!, response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun moduleVisibiltiy() {
        try {
            val sharedPreference1 = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString("CountryCode", "")
            val countryOfficeDAO =
                MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO()
            val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode.toString())
            val field_enable_health_manager: String = countryCodeRequest.field_enable_health_manager
            val field_enable_live_chat: String = countryCodeRequest.field_enable_live_chat
            val field_enable_service_locator: String = countryCodeRequest.field_enable_service_locator
            val field_language: String = countryCodeRequest.field_language
            val field_contact_number: String = countryCodeRequest.field_contact_number
            val field_health_management_export = countryCodeRequest.field_health_management_export.toString()
            val jarr = JSONArray(field_health_management_export)
            val field_health_enabled: String = jarr.getJSONObject(0).getString("enabled")
            if (field_enable_health_manager == "On") {
                linearLayoutMyHealth.visibility = View.VISIBLE
                viewMyHealth.visibility = View.VISIBLE
            } else {
                linearLayoutMyHealth.visibility = View.GONE
                viewMyHealth.visibility = View.GONE
            }
            if (field_enable_service_locator == "On") {
                linearLayoutMyServices.visibility = View.VISIBLE
                viewMyServices.visibility = View.VISIBLE
            } else {
                linearLayoutMyServices.visibility = View.GONE
                viewMyServices.visibility = View.GONE
            }
            if (field_contact_number == "") {
                linearLayoutMyVoiceCall.visibility = View.GONE
                viewMyVoiceCall.visibility = View.GONE
            } else {
                val switchMyVoice = sharedPreference1.getBoolean("switchMyVoice", true)
                if (switchMyVoice) {
                    linearLayoutMyVoiceCall.visibility = View.VISIBLE
                    viewMyVoiceCall.visibility = View.VISIBLE
                } else {
                    linearLayoutMyVoiceCall.visibility = View.GONE
                    viewMyVoiceCall.visibility = View.GONE
                }

            }
            if (field_enable_live_chat == "On") {
                val switchMyVoice = sharedPreference1.getBoolean("switchMyVoice", true)
                if (switchMyVoice) {
                    linearLayoutMyVoiceChat.visibility = View.VISIBLE
                    viewMyVoiceChat.visibility = View.VISIBLE
                } else {
                    linearLayoutMyVoiceChat.visibility = View.GONE
                    viewMyVoiceChat.visibility = View.GONE
                }

            } else {
                linearLayoutMyVoiceChat.visibility = View.GONE
                viewMyVoiceChat.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearData(data: String) {
        try {
            if (data == "all") {
                /*Delete CountryCode Data*/
                //MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO().deleteAll()

                /*Delete Service Data*/
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myServiceDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.serviceCenterDetailDAO()
                    .deleteAll()
                /*Delete myVoice Data*/
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myVoiceDAO().deleteAll()
                /*Delete myVoice Data*/
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO().deleteAll()

                /*Delete myHealth Data*/
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.appointmentReminderDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.medicineReminderDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.medicineEventDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.periodTrackerDAO().deleteAll()

                /*Delete myKnowledge Data*/
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.SRHContentCategoryDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.quizRequestDAO().deleteAll()

                //clearing server's time stemp
                //add new timestemp to preference
                AppUtils.setTimestemp(this.activity!!, "")
                ImageLoader.getInstance().clearMemoryCache()
                ImageLoader.getInstance().clearDiskCache()


                Toast.makeText(activity, activity!!.getString(R.string.data_cleared), Toast.LENGTH_SHORT).show()
            }

            if (data == "knowledge") {
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.SRHContentCategoryDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO().deleteAll()
                ImageLoader.getInstance().clearMemoryCache()
                ImageLoader.getInstance().clearDiskCache()
                Toast.makeText(activity, activity!!.getString(R.string.my_knowledge_data_cleared), Toast.LENGTH_SHORT)
                    .show()
            }
            if (data == "service") {
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myServiceDAO().deleteAll()
                Toast.makeText(activity, activity!!.getString(R.string.my_service_data_cleared), Toast.LENGTH_SHORT)
                    .show()
            }
            if (data == "voice") {
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO().deleteAll()
                Toast.makeText(activity, activity!!.getString(R.string.my_voice_data_cleared), Toast.LENGTH_SHORT)
                    .show()
            }

            if (data == "health") {
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.appointmentReminderDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.medicineReminderDAO()
                    .deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.medicineEventDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.periodTrackerDAO().deleteAll()
                Toast.makeText(activity, activity!!.getString(R.string.my_health_data_cleared), Toast.LENGTH_SHORT)
                    .show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateData() {
        if (AppUtils.isConnectingToInternet(this!!.activity!!)) {
            API_Controller.getLatestTimestamp(this)
        } else {

            Toast.makeText(
                this.activity,
                getString(R.string.please_connect_to_internet),
                Toast.LENGTH_LONG
            )
                .show()


        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.home).isVisible = true
        }
    }

    private fun checkModulesVisibility() {
        try {
            val sharedPreference1 = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString("CountryCode", "")
            val countryOfficeDAO =
                MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO()
            val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode.toString())
            val field_enable_health_manager: String = countryCodeRequest.field_enable_health_manager
            val field_enable_live_chat: String = countryCodeRequest.field_enable_live_chat
            val field_enable_service_locator: String = countryCodeRequest.field_enable_service_locator
            val field_language: String = countryCodeRequest.field_language
            val field_contact_number: String = countryCodeRequest.field_contact_number
            val field_health_management_export = countryCodeRequest.field_health_management_export.toString()
            val jarr = JSONArray(field_health_management_export)
            val field_health_enabled: String = jarr.getJSONObject(0).getString("enabled")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    val apiCompleteListener = object : Listener {
        override fun onComplete(response: String) {

        }

    }

    inner class FetchDataTask() : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {

            Thread.sleep(2500)
            API_Controller.getCountryOfficeList(activity!!.applicationContext, apiCompleteListener)
            API_Controller.getContentCategories(activity!!.applicationContext, apiCompleteListener)
            API_Controller.getContentList(
                activity!!.applicationContext,
                apiCompleteListener,
                AppUtils.getCountryOffice(activity!!),
                AppUtils.getLanguageCode(activity!!)
            )
            API_Controller.getServiceCenterDetail(
                activity!!.applicationContext,
                apiCompleteListener
//                AppUtils.getCountryOffice(activity!!),
//                AppUtils.getLanguageCode(activity!!)
            )
            API_Controller.getQuiz(activity!!.applicationContext, apiCompleteListener)
            API_Controller.getMyServiceList(activity!!.applicationContext, apiCompleteListener)
            return null

        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar1.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progressBar1.visibility = View.GONE
            Toast.makeText(activity, activity!!.getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
        }

    }
}

data class Langauge(val Name: String, val code: String)