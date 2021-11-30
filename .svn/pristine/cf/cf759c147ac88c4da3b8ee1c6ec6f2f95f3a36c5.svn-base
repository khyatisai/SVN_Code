package com.unfpa.appsistenciamaternaunfpa.fragments.nurse

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.PatientActivityNurse
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.DoctorsList
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.chat.DoctorsListPatient
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogListUsersFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.btnsearch
import kotlinx.android.synthetic.main.bottomsheet_fragma.etBusqueda
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.fragment_dialog_addpacientetodoctor.*
import kotlinx.android.synthetic.main.fragment_dialog_from_calender.*
import org.json.JSONObject


class DialogAddPacienteToDoctorFragment : BottomSheetDialogFragment() {
    var GETUSER = EndPoints.URL_HEROKU + "/api/v1/user/getonlyuser"
    var patientId: String = ""
    val AddPregnantToDoctor = EndPoints.URL_HEROKU + "/api/v1/patients/addpatient"
    val DocListByNurse = EndPoints.URL_HEROKU + "/api/v1/nurse/getdoctors"
    var selectedDoctorId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val rootView = inflater.inflate(R.layout.fragment_dialog_addpacientetodoctor, container,false)
//        rootView!!.post {
//            BottomSheetBehavior.PEEK_HEIGHT_AUTO
//        }

        return inflater.inflate(R.layout.add_patient_to_doc_nurse, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

        val layoutToast =
            layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)


        val IdUser = arguments?.getString("IdUser")
        val nombre = arguments?.getString("nombre")
        val gestationWeeks = arguments?.getString("gestationWeeks")
        val gestationWeeksDate = arguments?.getString("gestationWeeksDate")

        val pathologicalAntecedents = arguments?.getString("pathologicalAntecedents")
        val treatmentsReceived = arguments?.getString("treatmentsReceived")
        val medicalObservations = arguments?.getString("medicalObservations")

        etSemanaGestacion.setText(gestationWeeks)
        etAntecedentePatologio.setText(pathologicalAntecedents)
        etTratamientosRecibidos.setText(treatmentsReceived)
        etObservacionesMedicas.setText(medicalObservations)

        patientId = arguments?.getString("id").toString()

        txtVPaciente2.setText(nombre?.capitalize())

        etBusqueda.setText(nombre?.capitalize())

        var getMyUserId = AppUtils.getDataUser(this.context as Activity)
        val jsonobj = JSONObject()
        jsonobj.put("nurseid", getMyUserId)

        val que = Volley.newRequestQueue(this.context as Activity)
        val req = JsonObjectRequest(
            Request.Method.POST, DocListByNurse, jsonobj,
            Response.Listener { response ->
                val obj = response.getJSONArray("data")
                if (response.getString("message") != "unsuccessfuly") {
                    rlDocList.visibility = View.VISIBLE
                    val linearLayoutManagerApp = LinearLayoutManager(this.context as Activity)
                    rlDocList.layoutManager = linearLayoutManagerApp
                    val adapter = DoctorsListPatient(obj, this)
                    rlDocList.adapter = adapter
                } else {
                    rlDocList.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                println(error)
            })
        que.add(req)
        btnsearch.setOnClickListener {

            var busqueda = etBusqueda.text.toString()

            val bottomSheetFragment = DialogListUsersFragment()
            val bundle = Bundle()
            bundle.putString("busqueda", busqueda)
            bundle.putString("tipo", "2")
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(childFragmentManager, "bottomSheetFragment")


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
//
//
//                        if(response.getString("message") != "error"){
//                            val obj = response.getJSONObject("response")
//                            patientId = obj.getString("id")
//                            val firstname = obj.getString("firstname")
//                            val lastname = obj.getString("lastname")
//
//                            txtVPaciente2.setText("${firstname.capitalize()}"+" "+"${lastname.capitalize()}")
//                            //Toast.makeText(activity ,"Paciente encontrado correctamente", Toast.LENGTH_LONG).show()
//                            context?.let { it1 ->
//                                AppUtils.createCustomToast("Paciente encontrado correctamente", it1, layoutToast,"success")
//                            }
//                            it.hideKeyboard()
//                        }else{
//                            context?.let { it1 ->
//                                AppUtils.createCustomToast("No se ha encontrado el paciente", it1, layoutToast,"warning")
//                            }
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


        var getNomDoctor = AppUtils.getDataCompleteNomUser(this.context as Activity)
        btnSave2.setOnClickListener {
//            if (ValidateForm()){
            if (selectedDoctorId.isNullOrBlank()) {
                context?.let { it1 ->
                    AppUtils.createCustomToast(
                        "please select doctor",
                        it1,
                        layoutToast,
                        "warning"
                    )
                }
                return@setOnClickListener
            } else {
                val jsonobj = JSONObject()

                jsonobj.put("doctorid", selectedDoctorId)
                jsonobj.put("userid", IdUser)

                jsonobj.put("gestationWeeks", etSemanaGestacion.text.toString())
                jsonobj.put("gestationWeeksDate", gestationWeeksDate)
                jsonobj.put("pathologicalAntecedents", etAntecedentePatologio.text.toString())
                jsonobj.put("treatmentsReceived", etTratamientosRecibidos.text.toString())
                jsonobj.put("medicalObservations", etObservacionesMedicas.text.toString())

                val que = Volley.newRequestQueue(this.context as Activity)
                val req = JsonObjectRequest(
                    Request.Method.POST, AddPregnantToDoctor, jsonobj,
                    Response.Listener { response ->

                        val obj = response.getJSONObject("response")
                        if (obj.getString("code") == "duplicate") {
                            context?.let { it1 ->
                                AppUtils.createCustomToast(
                                    obj.getString("message"),
                                    it1,
                                    layoutToast,
                                    "warning"
                                )
                            }
                        } else {
                            var intent = Intent(activity, PatientActivityNurse::class.java)
                            startActivity(intent)
                            getActivity()?.finish()
                            context?.let { it1 ->
                                AppUtils.createCustomToast(
                                    obj.getString("message"),
                                    it1,
                                    layoutToast,
                                    "success"
                                )
                            }
                        }
                    },
                    Response.ErrorListener { error ->
                        println(error)
                    })
                que.add(req)
            }
        }

        btn_exit.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //Code here
        //Toast.makeText(context,"oculto", Toast.LENGTH_SHORT).show()
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    fun Context.toast(
        context: Context = applicationContext,
        message: String,
        toastDuration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, message, toastDuration).show()
    }

    fun Context.stringResToast(
        context: Context = applicationContext,
        message: Int,
        toastDuration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, context.getString(message), toastDuration).show()
    }

    private fun ValidateForm(): Boolean {

        if (TextUtils.isEmpty(etBusqueda.text)) {
            etBusqueda.error = "Por favor ingrese un dato y seleccione buscar"
            etBusqueda.requestFocus()
            return false
        }
        if (patientId == "" || patientId == "null") {
            etBusqueda.error = "Por favor ingrese un dato y seleccione buscar"
            etBusqueda.requestFocus()
            txtVPaciente2.error = "No hay ningun paciente seleccionado"
            txtVPaciente2.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(etSemanaGestacion.text)) {
            etSemanaGestacion.error = "Por favor ingrese un dato"
            etSemanaGestacion.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etAntecedentePatologio.text)) {
            etAntecedentePatologio.error = "Por favor ingrese un dato"
            etAntecedentePatologio.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etTratamientosRecibidos.text)) {
            etTratamientosRecibidos.error = "Por favor ingrese un dato"
            etTratamientosRecibidos.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(etObservacionesMedicas.text)) {
            etObservacionesMedicas.error = "Por favor ingrese un dato"
            etObservacionesMedicas.requestFocus()
            return false
        }


        return true
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}