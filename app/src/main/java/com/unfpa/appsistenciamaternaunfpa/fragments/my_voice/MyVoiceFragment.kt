package com.unfpa.appsistenciamaternaunfpa.fragments.my_voice


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.Toast

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_voice.MyVoiceRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat.ChatPeopleFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_voice.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 */
class MyVoiceFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_voice, container, false)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.menu_my_voice)

            val sharedPreference1 =
                activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString("CountryCode", "")
            val countryOfficeDAO =
                MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.countryOfficeDAO()
            val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode.toString())
            // val field_enable_live_chat: String = countryCodeRequest.field_enable_live_chat
            //   val field_contact_number: String = countryCodeRequest.field_contact_number
            val field_enable_live_chat = "On"
            val field_contact_number = "123456789"
            val switchMyVoiceCall = sharedPreference1.getBoolean("switchMyVoiceCall", true)
            val switchMyVoiceChat = sharedPreference1.getBoolean("switchMyVoiceChat", true)

            if (field_contact_number == "") {
                btnCall.visibility = View.GONE
            } else {
                if (switchMyVoiceCall) {
                    btnCall.visibility = View.VISIBLE
                } else {
                    btnCall.visibility = View.GONE
                }

            }


            if (field_enable_live_chat == "On") {
                if (switchMyVoiceChat) {
                    btnChat.visibility = View.VISIBLE
                } else {
                    btnChat.visibility = View.GONE
                }
            } else {
                btnChat.visibility = View.GONE
            }


            btnShareStory.setOnClickListener {
                var frag = MyVoiceShareStoryFragment()
                AppUtils.addFragment(activity as AppCompatActivity, frag, true, "")
            }
            btnChat.setOnClickListener {
                if (AppUtils.isConnectingToInternet(this.activity!!)) {
                    //Logging firebase screen
                    AppUtils.trackScreen(Constant.CHAT_KNOWLEDGEABLE_PERSON, activity as AppCompatActivity)
                    //Toast.makeText(activity, activity!!.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                    AppUtils.addFragment(activity as AppCompatActivity, ChatPeopleFragment(), true, "")
                } else {
                    Toast.makeText(
                        this.activity,
                        activity!!.getString(R.string.please_connect_to_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            btnCall.setOnClickListener {
                //Logging firebase screen
                AppUtils.trackScreen(Constant.CALL_KNOWLEDGEABLE_PERSON, activity as AppCompatActivity)
                callPhone(field_contact_number)
            }
            cardStoryEmpty.setOnClickListener {
                var frag = MyVoiceShareStoryFragment()
                AppUtils.addFragment(activity as AppCompatActivity, frag, true, "")
            }
            getVoiceList(view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callPhone(field_contact_number: String) {
        try {
            val i = Intent(Intent.ACTION_DIAL)
            val number = "0123456789"
            i.data = Uri.parse("tel:$field_contact_number")
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getVoiceList(view: View) {
        try {
            recyclerViewCenterListLocation.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val myVoiceDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myVoiceDAO()
            val listLiveMyVoiceData: List<MyVoiceEntity> = myVoiceDAO?.getAllContent()
            //recyclerMyServiceList.adapter =

            val myVoiceAdapter = MyVoiceRecyclerViewAdapter(this.activity!!)
            recyclerViewCenterListLocation.adapter = myVoiceAdapter
            myVoiceAdapter.setMyVoiceList(listLiveMyVoiceData)

            if (listLiveMyVoiceData.isNotEmpty()) {
                cardStoryEmpty.visibility = View.GONE
                recyclerViewCenterListLocation.visibility = View.VISIBLE
                btnShareStory.visibility = View.VISIBLE
            } else {
                cardStoryEmpty.visibility = View.VISIBLE
                recyclerViewCenterListLocation.visibility = View.GONE
                btnShareStory.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(): MyVoiceFragment {
            return MyVoiceFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

}
