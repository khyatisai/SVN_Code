package com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainDoctorActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.MainNurseActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.nurse.DoctorsList
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.*
import kotlinx.android.synthetic.main.bottomsheet_fragma.btnSave
import kotlinx.android.synthetic.main.bottomsheet_fragma.btn_button1
import kotlinx.android.synthetic.main.bottomsheet_fragma.btnsearch
import kotlinx.android.synthetic.main.bottomsheet_fragma.etBusqueda
import kotlinx.android.synthetic.main.bottomsheet_fragma.rbMeet
import kotlinx.android.synthetic.main.bottomsheet_fragma.rbTelMedicine
import kotlinx.android.synthetic.main.bottomsheet_fragma.txtHour
import kotlinx.android.synthetic.main.bottomsheet_fragma.txtSelDate
import kotlinx.android.synthetic.main.bottomsheet_fragma.txtVPaciente
import kotlinx.android.synthetic.main.custom_toast_layout.*
import kotlinx.android.synthetic.main.fragment_dialog_from_calender.*
import kotlinx.android.synthetic.main.popup_period_date_input.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DialogFromCalendarFragment : BottomSheetDialogFragment() {

    var selectedTime: String = ""
    var selectedTimeFormated: String = ""
    var patientId: String = ""
    var dateFormat: String = ""
    var dateUnFormat: String = ""
    var enteredDt: String = ""
    var shape: Drawable? = null
    var shape2: Drawable? = null
    var selectedTypeRb1: Int = 0
    var selectedTypeRb2: Int = 0
    val APPOINTMENT = EndPoints.URL_HEROKU + "/api/v1/nurse/appointment/register"
    val DocListByNurse = EndPoints.URL_HEROKU + "/api/v1/nurse/getdoctors"
    public var selectedDoctorId = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* try {
            val view: View =
                inflater.inflate(R.layout.fragment_dialog_from_calender, container, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view*/
        return inflater.inflate(R.layout.fragment_dialog_from_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

            val nombre = arguments?.getString("nombre")
            patientId = arguments?.getString("id").toString()

            txtVPaciente.setText(nombre?.capitalize(Locale.ROOT))

            etBusqueda.setText(nombre?.capitalize(Locale.ROOT))
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
                        val adapter = DoctorsList(obj, this)
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
                bundle.putString("tipo", "1")
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(childFragmentManager, "bottomSheetFragmentFromCalendar")

                //searchUserList.show(childFragmentManager,"Bottom sheet dialog")
            }
            btn_button1.setOnClickListener {
                this.dismiss()
            }
            txtHour.setOnClickListener {
                showTImePopup()//Select Appointment Date
            }
            txtSelDate.setOnClickListener {
                showDatePopup(getString(R.string.select_appointment_date))
            }
            shape = activity?.let { getDrawable(it, R.drawable.round_shape_green) }
            shape2 = activity?.let { getDrawable(it, R.drawable.button_round_green) }

            rbMeet.setHintTextColor(resources.getColor(R.color.nurse_green_color))
            rbMeet.setPadding(28, 0, 0, 0)
            rbMeet.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    rbMeet.background = shape
                    rbMeet.setHintTextColor(Color.WHITE)
//                rbMeet.setPadding(31, 0, 0, 0)
                    rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    selectedTypeRb1 = 1;
                } else {
                    rbMeet.background = shape2
                    rbMeet.setHintTextColor(resources.getColor(R.color.nurse_green_color))
//                rbMeet.setPadding(31, 0, 0, 0)
                    rbMeet.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    selectedTypeRb1 = 0;
                }
            }
            rbTelMedicine.setHintTextColor(resources.getColor(R.color.nurse_green_color))
            rbTelMedicine.setPadding(28, 0, 0, 0)
            rbTelMedicine.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    rbTelMedicine.background = shape
                    rbTelMedicine.setHintTextColor(Color.WHITE)
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                    rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    selectedTypeRb2 = 2;
                } else {
                    rbTelMedicine.background = shape2
                    rbTelMedicine.setHintTextColor(resources.getColor(R.color.nurse_green_color))
//                rbTelMedicine.setPadding(31, 0, 0, 0)
                    rbTelMedicine.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    selectedTypeRb2 = 0;
                }
            }

            var getNomDoctor = AppUtils.getDataCompleteNomUser(this.context as Activity)
            btnSave.setOnClickListener {
                if (etBusqueda.text.toString() == "") {
                    etBusqueda.setError("Por favor ingrese un dato y seleccione buscar")
                    etBusqueda.requestFocus()
                } else if (patientId == "") {
                    txtVPaciente.setError("No hay ningun paciente seleccionado")
                    txtVPaciente.requestFocus()
                } else if (selectedTypeRb1 == 0 && selectedTypeRb2 == 0) {
                    rbMeet.setError("Seleccione el tipo de cita presencial o remoto")
                    rbMeet.requestFocus()
                } else if (dateFormat == "") {
                    txtSelDate.setError("Seleccione la fecha de la cita")
                    txtSelDate.requestFocus()
                } else if (selectedTime == "") {
                    txtHour.setError("Seleccione la hora de la cita")
                    txtHour.requestFocus()
                } else if (selectedDoctorId == "") {
                    val layoutToast =
                        layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)

                    AppUtils.createCustomToast(
                        "Please Select doctor",
                        requireContext(),
                        layoutToast,
                        "warning"
                    )

                } else {
                    val jsonobj = JSONObject()
                    jsonobj.put("patient", patientId.toInt())
                    jsonobj.put("doctor", selectedDoctorId.toInt())
                    jsonobj.put("nurse", getMyUserId.toInt())
                    jsonobj.put("typeAppointment", if (selectedTypeRb1 == 1) "1" else "2")
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

                            //val obj = response.getJSONObject("data")

                            if (response.getString("message") != "unsuccessfuly") {
                                if (response.has("success") && response.getString("success")
                                        .equals("false")
                                ) {
                                    val layoutToast = layoutInflater.inflate(
                                        R.layout.custom_toast_layout,
                                        custom_toast_container
                                    )
                                    context?.let { it1 ->
                                        AppUtils.createCustomToast(
                                            response.getString("message"),
                                            it1,
                                            layoutToast,
                                            "error"
                                        )
                                    }
                                    return@Listener
                                }
                                if (response.has("message") && response.getString("message")
                                        .equals("successfuly") && response.has("data") && (response.get(
                                        "data"
                                    ) is Boolean) && !response.getBoolean("data")
                                ) {
                                    val layoutToast = layoutInflater.inflate(
                                        R.layout.custom_toast_layout,
                                        custom_toast_container
                                    )
                                    context?.let { it1 ->
                                        AppUtils.createCustomToast(
                                            "Ya existe una cita para hoy a la misma hora.",
                                            it1,
                                            layoutToast,
                                            "warning"
                                        )
                                    }
                                    return@Listener
                                }
                                var intent = Intent(activity, MainNurseActivity::class.java)
                                activity?.finish()
                                startActivity(intent)
                                val layoutToast = layoutInflater.inflate(
                                    R.layout.custom_toast_layout,
                                    custom_toast_container
                                )
                                context?.let { it1 ->
                                    AppUtils.createCustomToast(
                                        "Cita programada correctamente.",
                                        it1,
                                        layoutToast,
                                        "success"
                                    )
                                }
                            } else {
                                val layoutToast = layoutInflater.inflate(
                                    R.layout.custom_toast_layout,
                                    custom_toast_container
                                )
                                context?.let { it1 ->
                                    AppUtils.createCustomToast(
                                        "Ya existe una cita para hoy a la misma hora.",
                                        it1,
                                        layoutToast,
                                        "warning"
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showTImePopup() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val tpd =
            TimePickerDialog(
                activity,
                R.style.TimePickerTheme,
                TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                    try {
                        c.set(Calendar.HOUR_OF_DAY, h)
                        c.set(Calendar.MINUTE, m)
                        txtHour.text =
                            AppUtils.getTime12HourFormat(SimpleDateFormat("HH:mm").format(c.time))
                        selectedTimeFormated =
                            AppUtils.getTime12HourFormat(SimpleDateFormat("HH:mm").format(c.time))
                        txtHour.error = null
                        selectedTime = String.format("%d:%d", h, m, ":00")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }),
                hour,
                minute,
                false
            )

        tpd.show()
    }

    fun showDatePopup(message: String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_date_input_nurse)
        dialog.btnDone.setOnClickListener {
            if (enteredDt != "") {
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
}