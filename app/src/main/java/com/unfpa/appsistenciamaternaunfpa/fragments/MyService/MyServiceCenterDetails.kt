package com.unfpa.appsistenciamaternaunfpa.fragments.MyService


import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.transition.TransitionManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.transition.Slide
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.CenterDetailsDoctorsRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.CenterDetailsOperationalRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.MyServiceTagsAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.API_Controller
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_my_service_center_details.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 *
 */
class MyServiceCenterDetails : androidx.fragment.app.Fragment() {

    lateinit var field_name: String
    lateinit var nid: String
    lateinit var field_address: String
    lateinit var field_telephone: String
    lateinit var operationalHours: String
    lateinit var field_service_provided: String
    lateinit var field_service_provided_1: String
    lateinit var field_primary_doctor: String
    lateinit var field_primary_nurse: String
    lateinit var field_lat: String
    lateinit var field_long: String
    lateinit var uuid: String
    private val TAG = "PermissionDemo"
    private val CALL_REQUEST_CODE = 102
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_my_service_center_details, container, false)
        setHasOptionsMenu(true)
        try {
            field_name = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_name")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            nid =
                HtmlCompat.fromHtml((this.arguments!!.getString("nid")?.toString()!!), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString()
            field_address = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_address")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_telephone = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_telephone")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            //operationalHours = HtmlCompat.fromHtml((this.arguments!!.getString("operationalHours")?.toString()!!), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            operationalHours = this.arguments!!.getString("operationalHours")?.toString()!!
            field_service_provided = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_service_provided")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_service_provided_1 = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_service_provided_1")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_primary_doctor = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_primary_doctor")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_primary_nurse = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_primary_nurse")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_lat = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_lat")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            field_long = HtmlCompat.fromHtml(
                (this.arguments!!.getString("field_long")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            uuid = HtmlCompat.fromHtml(
                (this.arguments!!.getString("uuid")?.toString()!!),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()

            setServiceTags(rootView)
            setDoctorsNurses(rootView)
            setOperationalHours(rootView)
            val btnFeedback = rootView.findViewById<Button>(R.id.btnFeedback)
            val txtCenterPhone = rootView.findViewById<TextView>(R.id.txtCenterPhone)
            val txtCenterAdderess = rootView.findViewById<TextView>(R.id.txtCenterAdderess)
            val imgShare = rootView.findViewById<ImageView>(R.id.imgShare)
            btnFeedback.setOnClickListener {
                //popUpWindow(rootView)
                API_Controller.getToken(context!!.applicationContext)
                feedbackDialog()
            }
            txtCenterPhone.setOnClickListener {
                callPhone()
            }
            imgShare.setOnClickListener {
                //Logging firebase screen
                AppUtils.trackScreen(Constant.SERVICE_PROVIDER_SHARE, activity as AppCompatActivity)
                shareContent()
            }
            txtCenterAdderess.setOnClickListener {
                //val uri = String.format(Locale.ENGLISH, "geo:%f,%f", field_lat.toDouble(), field_long.toDouble())
                val uri =
                    ("geo:<" + field_lat.toDouble() + ">,<" + field_long.toDouble() + ">?q=<" + field_lat.toDouble() + ">,<" + field_long.toDouble() + ">(" + field_name + ")")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context!!.startActivity(intent)
                /* val uri = String.format(Locale.ENGLISH, "geo:%f,%f", 19.1267, 72.8767)
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                 context!!.startActivity(intent)*/
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.home).isVisible = true
        }
    }

    private fun shareContent() {
        try {
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    activity!!.getString(R.string.information_on_important_services)
                )//Information on important services
                putExtra(
                    Intent.EXTRA_TEXT,
                    activity!!.getString(R.string.information_on_where_you_can_find_imp_services) + "\n \n" +
                            activity!!.getString(R.string.center_name) + field_name + "," + "\n" +
                            activity!!.getString(R.string.center_address_1) + field_address + "," + "\n" +
                            activity!!.getString(R.string.center_telephone_no) + field_telephone + "," + "\n" +
                            activity!!.getString(R.string.center_service_provided) + field_service_provided + "\n" +
                            activity!!.getString(R.string.please_call_the_number_above_for_additional_information)
                )
                // (Optional) Here we're setting the title of the content
                putExtra(Intent.EXTRA_TITLE, activity!!.getString(R.string.sharing_center_details_as_below))
                type = "text/plain"
                // (Optional) Here we're passing a content URI to an image to be displayed
                //setClipData(contentUri);
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callPhone() {
        try {
            //val intent = Intent(Intent.ACTION_CALL);
            //intent.data = Uri.parse("tel:$field_telephone")
            val i = Intent(Intent.ACTION_DIAL)
            val number = field_telephone.replace("-", "")
            i.data = Uri.parse("tel:$number")
            startActivity(i)
            /*if (ContextCompat.checkSelfPermission(activity!!.applicationContext, CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            {
                startActivity(i)
            }
            else
            {
                requestPermissions(arrayOf(CALL_PHONE), CALL_REQUEST_CODE)
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CALL_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!, CALL_PHONE)) {
                        Toast.makeText(
                            activity,
                            activity!!.getString(R.string.permission_has_been_denied_by_user),
                            Toast.LENGTH_SHORT
                        ).show()
                        val builder = AlertDialog.Builder(this.activity!!)
                        builder.setMessage(activity!!.getString(R.string.if_cancel_call))
                            .setTitle(activity!!.getString(R.string.permission_required))
                        builder.setPositiveButton(activity!!.getString(R.string.allow)) { _, _ ->
                            Log.i(TAG, "Clicked Allow")
                            val i = Intent(Intent.ACTION_DIAL)
                            i.data = Uri.parse("tel:$field_telephone")
                            startActivity(i)
                            //makeRequest()
                        }
                        builder.setNegativeButton(activity!!.getString(R.string.cancel_1)) { _, _ ->
                            Log.i(TAG, "Clicked Cancel")
                            //dontAllowLocationPermission = true
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }

    }

    @SuppressLint("WrongConstant")
    private fun setOperationalHours(rootView: View) {
        try {
            val recyclerOperationalHourList: androidx.recyclerview.widget.RecyclerView =
                rootView.findViewById(R.id.recyclerOperationalHourList)
            recyclerOperationalHourList.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context, VERTICAL, false)
            val hoursList = ArrayList<OperationalHours>()
            //operationalHours.replace("\n",",")
            val resultHours: List<String> = operationalHours.split("\n").map { it.trim() }
            for (item in resultHours) {
                var i = item.indexOf(" ")
                var word = item.substring(0, i)
                var rest = item.substring(i + 1).replace("a.m.", "AM")
                var rest1 = rest.replace("p.m.", "PM")
                var rest2 = rest1.replace("–", "  to  ")
                hoursList.add(OperationalHours(word, rest2))
            }

            val adapter =
                CenterDetailsOperationalRecyclerViewAdapter(
                    this.context!!,
                    hoursList
                )
            recyclerOperationalHourList.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setDoctorsNurses(rootView: View) {
        try {
            val recyclerDoctorsList: androidx.recyclerview.widget.RecyclerView =
                rootView.findViewById(R.id.recyclerDoctorsList)
            val linearLayoutDoctorAndNurses: LinearLayout = rootView.findViewById(R.id.LinearLayoutDoctorAndNurses)
            recyclerDoctorsList.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context, HORIZONTAL, false)
            val users = ArrayList<User>()
            if (field_primary_nurse.isNotEmpty() && field_primary_doctor.isNotEmpty()) {
                linearLayoutDoctorAndNurses.visibility = View.VISIBLE
            } else {
                linearLayoutDoctorAndNurses.visibility = View.GONE
            }
            if (field_primary_nurse.isNotEmpty()) {
                if (field_primary_nurse.contains(",")) {
                    val resultNurse: List<String> = field_primary_nurse.split(",").map { it.trim() }
                    for (item in resultNurse) {
                        users.add(User("Nurse", item))
                    }
                } else {
                    users.add(
                        User(
                            "Nurse",
                            field_primary_nurse
                        )
                    )
                }
            }
            if (field_primary_doctor.isNotEmpty()) {
                if (field_primary_doctor.contains(",")) {
                    val resultDoctor: List<String> =
                        field_primary_doctor.split(",").map { it.trim() }
                    for (item in resultDoctor) {
                        users.add(User("Doctor", "Dr. $item"))
                    }
                } else {
                    users.add(
                        User(
                            "Doctor",
                            "Dr. $field_primary_doctor"
                        )
                    )
                }
            }
            if (users.size != 0) {
                val adapter =
                    CenterDetailsDoctorsRecyclerViewAdapter(
                        users
                    )
                recyclerDoctorsList.adapter = adapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setServiceTags(rootView: View) {
        try {


            val linearLayout = rootView.findViewById<LinearLayout>(R.id.DynamicLinearServices)
            val result: List<String> = field_service_provided.split(",").map { it.trim() }
            for (item in result) {
                val textView = TextView(context)
                val layoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //textView.compoundPaddingEnd
                textView.gravity = Gravity.CENTER
                textView.text = item
                layoutParams.setMargins(10, 10, 10, 10)
                textView.layoutParams = layoutParams
                textView.setTextColor(resources.getColor(R.color.white))
                textView.setBackgroundResource(R.drawable.textview_border)
                linearLayout.addView(textView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun feedbackDialog() {
        try {
            val view: View = layoutInflater.inflate(R.layout.fragment_myservice_feedback, null)
            val dialog = activity?.let { Dialog(it) }
            if (dialog != null) {
                dialog.setContentView(view)
            }
            if (dialog != null) {
                dialog.setCancelable(false)
            }
            val btnSubmitFeedback = view.findViewById<Button>(R.id.btnSubmitFeedback)
            val txtCancel = view.findViewById<TextView>(R.id.txtCancel)
            val txtOptionalItalic = view.findViewById<TextView>(R.id.txtOptionalItalic)
            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
            val edtFeedbackText = view.findViewById<EditText>(R.id.edtFeedbackText)

            txtOptionalItalic.setTypeface(txtOptionalItalic.typeface, Typeface.ITALIC)
            btnSubmitFeedback.setOnClickListener {
                //Logging firebase screen
                AppUtils.trackScreen(Constant.SERVICE_PROVIDER_FEEDBACK, activity as AppCompatActivity)

                val rating = ratingBar.rating
                val edtFeedbackTxt = edtFeedbackText.text
                //if(edtFeedbackTxt.toString().length <= 250 ){
                //PostJsonReqObj.FeedbackPostJsonReqObj(context!!.applicationContext,feedbackToken,edtFeedbackTxt.toString() ,rating.toString(),field_name,field_service_provided,uuid)

                val Main = JSONObject()
                //_embedded
                val _embedded = JSONObject()
                val _embedded_1 = JSONArray()
                val _embedded_2 = JSONArray()
                val MayBeCenterUid: String = "http://mhealth-dev-01.unfpa.org/mhealth/node/$nid?_format=hal_json"
                val _embedded_1_0 = get_embedded_0_obj(
                    MayBeCenterUid,
                    "http://mhealth-dev-01.unfpa.org/mhealth/type/node/srh_service_provider",
                    uuid
                )
                _embedded_1_0.put("lang", "en")
                _embedded_1.put(_embedded_1_0)
                _embedded.put(
                    "http://mhealth-dev-01.unfpa.org/mhealth/rest/relation/node/srh_provider_feedback/field_provider",
                    _embedded_1
                )
                val resultServiceProviderID: List<String> = field_service_provided_1.split(",").map { it.trim() }
                for (item in resultServiceProviderID) {
                    //http://mhealth-dev-01.unfpa.org/mhealth/node/22?_format=hal_json
                    val myServiceDAO =
                        MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myServiceDAO()
                    val uuidOfServiceProvider: String = myServiceDAO?.getServiceUuid(item)
                    val tempUrl: String = "http://mhealth-dev-01.unfpa.org/mhealth/node/$item?_format=hal_json"
                    val uuid: String = "bd3d931d-bf32-4017-9544-acd58456e6b9" //db madhun ghyaychay
                    val type: String =
                        "http://mhealth-dev-01.unfpa.org/mhealth/rest/type/node/srh_service_type"// common no need to change
                    _embedded_2.put(get_embedded_0_obj(tempUrl, type, uuidOfServiceProvider))
                }
                _embedded.put(
                    "http://mhealth-dev-01.unfpa.org/mhealth/rest/relation/node/srh_provider_feedback/field_service_provided",
                    _embedded_2
                )

                //_links object created
                val _links = JSONObject()
                val _links_Obj_type = JSONObject()
                val _links_Obj_1 = JSONObject()
                val _links_Arr_1 = JSONArray()

                _links_Obj_type.put(
                    "href",
                    "http://mhealth-dev-01.unfpa.org/mhealth/rest/type/node/srh_provider_feedback"
                )
                _links.put("type", _links_Obj_type)
                _links_Obj_1.put("href", MayBeCenterUid)
                _links.put(
                    "http://mhealth-dev-01.unfpa.org/mhealth/rest/relation/node/srh_provider_feedback/field_provider",
                    _links_Obj_1
                )
                for (item in resultServiceProviderID) {
                    val tempUrl: String = "http://mhealth-dev-01.unfpa.org/mhealth/node/$item?_format=hal_json"
                    val newObj = JSONObject()
                    newObj.put("href", tempUrl)
                    _links_Arr_1.put(newObj)
                }
                _links.put(
                    "http://mhealth-dev-01.unfpa.org/mhealth/rest/relation/node/srh_service_provider/field_service_provided",
                    _links_Arr_1
                )

                //title array
                val title = JSONArray()
                val title_Obj_0 = JSONObject()
                title_Obj_0.put("value", field_name)
                title.put(title_Obj_0)

                //field comments
                val field_comments = JSONArray()
                val field_comments_0 = JSONObject()
                field_comments_0.put("value", edtFeedbackTxt)
                field_comments_0.put("format", "plain_text")
                field_comments.put(field_comments_0)


                //field feedback rating stars
                val field_feedback = JSONArray()
                val field_feedback_0 = JSONObject()
                field_feedback_0.put("value", rating)
                field_feedback_0.put("format", "integer")
                field_feedback.put(field_feedback_0)

                Main.put("_links", _links)
                Main.put("title", title)
                Main.put("field_comments", field_comments)
                Main.put("field_feedback", field_feedback)
                Main.put("_embedded", _embedded)

                Log.i("JSONRequestFeedback", Main.toString())
                API_Controller.postFeedbackRating(context!!.applicationContext, Main)

                /*}else{*/
                if (dialog != null) {
                    dialog.dismiss()
                }
                Toast.makeText(activity, "submitted", Toast.LENGTH_SHORT).show()
                //}
            }
            txtCancel.setOnClickListener {
                if (dialog != null) {
                    dialog.dismiss()
                }
            }
            if (dialog != null) {
                dialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun get_embedded_0_obj(self: String, type: String, uuid: String): JSONObject {
        val _embedded_1_0 = JSONObject()
        val _embedded_1_0_links_obj = JSONObject()
        val _embedded_1_0_links_obj_self = JSONObject()
        val _embedded_1_0_links_obj_type = JSONObject()
        _embedded_1_0_links_obj_self.put(
            "href",
            self //http://mhealth-dev-01.unfpa.org/mhealth/rest/relation/node/srh_provider_feedback/field_service_provided
        )
        _embedded_1_0_links_obj_type.put(
            "href",
            type //http://mhealth-dev-01.unfpa.org/mhealth/type/node/srh_service_provider
        )
        _embedded_1_0_links_obj.put("self", _embedded_1_0_links_obj_self)
        _embedded_1_0_links_obj.put("type", _embedded_1_0_links_obj_type)
        _embedded_1_0.put("_link", _embedded_1_0_links_obj)
        val _embedded_1_0_uuid = JSONArray()
        val _embedded_1_0_uuid_0 = JSONObject()
        _embedded_1_0_uuid_0.put("value", uuid)//bd3d931d-bf32-4017-9544-acd58456e6b9
        _embedded_1_0_uuid.put(_embedded_1_0_uuid_0)

        _embedded_1_0.put("uuid", _embedded_1_0_uuid)
        return _embedded_1_0
    }

    private fun popUpWindow(rootView: View) {
        try {

            //rootView.background = this.context?.let { ContextCompat.getDrawable(it, R.color.transparent) }
            val view: View = layoutInflater.inflate(R.layout.fragment_myservice_feedback, null)
            val popupWindow =
                PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            rootView.alpha = 0.3F
            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }
            // If API level 23 or higher then execute the code
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn
                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

            }
            val buttonPopup = view.findViewById<Button>(R.id.btnSubmitFeedback)
            val txtCancel = view.findViewById<TextView>(R.id.txtCancel)

            buttonPopup.setOnClickListener {
                popupWindow.dismiss()
                Toast.makeText(activity, "submitted", Toast.LENGTH_SHORT).show()
                rootView.alpha = 1F
            }
            txtCancel.setOnClickListener {
                popupWindow.dismiss()
                Toast.makeText(activity, "Popup cancelled", Toast.LENGTH_SHORT).show()
                rootView.alpha = 1F
            }
            /*popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true*/
            popupWindow.setOnDismissListener {

            }
            TransitionManager.beginDelayedTransition(root_layout)

            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            val chipsLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager =
                ChipsLayoutManager.newBuilder(context)
                    .setChildGravity(Gravity.CENTER_HORIZONTAL)
                    .setScrollingEnabled(true)
                    .setGravityResolver { Gravity.CENTER }
                    .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .withLastRow(true)
                    .build()
            lstTags!!.layoutManager = chipsLayoutManager
            ViewCompat.setLayoutDirection(lstTags, ViewCompat.LAYOUT_DIRECTION_LTR)
            val topicsAdapter =
                MyServiceTagsAdapter(this.activity!!)
            lstTags.adapter = topicsAdapter
            if (field_service_provided != "")
                topicsAdapter.setTopicList(field_service_provided.split(","))
            else {
                //llHeaderTopicList.visibility = View.GONE
                //lstTags.visibility = View.GONE
                //topicsAdapter.setTopicList(emptyList())
            }

            //updateToolbar()
            (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.center_details)
            txtCenterName.text = field_name
            txtCenterAdderess.text = field_address
            txtCenterPhone.text = field_telephone
            txtCenterAdderess.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            txtCenterPhone.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            //txtOperationalHour.text = operationalHours
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

data class User(val profession: String, val name: String)
data class OperationalHours(val day: String, val timing: String)
