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

class CustomAdapterConsejoCategoriaDetalleList(
    private var context: Context,

    private var idInfoList: ArrayList<String> = ArrayList(),
    private var NameInfoList: ArrayList<String> = ArrayList(),
    private var DescriptionInfoList: ArrayList<String> = ArrayList(),
    private var ImageInfoList: ArrayList<String> = ArrayList()

) :
    RecyclerView.Adapter<CustomAdapterConsejoCategoriaDetalleList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_consejo_detalle_list, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        //holder.nombrePregnant.text = IdConsejos[position]
        holder.tvNameInfoList.text = NameInfoList[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, idInfoList[position], Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return idInfoList.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var tvNameInfoList: TextView = itemView.findViewById<View>(R.id.nombreCategoria) as TextView
    }
}