package com.unfpa.appsistenciamaternaunfpa.adapters.patient

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteDetalleCitaActivity
import java.util.*

class CustomAdapterPregnantDetails(
    private var context: Context,

    private var tvCitaCancelada: ArrayList<String> = ArrayList(),
    private var tvFechaAppointment: ArrayList<String> = ArrayList(),
    private var tvHora: ArrayList<String> = ArrayList(),
    private var tvTypeAppointment: ArrayList<String> = ArrayList(),
    private var tvWeeksPac2: ArrayList<String> = ArrayList(),
    private var tvDiagnostics: ArrayList<String> = ArrayList(),
    private var tvPlans: ArrayList<String> = ArrayList(),
    private var tvreportOfFetalMovements: ArrayList<String> = ArrayList(),
    private var tvarObro: ArrayList<String> = ArrayList(),
    private var tvotherRemarks: ArrayList<String> = ArrayList(),
    private var tvmainReasonForTheConsultation: ArrayList<String> = ArrayList()

) :
    RecyclerView.Adapter<CustomAdapterPregnantDetails.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_doctorlistpacientedetails, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items

        if(tvCitaCancelada[position] == "true"){
            holder.CitaCancelada.visibility = View.VISIBLE
        }

        holder.FechaAppointment.text = tvFechaAppointment[position]
        holder.Hora.text = tvHora[position]
        holder.TypeAppointment.text = tvTypeAppointment[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            //Toast.makeText(context, tvFechaAppointment[position], Toast.LENGTH_SHORT).show()
            var intent =  Intent(context, PacienteDetalleCitaActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("FechaAppointment", tvFechaAppointment[position])
            intent.putExtra("Hora", tvHora[position])
            intent.putExtra("TypeAppointment", tvTypeAppointment[position])
            intent.putExtra("weeksPac", tvWeeksPac2[position])
            intent.putExtra("diagnostics", tvDiagnostics[position])
            intent.putExtra("plans", tvPlans[position])
             intent.putExtra("reportOfFetalMovements", tvreportOfFetalMovements[position])
            intent.putExtra("arObro", tvarObro[position])
             intent.putExtra("otherRemarks", tvotherRemarks[position])
             intent.putExtra("mainReasonForTheConsultation", tvmainReasonForTheConsultation[position])
            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return tvFechaAppointment.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var FechaAppointment: TextView = itemView.findViewById<View>(R.id.tvFechaAppointment) as TextView
        var Hora: TextView = itemView.findViewById<View>(R.id.tvHora) as TextView
        var TypeAppointment: TextView = itemView.findViewById<View>(R.id.tvTypeAppointment) as TextView
        var CitaCancelada: TextView = itemView.findViewById<View>(R.id.tvCitaCancelada) as TextView

    }
}