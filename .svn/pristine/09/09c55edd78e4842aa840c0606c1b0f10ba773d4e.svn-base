package com.unfpa.appsistenciamaternaunfpa.adapters.patient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import java.util.*

class CustomAdapterPregnantListByBrigadista(
    private var context: Context,

    private var nombrePac: ArrayList<String> = ArrayList(),
    private var tvCitasPersona: ArrayList<String> = ArrayList(),
    private var tvCitasTelemedicina: ArrayList<String> = ArrayList()
) :
    RecyclerView.Adapter<CustomAdapterPregnantListByBrigadista.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_doctorlistpaciente, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.nombrePregnant.text = nombrePac[position]
        holder.CitasPersona.text = tvCitasPersona[position]
        holder.CitasTelemedicina.text = tvCitasTelemedicina[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, nombrePac[position], Toast.LENGTH_SHORT).show()
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