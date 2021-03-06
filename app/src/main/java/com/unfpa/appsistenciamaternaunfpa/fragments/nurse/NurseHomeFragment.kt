package com.unfpa.appsistenciamaternaunfpa.fragments.nurse

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.AppoitmentCalendarDoctorActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteDetalleActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.PatientActivityNurse
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.PatientDetailActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.appointment.AppointmentCalenderNurseActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.chat.ListPacienteActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterAppointment
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.call
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.dialog_confirmation_permission.view.*
import kotlinx.android.synthetic.main.fragment_home_doctor.*
import kotlinx.android.synthetic.main.fragment_nurse_home.*
import kotlinx.android.synthetic.main.fragment_nurse_home.btMasInformacion
import kotlinx.android.synthetic.main.fragment_nurse_home.callvideo
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by KhyatiShah on 8/26/2021.
 */
class NurseHomeFragment : androidx.fragment.app.Fragment() {

    private val permissions =
        arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
    private val requestcode = 1
    lateinit var medicineReminderList: List<MedicineReminder>
    val APPOINTMENT = EndPoints.URL_HEROKU + "/api/v1/appointment/nurse/getbyday"
    var pacientId = ""
    var IdAppointment = ""
    var firebaseRef = Firebase.database.getReference("users")
    var birth = ""
    var gestationWeeks = ""
    var gestationWeeksDate = ""
    var fechaApp = ""

    //var getAppByNurse = EndPoints.URL_HEROKU + "/api/v1/appointment/getbynurse"
    var getAppByNurse = EndPoints.URL_HEROKU + "/api/v1/appointment/getbynursefordate"
    val jsonobj = JSONObject()

    var HoraCitaPac: ArrayList<String> = ArrayList()
    var NombreCompletoPac: ArrayList<String> = ArrayList()
    var ListCancelada: ArrayList<String> = ArrayList()
    var nombreCompleto = ""
    var doctorName = ""
    private var horas = ""
    var getMyUserId = ""

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CustomAdapterAppointment.MyViewHolder>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Obtiene contexto.
        val appContext = requireContext().applicationContext
        if (!AppUtils.isConnectingToInternet(requireContext().applicationContext)) {
            val layoutToast =
                layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
            context?.let { it1 ->
                AppUtils.createCustomToast(
                    "Porfavor revisa tu conexion a internet",
                    it1,
                    layoutToast,
                    "warning"
                )
            }
        }

        val rootView = inflater.inflate(R.layout.fragment_nurse_home, container, false)

        val recyclerMyAppointment = rootView.findViewById<RecyclerView>(R.id.recyclerMyAppointment)
        val linearLayoutManagerApp = LinearLayoutManager(requireContext())
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp
        /*****Get Appointment by toDay*****/
        /*****Get Appointment by toDay*****/
        /*****Get Appointment by toDay*****/
        getMyUserId = AppUtils.getDataUser(this.context as Activity)
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        val date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
        val hm = "$hour:$minute:$second"

        //val time2 = LocalTime.of(hour, minute, second)
        val sdf = SimpleDateFormat("HH:mm:ss")
        val time = sdf.format(c.time)
        println(time)

        val jsonobj = JSONObject()
        jsonobj.put("nurseid", getMyUserId)
        jsonobj.put("today", date)
        jsonobj.put("hour", time)

        val que = Volley.newRequestQueue(this.context as Activity)
        val req = JsonObjectRequest(
            Request.Method.POST, APPOINTMENT, jsonobj,
            Response.Listener { response ->

                var test = response.getString("message")

                if (response.getString("message") != "error") {
                    /*var data = response.getJSONObject("data")
                    var paceinte = data.getJSONObject("patient")
                    var users = paceinte.getJSONObject("user")
                    pacientId = users.getString("id")
                    IdAppointment = data.getString("id")
                    birth = users.getString("birth")
                    gestationWeeks = paceinte.getString("gestationWeeks")
                    gestationWeeksDate = paceinte.getString("gestationWeeksDate")


                    nombreCompleto =
                        users.getString("firstname") + " " + users.getString("lastname")

                    horas = data.getString("hour")
                    fechaApp = data.getString("date")

                    val TypeAppointment = data.getString("typeAppointment")
                    var TypeAppointmentStr = "Tipo de cita"


                    if (TypeAppointment == "presencial")
                        TypeAppointmentStr = "Consulta Presencial"
                    else
                        TypeAppointmentStr = "Teleconsulta M??dica"

                    rootView.findViewById<TextView>(R.id.txHour)
                        .setText(AppUtils.getTime12HourFormat(horas))
                    rootView.findViewById<TextView>(R.id.txtDate).setText(fechaApp)
                    rootView.findViewById<TextView>(R.id.txtName)
                        .setText(nombreCompleto.capitalize(Locale.ROOT))
                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
                        .setBackgroundResource(R.drawable.text_view_border)
                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
                        .setText(TypeAppointmentStr)*/
                    var data = response.getJSONArray("data").getJSONObject(0)
                    pacientId = data.getString("user_id")
                    //pacientId = data.getString("patient_id")

                    IdAppointment = data.getString("appointment_id")
                    birth = data.getString("user_birth")
                    gestationWeeks = data.getString("patient_gestationWeeks")
                    gestationWeeksDate = data.getString("patient_gestationWeeksDate")


                    nombreCompleto =
                        data.getString("user_firstname") + " " + data.getString("user_lastname")

                    horas = data.getString("appointment_hour")
                    fechaApp = data.getString("appointment_date")
                    doctorName = data.getString("doctorname")
                    val TypeAppointment = data.getString("appointment_typeAppointment")
                    var TypeAppointmentStr = "Tipo de cita"


                    if (TypeAppointment == "presencial")
                        TypeAppointmentStr = "Consulta Presencial"
                    else
                        TypeAppointmentStr = "Teleconsulta M??dica"

                    rootView.findViewById<TextView>(R.id.txHour)
                        .setText(AppUtils.getTime12HourFormat(horas))
                    rootView.findViewById<TextView>(R.id.txtDate).setText(fechaApp)
                    rootView.findViewById<TextView>(R.id.txtName)
                        .setText(nombreCompleto.capitalize(Locale.ROOT))
                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
                        .setBackgroundResource(R.drawable.text_view_border)
                    rootView.findViewById<TextView>(R.id.txTypeAppointment)
                        .setText(TypeAppointmentStr)
                }
            },
            Response.ErrorListener { error ->
                println(error)
            })
        que.add(req)

        /******Get all Appointment by doctor******/
        /******Get all Appointment by doctor******/
        /******Get all Appointment by doctor******/
        val dateToFormatStr =
            DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        val dateTimeStr = AppUtils.dateConverter("dd-MM-yyyy", "yyyy/MM/dd", dateToFormatStr)
        val jsonobj2 = JSONObject()

        adapter = null

        jsonobj2.put("nurseid", getMyUserId)
        jsonobj2.put("today", dateTimeStr)

        try {
            val quee = Volley.newRequestQueue(this.context as Activity)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getAppByNurse, jsonobj2,
                Response.Listener { response ->


                    var dataArray = response.getJSONArray("data")
                    var test = dataArray.length()

                    for (i in 0 until dataArray.length()) {
                        val userDetail = dataArray.getJSONObject(i)
                        val horaFormat = AppUtils.getTime12HourFormat(
                            dataArray.getJSONObject(i).getString("hour")
                        )
                        HoraCitaPac.add(horaFormat)
                        var nombrecomplet =
                            dataArray.getJSONObject(i)
                                .getString("firstname") + " " + dataArray.getJSONObject(
                                i
                            ).getString("lastname")
                        NombreCompletoPac.add(nombrecomplet)
                        val cancelada = dataArray.getJSONObject(i).getString("cancel")
                        if (cancelada == "1") {
                            ListCancelada.add("true")
                        } else {
                            ListCancelada.add("false")
                        }


                    }

                    val customAdapter = CustomAdapterAppointment(
                        activity,
                        HoraCitaPac,
                        NombreCompletoPac,
                        ListCancelada
                    )
                    recyclerMyAppointment.adapter = customAdapter

                    recyclerMyAppointment.adapter?.notifyDataSetChanged()

                    // only create and set a new adapter if there isn't already one
//                    if (adapter !== null) {
//
//                        adapter = CustomAdapterAppointment(this.context as Activity, HoraCitaPac, NombreCompletoPac)
//                        recyclerMyAppointment.setAdapter(adapter);
//                    }


                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            cardNursePaciente.setOnClickListener {
                val intent = Intent(activity, PatientActivityNurse::class.java)
                startActivity(intent)
            }

            Firebase.initialize(this.context as Activity)

            if (!isPermissionGranted()) {
                askPermissions()
            }

            btMasInformacion.setOnClickListener {
                if (pacientId != "") {
                    var intent = Intent(activity, PatientDetailActivity::class.java)
                    intent.putExtra("idUserPac", pacientId)
                    startActivity(intent)
                } else {
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
                    context?.let { it1 ->
                        AppUtils.createCustomToast(
                            "No tiene cita programada",
                            it1,
                            layoutToast,
                            "success"
                        )
                    }
                }
            }
            cardCalendarNurse.setOnClickListener {

                val intent = Intent(activity, AppointmentCalenderNurseActivity::class.java)
                startActivity(intent)
            }

            cardNurseMensaje.setOnClickListener {
                var intent = Intent(this.context, ListPacienteActivity::class.java)
                startActivity(intent)
            }

            callvideo.setOnClickListener {
                if (!isPermissionGranted()) {
                    askPermissions()
                } else {
                    if (pacientId != "") {
                        var getMyUserId = AppUtils.getDataUser(this.context as Activity)

                        firebaseRef.child(pacientId).child("connId")
                            .addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {}

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value == null) {
                                        val dateFormat = SimpleDateFormat("hh:mm a")
                                        val formattedDate = dateFormat.format(Date()).toString()
                                        AppUtils.setCallTime(activity!!, formattedDate, "Start")
                                        var intent = Intent(activity, call::class.java)
                                        intent.putExtra("username", getMyUserId)
                                        intent.putExtra("calluser", pacientId)
                                        intent.putExtra("doctor", "")
                                        intent.putExtra("IdAppointment", IdAppointment)
                                        intent.putExtra("birth", birth)
                                        intent.putExtra("gestationWeeks", gestationWeeks)
                                        intent.putExtra("gestationWeeksDate", gestationWeeksDate)
                                        intent.putExtra("uniqueId", getUniqueID())
                                        intent.putExtra("hour", horas)
                                        intent.putExtra("date", fechaApp)
                                        intent.putExtra("patient_name", nombreCompleto)
                                        intent.putExtra("doctor_name", doctorName)
                                        startActivity(intent)
                                    } else {
                                        val layoutToast = layoutInflater.inflate(
                                            R.layout.custom_toast_layout,
                                            custom_toast_container
                                        )
                                        context?.let { it1 ->
                                            AppUtils.createCustomToast(
                                                "Paciente esta en una llamada",
                                                it1,
                                                layoutToast,
                                                "success"
                                            )
                                        }
                                    }
                                }
                            })

                    } else {
                        val layoutToast = layoutInflater.inflate(
                            R.layout.custom_toast_layout,
                            custom_toast_container
                        )
                        context?.let { it1 ->
                            AppUtils.createCustomToast(
                                "No tiene cita programada",
                                it1,
                                layoutToast,
                                "warning"
                            )
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }
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

    private fun askPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.context as Activity,
                permissions[0]
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                this.context as Activity,
                permissions[1]
            )
        ) {
//            Toast.makeText(this.context, "probando 1", Toast.LENGTH_LONG).show()
            // usuario ya rechazo permisos
            ActivityCompat.requestPermissions(this.context as Activity, permissions, requestcode)

        } else {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.context, "probando 0", Toast.LENGTH_LONG).show()
                } else {

                    val modalPermissionVerification = LayoutInflater.from(this.context)
                        .inflate(R.layout.dialog_confirmation_permission, null)
                    val mbuilder =
                        AlertDialog.Builder(this.context).setView(modalPermissionVerification)
                            .setTitle("permissions")
                    val mDialog = mbuilder.show()
                    modalPermissionVerification.btn_ok.setOnClickListener {
                        startInstalledAppDetailsActivity(this.context as Activity)
                    }
                    modalPermissionVerification.btn_cancel.setOnClickListener {
                        mDialog.dismiss()
                    }
                }
            }
            1 -> {

                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.context, "probando 1", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this.context, "probando 4", Toast.LENGTH_LONG).show()
                    val modalPermissionVerification = LayoutInflater.from(this.context)
                        .inflate(R.layout.dialog_confirmation_permission, null)
                    val mbuilder =
                        AlertDialog.Builder(this.context).setView(modalPermissionVerification)
                            .setTitle("permissions")
                    val mDialog = mbuilder.show()
                    modalPermissionVerification.btn_ok.setOnClickListener {
                        startInstalledAppDetailsActivity(this.context as Activity)
                    }
                    modalPermissionVerification.btn_cancel.setOnClickListener {
                        mDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun getUniqueID(): String {
        return UUID.randomUUID().toString()
    }
}