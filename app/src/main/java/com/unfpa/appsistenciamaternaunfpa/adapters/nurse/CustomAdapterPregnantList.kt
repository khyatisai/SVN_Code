package com.unfpa.appsistenciamaternaunfpa.adapters.nurse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteDetalleActivity
import com.unfpa.appsistenciamaternaunfpa.activities.nurse.PatientDetailActivity
import java.util.*

class CustomAdapterPregnantList(
    private var context: Context,

    private var nombrePac: ArrayList<String> = ArrayList(),
    private var tvCitasPersona: ArrayList<String> = ArrayList(),
    private var tvCitasTelemedicina: ArrayList<String> = ArrayList(),
    private var IdUserPac: ArrayList<String> = ArrayList()
) :
    RecyclerView.Adapter<CustomAdapterPregnantList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_nurse_patientlist, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.nombrePregnant.text = nombrePac[position]
        holder.CitasPersona.text = tvCitasPersona[position]
        holder.CitasTelemedicina.text = tvCitasTelemedicina[position]

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            //Toast.makeText(context, IdUserPac[position], Toast.LENGTH_SHORT).show()
            var intent =  Intent(context, PatientDetailActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("idUserPac", IdUserPac[position])
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return nombrePac.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var nombrePregnant: TextView = itemView.findViewById<View>(R.id.nombrePac) as TextView
        var CitasPersona: TextView = itemView.findViewById<View>(R.id.tvCitasPersona) as TextView
        var CitasTelemedicina: TextView = itemView.findViewById<View>(R.id.tvCitasTelemedicina) as TextView
    }
}