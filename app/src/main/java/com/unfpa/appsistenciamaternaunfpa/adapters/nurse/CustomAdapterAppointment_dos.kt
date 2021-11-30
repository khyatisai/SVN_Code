package com.unfpa.appsistenciamaternaunfpa.adapters.nurse

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import kotlin.collections.ArrayList

class CustomAdapterAppointment_dos(
    private var context: Activity? = null,
    private var HoraCitaPac: ArrayList<String>? = null,
    private var NombreCompletoPac: ArrayList<String>? = null,
    private var typeAppointment: ArrayList<String>? = null,
    private var docName: ArrayList<String>? = null
) : RecyclerView.Adapter<CustomAdapterAppointment_dos.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_list_cal, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.horaAppointment.text = HoraCitaPac?.get(position) ?: null
        holder.NombrePaciente.text = NombreCompletoPac?.get(position) ?: null
        holder.TypeAppointment.text = typeAppointment?.get(position) ?: null
        holder.txtDocName.text = docName?.get(position) ?: null
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, NombreCompletoPac?.get(position), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return NombreCompletoPac?.size!!
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var horaAppointment: TextView = itemView.findViewById<View>(R.id.txtHora) as TextView
        var NombrePaciente: TextView =
            itemView.findViewById<View>(R.id.txtNombrePaciente) as TextView
        var TypeAppointment: TextView =
            itemView.findViewById<View>(R.id.txtTypeAppointment) as TextView
        var txtDocName: TextView = itemView.findViewById<View>(R.id.txtDocName) as TextView
    }
}