package com.unfpa.appsistenciamaternaunfpa.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.AppointmentReminderAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.MedicineReminderAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentListAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.MyServiceRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.beforecall
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.edtSearch
import kotlinx.android.synthetic.main.fragment_home.lstMedicineReminder
import kotlinx.android.synthetic.main.fragment_home.lstSRHContent
import java.lang.Exception

/**
 * Created by KhyatiShah on 8/16/2019.
 */
class HomeFragment : androidx.fragment.app.Fragment() {

    lateinit var medicineReminderList: List<MedicineReminder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
            val sharedPreference1 = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString("CountryCode", "")
            var field_enable_health_manager = "On"
            var field_enable_service_locator = "On"
            if (!CountryCode.isNullOrEmpty()) {
                val countryOfficeDAO =
                    MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO()
                val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode)
                field_enable_health_manager = countryCodeRequest.field_enable_health_manager
                field_enable_service_locator = countryCodeRequest.field_enable_service_locator
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 20, 0)//(left, top, right, bottom)

            val switchMyHealth = sharedPreference1.getBoolean("switchMyHealth", true)
            val switchMyServices = sharedPreference1.getBoolean("switchMyServices", true)
            val switchMyVoice = sharedPreference1.getBoolean("switchMyVoice", true)


            if (field_enable_health_manager == "On") {
                if (switchMyHealth) {
                    cardMyHealth.visibility = View.VISIBLE
                    // cardMyHealth1.visibility = View.VISIBLE

                } else {
                    cardMyHealth.visibility = View.GONE
                    //cardMyHealth1.visibility = View.GONE
                    val params1 = LinearLayout.LayoutParams(450, 450)
                    params1.setMargins(0, 0, 60, 0)//(left, top, right, bottom)// android:layout_marginRight="20dp
                    // cardMyVoice1.layoutParams = params1
                }
                //cardMyHealth.layoutParams = params
            } else {
                cardMyHealth.visibility = View.GONE
                //cardMyHealth1.visibility = View.GONE
                val params1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                params1.setMargins(0, 0, 60, 0)//(left, top, right, bottom)// android:layout_marginRight="20dp
                // cardMyVoice1.layoutParams = params1
            }

            if (field_enable_service_locator == "On") {
                if (switchMyServices) {
                    cardMyServices.visibility = View.VISIBLE
                    //cardMyServices1.visibility = View.VISIBLE
                } else {
                    cardMyServices.visibility = View.GONE
                    //cardMyServices1.visibility = View.GONE
                }
                //cardMyKnowledge.layoutParams = params
            } else {
                cardMyServices.visibility = View.GONE
                // cardMyServices1.visibility = View.GONE
            }
            if (switchMyVoice) {
                cardMyVoice.visibility = View.VISIBLE
                // cardMyVoice1.visibility = View.VISIBLE
            } else {
                cardMyVoice.visibility = View.GONE
                //  cardMyVoice1.visibility = View.GONE
            }


            cardMyKnowledge.setOnClickListener {
                (activity as MainActivity).setMenuChecked(1)
                /*AppUtils.addFragment(
                    this!!.activity!!,
                    MyKnowledgeFragment(),
                    false,
                    ""
                )*/
            }

            cardMyServices.setOnClickListener {
                (activity as MainActivity).setMenuChecked(2)
                /* AppUtils.addFragment(
                     this!!.activity!!,
                     MyServiceFragment(),
                     false,
                     ""
                 )*/
            }
            cardMyHealth.setOnClickListener {
                (activity as MainActivity).setMenuChecked(3)
                /*AppUtils.addFragment(
                    this!!.activity!!,
                    MyHealthLandingFragment(),
                    false,
                    ""
                )*/
            }
            cardMyVoice.setOnClickListener {

                var intent = Intent(this.context, beforecall::class.java)
                startActivity(intent)
                /*(activity as MainActivity).setMenuChecked(4)*/

                (activity as MainActivity).setMenuChecked(4)

                /* AppUtils.addFragment(
                     this!!.activity!!,
                     MyVoiceFragment(),
                     false,
                     ""
                 )*/
            }
            lstSRHContent.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
            val contentMasterDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO()
            val listcontentData: List<SRHContent> = contentMasterDAO?.getAllContent()
            val contentAdapter = SRHContentListAdapter(this.activity!!)
            lstSRHContent.adapter = contentAdapter
            contentAdapter.setContentList(listcontentData)


            lstSRHServices.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val myServiceDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myServiceDAO()
            val listLiveMyServiceData: List<MyServiceEntity> = myServiceDAO?.getAllContent()
            val myServiceAdapter =
                MyServiceRecyclerViewAdapter(this.activity!!)
            lstSRHServices.adapter = myServiceAdapter
            myServiceAdapter.setMyServiceList(listLiveMyServiceData)

            setMedicineReminderUI()
            setupAppointmentUI()

            edtSearch.addTextChangedListener((object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    if (TextUtils.isEmpty(p0.toString())) {
                        llDashboard.visibility = View.VISIBLE
                        llSearchResult.visibility = View.GONE
                    } else {
                        llDashboard.visibility = View.GONE
                        llSearchResult.visibility = View.VISIBLE
                        val lstSearch = contentMasterDAO?.getContentListSearchAll(p0.toString())
                        contentAdapter.setContentList(lstSearch)
                        if (lstSearch.isEmpty()) {
                            /*lstSRHContent.visibility = View.GONE
                            txtArticleHeader.visibility = View.GONE*/
                            llArticleContainer.visibility = View.GONE
                        } else {
                            /*lstSRHContent.visibility = View.VISIBLE
                            txtArticleHeader.visibility = View.VISIBLE*/
                            llArticleContainer.visibility = View.VISIBLE
                        }
                        val lstSearchServices = myServiceDAO?.getContentListSearch(p0.toString())
                        myServiceAdapter.setContentList(lstSearchServices)
                        if (!lstSearchServices.isEmpty()) {
                            /*txtHeaderServices.visibility = View.VISIBLE
                            lstSRHServices.visibility = View.VISIBLE*/
                            llServiceContainer.visibility = View.VISIBLE
                        } else {
                            /*txtHeaderServices.visibility = View.GONE
                            lstSRHServices.visibility = View.GONE*/
                            llServiceContainer.visibility = View.GONE
                        }
                        if (lstSearchServices.isEmpty() && lstSearch.isEmpty()) {
                            txtNoItems.visibility = View.VISIBLE
                        } else {
                            txtNoItems.visibility = View.GONE
                        }

                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            }))
            edtSearch.setOnClickListener {
                edtSearch.isCursorVisible = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun setMedicineReminderUI() {
        val medicineReminderDAO =
            MhealthRoomDatabase.getAppDataBase(this!!.activity!!)!!.medicineReminderDAO()
        medicineReminderList = medicineReminderDAO.getAllMedicine()
        lstMedicineReminder.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.HORIZONTAL, false)
        val adapter = MedicineReminderAdapter(activity!!)
        lstMedicineReminder.adapter = adapter
        adapter.setMedicineReminderList(medicineReminderList)

        if (medicineReminderList.isEmpty()) {
            llMyReminder.visibility = View.GONE
        } else {
            llMyReminder.visibility = View.VISIBLE
        }
    }

    fun setupAppointmentUI() {
        val appointmentReminderDAO =
            MhealthRoomDatabase.getAppDataBase(activity!!)!!.appointmentReminderDAO()
        val listAppointments = appointmentReminderDAO.getAllAppointments()
        lstAppointmentReminder.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)

        val adapter = AppointmentReminderAdapter(activity!!)
        lstAppointmentReminder.adapter = adapter
        adapter.setAppointmnetList(listAppointments)

        if (listAppointments.isEmpty()) {
            llMyAppoints.visibility = View.GONE
        } else {
            llMyAppoints.visibility = View.VISIBLE
        }
    }
}