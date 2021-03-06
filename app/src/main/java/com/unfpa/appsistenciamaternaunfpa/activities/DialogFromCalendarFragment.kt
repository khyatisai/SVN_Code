package com.unfpa.appsistenciamaternaunfpa.activities

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.popup_period_date_input.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DialogFromCalendarFragment: BottomSheetDialogFragment(){
    var GETUSER = EndPoints.URL_HEROKU + "/api/v1/user/getuser"
    var selectedTime: String = ""
    var selectedTimeFormated:String = ""
    var patientId: String = ""
    var dateFormat:String = ""
    var dateUnFormat:String = ""
    var enteredDt: String = ""
    var shape: Drawable? = null
    var shape2: Drawable? = null
    var selectedTypeRb1:Int = 0
    var selectedTypeRb2:Int = 0
    val APPOINTMENT = EndPoints.URL_HEROKU + "/api/v1/appointment/register"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragma, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

        val nombre =  arguments?.getString("nombre")
        patientId = arguments?.getString("id").toString()

        txtVPaciente.setText(nombre?.capitalize())

        etBusqueda.setText(nombre?.capitalize())


//        etBusqueda.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
//            //Start your action
//            Toast.makeText(activity, "Hi there ! \n This is a Enter Event Button", Toast.LENGTH_LONG).show()
//                //                            Toast.makeText(activity ,"Paciente encontrado correctamente", Toast.LENGTH_LONG).show()
//            //End action
//            return@OnKeyListener true
//        }
//            false
//        })


        btnsearch.setOnClickListener{

            var busqueda = etBusqueda.text.toString()

            val bottomSheetFragment = DialogListUsersFragment()
            val bundle = Bundle()
            bundle.putString("busqueda", busqueda)
            bundle.putString("tipo", "1")
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(childFragmentManager,"bottomSheetFragmentFromCalendar")

            //searchUserList.show(childFragmentManager,"Bottom sheet dialog")
        }
//        btnsearch.setOnClickListener{
//            val jsonobj = JSONObject()
//            jsonobj.put("identification", etBusqueda.text.toString())
//
//            val que = Volley.newRequestQueue(activity)
//            val req = JsonObjectRequest(
//                Request.Method.POST, GETUSER, jsonobj,
//                Response.Listener { response ->
//                    try {
//
//                        if(response.getString("message") != "error"){
//                            val obj = response.getJSONObject("response")
//                            patientId = obj.getString("id")
//                            val firstname = obj.getString("firstname")
//                            val lastname = obj.getString("lastname")
//
//                            txtVPaciente.setText("Tu, $firstname"+" "+"$lastname")
//                            Toast.makeText(activity ,"Paciente encontrado correctamente", Toast.LENGTH_LONG).show()
//                            it.hideKeyboard()
//                        }else{
//                            Toast.makeText(activity ,"No se ha encontrado el paciente", Toast.LENGTH_LONG).show()
//                        }
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                },
//                Response.ErrorListener {
//                        error ->
////                        loginError.setText(getVolleyError(error));
//                })
//            que.add(req)
//        }
        btn_button1.setOnClickListener{
            this.dismiss()
        }
        txtHour.setOnClickListener {
            showTImePopup()//Select Appointment Date
        }
        txtSelDate.setOnClickListener{
            showDatePopup(getString(R.string.select_appointment_date))
        }
        shape = activity?.let { getDrawable(it,R.drawable.boton_redondo_relleno) }
        shape2 = activity?.let { getDrawable(it,R.drawable.boton_redondo) }

        rbMeet.setHintTextColor(resources.getColor(R.color.colorDoctorBlue))
        rbMeet.setPadding(28, 0, 0, 0)
        rbMeet.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                rbMeet.background = shape
                rbMeet.setHintTextColor(Color.WHITE)
//                rbMeet.setPadding(31, 0, 0, 0)
                rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb1 = 1;
            }else {
                rbMeet.background = shape2
                rbMeet.setHintTextColor(resources.getColor(R.color.colorDoctorBlue))
//                rbMeet.setPadding(31, 0, 0, 0)
                rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb1 = 0;
            }
        }
        rbTelMedicine.setHintTextColor(resources.getColor(R.color.colorDoctorBlue))
        rbTelMedicine.setPadding(28, 0, 0, 0)
        rbTelMedicine.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                rbTelMedicine.background = shape
                rbTelMedicine.setHintTextColor(Color.WHITE)
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb2 = 2;
            }else {
                rbTelMedicine.background = shape2
                rbTelMedicine.setHintTextColor(resources.getColor(R.color.colorDoctorBlue))
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                selectedTypeRb2 = 0;
            }
        }
        var  getMyUserId = AppUtils.getDataUser(this.context as Activity)
        var getNomDoctor = AppUtils.getDataCompleteNomUser(this.context as Activity)
        btnSave.setOnClickListener {
            if (etBusqueda.text.toString() == "" ) {
                etBusqueda.setError("Por favor ingrese un dato y seleccione buscar")
                etBusqueda.requestFocus()
            }else if (patientId == "" ) {
                txtVPaciente.setError("No hay ningun paciente seleccionado")
                txtVPaciente.requestFocus()
            }
            else if(selectedTypeRb1 == 0 && selectedTypeRb2 == 0){
                rbMeet.setError("Seleccione el tipo de cita presencial o remoto")
                rbMeet.requestFocus()
            }
            else if(dateFormat == ""){
                txtSelDate.setError("Seleccione la fecha de la cita")
                txtSelDate.requestFocus()
            }
            else if(selectedTime == ""){
                txtHour.setError("Seleccione la hora de la cita")
                txtHour.requestFocus()
            }
            else {
                val jsonobj = JSONObject()
                jsonobj.put("patient", patientId)
                jsonobj.put("doctor", getMyUserId)
                jsonobj.put("typeAppointment", if(selectedTypeRb1 == 1)  "1" else "2")
                jsonobj.put("date", dateFormat)
                jsonobj.put("hour", selectedTime)

                val que = Volley.newRequestQueue(this.context as Activity)
                val req = JsonObjectRequest(
                    Request.Method.POST, APPOINTMENT, jsonobj,
                    Response.Listener { response ->

//                        if (response.length() != 0) {
//                            var intent = Intent(activity, MainDoctorActivity::class.java)
//                            startActivity(intent)
//                            getActivity()?.finish()
//                            //Toast.makeText(activity, "Cita programada correctamente",Toast.LENGTH_LONG).show()
//                            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
//                            context?.let { it1 ->
//                                AppUtils.createCustomToast("Cita programada correctamente", it1, layoutToast,"success")
//                            }
//                        }

                        val obj = response.getJSONObject("data")
                        if(response.getString("message") != "unsuccessfuly"){
                            var intent = Intent(activity, MainDoctorActivity::class.java)
                            startActivity(intent)
                            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                            context?.let { it1 ->
                                AppUtils.createCustomToast("Cita programada correctamente.", it1, layoutToast,"success")
                            }
                        }else{
                            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                            context?.let { it1 ->
                                AppUtils.createCustomToast("Ya existe una cita para hoy a la misma hora.", it1, layoutToast,"warning")
                            }
                        }
                    },
                    Response.ErrorListener { error ->
                        println(error)
                    })
                que.add(req)
            }

        }
    }
    fun showTImePopup() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            try {
                c.set(Calendar.HOUR_OF_DAY, h)
                c.set(Calendar.MINUTE, m)
                txtHour.text =  AppUtils.getTime12HourFormat(SimpleDateFormat("HH:mm").format(c.time))
                selectedTimeFormated = AppUtils.getTime12HourFormat(SimpleDateFormat("HH:mm").format(c.time))
                txtHour.error = null
                selectedTime = String.format("%d:%d", h, m,":00")
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }), hour, minute, false)

        tpd.show()
    }
    fun showDatePopup(message: String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_period_date_input_dos)
        dialog.btnDone.setOnClickListener {
            if(enteredDt != "") {
                dateFormat = AppUtils.dateConverter("dd-MM-yyyy", "yyyy-MM-dd", enteredDt)
                dateUnFormat = enteredDt
                txtSelDate.text = enteredDt
                dialog.dismiss()
            }
        }


        dialog.txtHeader.text = message

        dialog.calendarView?.minDate = Calendar.getInstance().timeInMillis
        dialog.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            enteredDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            // txtDate.text = enteredDt
        }
        dialog.txtClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}