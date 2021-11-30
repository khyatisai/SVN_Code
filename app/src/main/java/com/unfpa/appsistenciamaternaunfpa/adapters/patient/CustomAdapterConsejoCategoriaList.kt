package com.unfpa.appsistenciamaternaunfpa.adapters.patient

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant.InformacionDetalleActivity
import java.util.*


class CustomAdapterConsejoCategoriaList(
    private var context: Context,

    private var IdConsejos: ArrayList<String> = ArrayList(),
    private var tvCategoriaConsejos: ArrayList<String> = ArrayList()
) :
    RecyclerView.Adapter<CustomAdapterConsejoCategoriaList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_consejolistcategoria, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        //holder.nombrePregnant.text = IdConsejos[position]
        holder.tvCategoria.text = tvCategoriaConsejos[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, IdConsejos[position], Toast.LENGTH_SHORT).show()

            val intent = Intent(context, InformacionDetalleActivity::class.java)
            intent.putExtra("URI", IdConsejos[position])
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            //val intent = Intent(context, CustomAdapterConsejoCategoriaList::class.java)
            //intent.putExtra("URI", IdConsejos[position])

        }
    }
    override fun getItemCount(): Int {
        return IdConsejos.size
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var tvCategoria: TextView = itemView.findViewById<View>(R.id.nombreCategoria) as TextView
    }
}