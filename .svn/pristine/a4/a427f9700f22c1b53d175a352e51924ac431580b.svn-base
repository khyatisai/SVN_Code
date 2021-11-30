package com.unfpa.appsistenciamaternaunfpa.fragments.my_voice


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.API_Controller
import com.unfpa.appsistenciamaternaunfpa.database.DBHelper
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_voice_share_story.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class MyVoiceShareStoryFragment : androidx.fragment.app.Fragment() {
    private var myVoiceStoryMode: String = ""
    private var myVoiceUniqueId: String = ""
    private var myVoiceTitle: String = ""
    private var myVoiceStory: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_voice_share_story, container, false)
        setHasOptionsMenu(true)
        try {
            myVoiceStoryMode = HtmlCompat.fromHtml(
                (this.arguments!!.getString("story_mode")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            myVoiceUniqueId = HtmlCompat.fromHtml(
                (this.arguments!!.getString("unique_id")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            myVoiceTitle = HtmlCompat.fromHtml(
                (this.arguments!!.getString("title")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            myVoiceStory = HtmlCompat.fromHtml(
                (this.arguments!!.getString("story")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            if (myVoiceTitle.isNotEmpty()) {
                val edtTitle: EditText = rootView.findViewById(R.id.edtTitle) as EditText
                val edtTellYourStory: EditText = rootView.findViewById(R.id.edtTellYourStory) as EditText
                edtTitle.setText(myVoiceTitle)
                edtTellYourStory.setText(myVoiceStory)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rootView
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.menu_my_voice)
        try {
            val myVoiceDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myVoiceDAO()
            val listLiveMyVoiceData: List<MyVoiceEntity> = myVoiceDAO?.getAllContent()
            val id: Int = listLiveMyVoiceData.count()
            val edtTitle: EditText = view.findViewById(R.id.edtTitle) as EditText
            val edtTellYourStory: EditText = view.findViewById(R.id.edtTellYourStory) as EditText
            btnSubmitStory.setOnClickListener {
                if (edtTitle.text.toString().isNotEmpty() && edtTellYourStory.text.toString().isNotEmpty()) {
                    //Logging firebase screen
                    AppUtils.trackScreen(Constant.STORY_SUBMIT, activity as AppCompatActivity)
                    val incrementedID: String = (id + 1).toString()
                    if (myVoiceUniqueId == "") {
                        addStoryToDatabase(
                            incrementedID,
                            edtTitle.text.toString(),
                            edtTellYourStory.text.toString(),
                            "ticked"
                        )
                        addToTheServer(edtTitle.text.toString(), edtTellYourStory.text.toString())
                    } else {
                        addStoryToDatabase(
                            myVoiceUniqueId,
                            edtTitle.text.toString(),
                            edtTellYourStory.text.toString(),
                            "ticked"
                        )
                        addToTheServer(edtTitle.text.toString(), edtTellYourStory.text.toString())
                    }
                } else {
                    Toast.makeText(
                        activity,
                        activity!!.getString(R.string.title_and_story_should_not_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            btnSaveAsDraft.setOnClickListener {
                if (edtTitle.text.toString().isNotEmpty() && edtTellYourStory.text.toString().isNotEmpty()) {
                    val incrementedID: String = (id + 1).toString()
                    if (myVoiceUniqueId == "") {
                        addStoryToDatabase(
                            incrementedID,
                            edtTitle.text.toString(),
                            edtTellYourStory.text.toString(),
                            "edit"
                        )
                    } else {
                        addStoryToDatabase(
                            myVoiceUniqueId,
                            edtTitle.text.toString(),
                            edtTellYourStory.text.toString(),
                            "edit"
                        )
                    }
                } else {
                    Toast.makeText(
                        activity,
                        activity!!.getString(R.string.title_and_story_should_not_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addToTheServer(header: String, story: String) {
        /*
        {
          "title": {
            "value": "My SRH Voice"
          },
          "field_description": {
            "value": "My SRH Voice",
            "format": "plain_text"
          },
          "field_short_description": {
            "value": "My SRH Voice",
            "format": "plain_text"
          }
          "field_mode": {
            "value": "edit",
            "format": "plain_text"
          }
          "field_id": {
            "value": "1",
            "format": "plain_text"
          }
       }
*/
        try {
            val Main = JSONObject()
            val title = JSONObject()
            title.put("value", header)
            val field_description = JSONObject()
            field_description.put("value", story)
            field_description.put("format", "plain_text")
            val field_short_description = JSONObject()
            field_short_description.put("value", story)
            field_short_description.put("format", "plain_text")

            Main.put("title", title)
            Main.put("field_description", field_description)
            Main.put("field_short_description", field_short_description)
            API_Controller.postMyVoiceStory(context!!.applicationContext, Main)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addStoryToDatabase(unique_id: String, title: String, story: String, story_mode: String) {
        try {
            var frag = MyVoiceFragment()
            var bundle = Bundle()
            //bundle.putString("nid", myServiceCenterList.nid)
            //bundle.putString("myServiceId", myServiceId)
            val storeArray = JSONArray()
            val childObj = JSONObject()
            childObj.put("unique_id", unique_id)
            childObj.put("title", title)
            childObj.put("story", story)
            childObj.put("story_mode", story_mode)
            storeArray.put(childObj)
            val myVoiceList: List<MyVoiceEntity> =
                Gson().fromJson(storeArray.toString(), Array<MyVoiceEntity>::class.java).toList()
            if (myVoiceUniqueId == "") {
                DBHelper.insertMVoiceDetails(myVoiceList, activity!!)//.applicationContext!!
            } else {
                DBHelper.updateMVoiceDetails(myVoiceList, activity!!)//.applicationContext!!
            }

            AppUtils.addFragment(activity as AppCompatActivity, frag, false, "")
        } catch (e: Exception) {
            e.printStackTrace()
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
