package com.unfpa.appsistenciamaternaunfpa.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainPregnantActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.AppoitmentCalendarPatientActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.ListDoctorsActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteMasInformacionActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant.ChatBotActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.dialog_confirmation_permission.view.*
import kotlinx.android.synthetic.main.fragment_home_pregnant.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * Created by KhyatiShah on 8/16/2019.
 */
class HomePregnantFragment : androidx.fragment.app.Fragment() {
    private val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
    private val requestcode = 1
    lateinit var medicineReminderList: List<MedicineReminder>

    val APPOINTMENT = EndPoints.URL_HEROKU + "/api/v1/appointment/getbypatient"

    var nameDoctor = ""
    var horas = ""
    var dateInSpanishForIntent = ""
    var idCita = ""
    var pathologicalAntecedents = ""
    var treatmentsReceived = ""
    var medicalObservations = ""
    var note = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_pregnant, container, false)

        Firebase.initialize(this.context as Activity)
        if (!isPermissionGranted()) {
            askPermissions()
        }

        var getMyUserId = AppUtils.getDataUser(this.context as Activity)


        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        val date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val hm = "$hour:$minute:$second"

        val jsonobj = JSONObject()

        jsonobj.put("patient", getMyUserId)
        jsonobj.put("today", date)
        jsonobj.put("hour", hm)

        val que = Volley.newRequestQueue(this.context as Activity)
        val req = JsonObjectRequest(
            Request.Method.POST, APPOINTMENT, jsonobj,
            Response.Listener { response ->
                var test = response.getString("message")
                if(response.getString("message") != "null"){
                    var data = response.getJSONObject("data")
                    var doctor = data.getJSONObject("doctor")
                    var paciente = data.getJSONObject("patient")
                    var users = paciente.getJSONObject("user")

                    pathologicalAntecedents = paciente.getString("pathologicalAntecedents")
                    treatmentsReceived = paciente.getString("treatmentsReceived")
                    medicalObservations = paciente.getString("medicalObservations")

//                    var userId = users.getString("id")
//                    var nombreCompleto = users.getString("firstname") + " "+users.getString("lastname")
                    val localeSpanish = Locale("es", "ES")
                    nameDoctor = doctor.getString("firstname") + " "+doctor.getString("lastname")
                    horas = data.getString("hour")
                    var fechaAppointment = data.getString("date")
                    var appointmentType = data.getString("typeAppointment")

                    idCita = data.getString("id")
                    note = data.getString("note")


                    val readerFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SimpleDateFormat("yyyy-MM-dd", localeSpanish)
                    } else {
                        TODO("VERSION.SDK_INT < N")
                    }
                    val writerFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SimpleDateFormat("E d 'de' MMMM 'del' yyyy", localeSpanish)
                    } else {
                        TODO("VERSION.SDK_INT < N")
                    }
                    val readDate: Date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        readerFormatter.parse(fechaAppointment)
                    } else {
                        TODO("VERSION.SDK_INT < N")
                    }
                    val dateInSpanish: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        writerFormatter.format(readDate)
                    } else {
                        TODO("VERSION.SDK_INT < N")
                    }

                    dateInSpanishForIntent = dateInSpanish

                    val hor12hr = horas
                    rootView.findViewById<TextView>(R.id.txHour).setText(AppUtils.getTime12HourFormat(hor12hr))
                    rootView.findViewById<TextView>(R.id.txtDate).setText(dateInSpanish)
                    rootView.findViewById<TextView>(R.id.txtName).setText("Doctor $nameDoctor")
                    rootView.findViewById<TextView>(R.id.appointmentType)
                        .setBackgroundResource(R.drawable.text_view_border_black)
                    rootView.findViewById<TextView>(R.id.appointmentType).setText("De $appointmentType")
                }

            },
            Response.ErrorListener {
                    error ->
                println(error)
            })
        que.add(req)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

            btnMasInfoPac.setOnClickListener{
                if(idCita != ""){
                    var intent = Intent(activity, PacienteMasInformacionActivity::class.java)
                    intent.putExtra("nameDoctor", nameDoctor)
                    intent.putExtra("horas", horas)
                    intent.putExtra("dateInSpanishForIntent", dateInSpanishForIntent)
                    intent.putExtra("pathologicalAntecedents", pathologicalAntecedents)
                    intent.putExtra("treatmentsReceived", treatmentsReceived)
                    intent.putExtra("medicalObservations", medicalObservations)
                    intent.putExtra("note", note)

                    startActivity(intent)
                }else{
                    val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                    context?.let { it1 ->
                        AppUtils.createCustomToast("No tiene cita programada", it1, layoutToast,"success")
                    }
                }
            }

            var getMyUserId = AppUtils.getDataUser(this.context as Activity)

//            firebaseRef.child(getMyUserId).child("incoming").addValueEventListener(object:
//                ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {}
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        onCallRequest(snapshot.value as? String)
//                    }
//
//
//                }
//            })

            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
            val sharedPreference1 = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

//            callvideo.setOnClickListener {
//                var intent =  Intent(activity, call::class.java)
//                intent.putExtra("username", getMyUserId)
//                intent.putExtra("calluser", "")
//                startActivity(intent)
//            }
//            cardInformacion.setOnClickListener {
//                val intent = Intent(activity, InformacionActivity::class.java)
//                startActivity(intent)
//            }

            callvideopregnant.setOnClickListener {
                val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                context?.let { it1 ->
                    AppUtils.createCustomToast("Recibirá la llamada en el horario programado", it1, layoutToast,"success")
                }
            }

            cardInformacion.setOnClickListener {
                (activity as MainPregnantActivity).setMenuChecked(1)
                /*AppUtils.addFragment(
                    this!!.activity!!,
                    MyKnowledgeFragment(),
                    false,
                    ""
                )*/
            }
            cardAsistenteVirtual.setOnClickListener {
                val intent = Intent(activity, ChatBotActivity::class.java)
                startActivity(intent)
            }

            cardCalendarCitas.setOnClickListener {
                val intent = Intent(activity, AppoitmentCalendarPatientActivity::class.java)
                startActivity(intent)
            }

            cardMessage.setOnClickListener{
               val intent = Intent(activity, ListDoctorsActivity::class.java)
                startActivity(intent)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    var uniqueId = ""
//    private fun initializePeer(){
//        var firebaseRef = Firebase.database.getReference("users")
//        uniqueId = getUniqueID()
//        var getMyUserId = AppUtils.getDataUser(this.context as Activity)
//        firebaseRef.child(getMyUserId).child("incoming").addValueEventListener(object:
//            ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {}
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                onCallRequest(snapshot.value as? String)
//            }
//        })
//
//    }

//    private fun onCallRequest(caller:String?){
//        if(caller == null) return
//        var firebaseRef = Firebase.database.getReference("users")
//        callLayout.visibility = View.VISIBLE
//        incomingCallTxt.text = "$caller is calling..."
//        acceptBtn.setOnClickListener{
////            firebaseRef.child(username).child("connId").setValue(uniqueId)
////            firebaseRef.child(username).child("isAvailable").setValue(true)
//            callLayout.visibility = View.GONE
//
//        }
//        rejectBtn.setOnClickListener{
////            firebaseRef.child(username).child("incoming").setValue(null)
//            callLayout.visibility = View.GONE
//        }


//        rejectBtn2.setOnClickListener{
//            this.onDestroy()
//
//        }
//    }

    private fun askPermissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this.context as Activity,permissions[0]) && ActivityCompat.shouldShowRequestPermissionRationale(this.context as Activity,permissions[1])){
//            Toast.makeText(this.context, "probando 1", Toast.LENGTH_LONG).show()
            // usuario ya rechazo permisos
            ActivityCompat.requestPermissions(this.context as Activity, permissions, requestcode)

        }else{
            ActivityCompat.requestPermissions(this.context as Activity, permissions, requestcode)
        }
    }


    private fun isPermissionGranted(): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(
                    this.context as Activity,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            )
                return false;
        }
        return true
    }

    fun startInstalledAppDetailsActivity(context: Activity?) {
        if (context == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(i)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0 -> {

                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this.context, "probando 0", Toast.LENGTH_LONG).show()
                }else{

                    val modalPermissionVerification = LayoutInflater.from(this.context).inflate(R.layout.dialog_confirmation_permission, null)
                    val mbuilder = AlertDialog.Builder(this.context).setView(modalPermissionVerification).setTitle("permissions")
                    val mDialog = mbuilder.show()
                    modalPermissionVerification.btn_ok.setOnClickListener{
                        startInstalledAppDetailsActivity(this.context as Activity)
                    }
                    modalPermissionVerification.btn_cancel.setOnClickListener{
                        mDialog.dismiss()
                    }
                }
            }
            1 -> {

                if(grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this.context, "probando 1", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this.context, "probando 4", Toast.LENGTH_LONG).show()
                    val modalPermissionVerification = LayoutInflater.from(this.context).inflate(R.layout.dialog_confirmation_permission, null)
                    val mbuilder = AlertDialog.Builder(this.context).setView(modalPermissionVerification).setTitle("permissions")
                    val mDialog = mbuilder.show()
                    modalPermissionVerification.btn_ok.setOnClickListener{
                        startInstalledAppDetailsActivity(this.context as Activity)
                    }
                    modalPermissionVerification.btn_cancel.setOnClickListener{
                        mDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun getUniqueID(): String{
        return UUID.randomUUID().toString()
    }

//    override fun onDestroy() {
//        var getMyUserId = AppUtils.getDataUser(this.context as Activity)
//        firebaseRef.child(getMyUserId).setValue(null)
//        webView.loadUrl("about:blank")
//        super.onDestroy()
//    }

}