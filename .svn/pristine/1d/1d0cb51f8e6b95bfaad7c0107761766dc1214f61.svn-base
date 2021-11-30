package com.unfpa.appsistenciamaternaunfpa.adapters.doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.R.color.colorRed
import java.util.*

class CustomAdapterAppointment(
    private var context: Activity? = null,
    private var HoraCitaPac: ArrayList<String>? = null,
    private var NombreCompletoPac: ArrayList<String>? = null,
    private var ListCancelada: ArrayList<String>? = null

) : RecyclerView.Adapter<CustomAdapterAppointment.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_homedoctorlistitem, parent, false)
        return MyViewHolder(v)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items

        val isCancel = ListCancelada?.get(position) ?: null

        if(isCancel  == "true"){
            holder.imgCacnelada.setColorFilter(Color.RED)
        }

        holder.horaAppointment.text = HoraCitaPac?.get(position) ?: null
        holder.NombrePaciente.text = NombreCompletoPac?.get(position) ?: null
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
        var NombrePaciente: TextView = itemView.findViewById<View>(R.id.txtNombrePaciente) as TextView
        var imgCacnelada: ImageView = itemView.findViewById<View>(R.id.circleAppointment) as ImageView
    }
}