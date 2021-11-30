package com.unfpa.appsistenciamaternaunfpa.activities

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.doctor.CustomAdapterGetAllBrigadista
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragma.*
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class DialogAddBrigadistToPacFragment : BottomSheetDialogFragment() {
    val GetBrigadista = EndPoints.URL_HEROKU + "/api/v1/user/getbrigadista"

    var IdBrigadista: ArrayList<String> = ArrayList()
    var NombreCompletoBrigadista: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_addbrigadist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerMyAppointment = view.findViewById<RecyclerView>(R.id.recyclerBrigadista)
        val linearLayoutManagerApp = LinearLayoutManager(this.context as Activity)
        recyclerMyAppointment.layoutManager = linearLayoutManagerApp

        btn_button1.setOnClickListener {
            this.dismiss()
        }
        val jsonobj = JSONObject()

        try {
            val quee = Volley.newRequestQueue(this.context as Activity)
            val reqq = JsonObjectRequest(
                Request.Method.POST, GetBrigadista, jsonobj,
                Response.Listener { response ->

                    if(response.getString("message") != "error"){
                        var dataArray = response.getJSONArray("response")
                        IdBrigadista.clear()
                        NombreCompletoBrigadista.clear()


                        for (i in 0 until dataArray.length()) {
                            var idBrigadist = dataArray.getJSONObject(i).getString("id")
                            var nombrecomplet =
                                dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(i).getString("lastname")
                            IdBrigadista.add(idBrigadist)
                            NombreCompletoBrigadista.add(nombrecomplet)
                        }
                        val customAdapter = CustomAdapterGetAllBrigadista(
                            this!!.activity!!,
                            IdBrigadista,
                            NombreCompletoBrigadista,
                            this
                        )
                        recyclerMyAppointment.adapter = customAdapter
                        recyclerMyAppointment.adapter?.notifyDataSetChanged()
                    }else{
                        val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                        context?.let { it1 ->
                            AppUtils.createCustomToast("No hay brigadistas registrado en la base de datos", it1, layoutToast,"success")
                        }
                    }

                },
                Response.ErrorListener { error ->
                    println(error)
                })
            quee.add(reqq)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}