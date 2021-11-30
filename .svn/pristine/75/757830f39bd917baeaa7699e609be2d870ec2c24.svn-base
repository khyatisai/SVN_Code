package com.unfpa.appsistenciamaternaunfpa.adapters.doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.internal.ContextUtils
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.DialogAddBrigadistToPacFragment
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PacienteAddBrigadistActivity
import java.util.*


class CustomAdapterGetAllBrigadista(
    private var context: Activity,
    private var IdBrigadista: ArrayList<String>? = null,
    private var NombreCompletoBrigadista: ArrayList<String>? = null,
    private val fragment: DialogAddBrigadistToPacFragment
) : RecyclerView.Adapter<CustomAdapterGetAllBrigadista.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_brigadistlistitem, parent, false)
        return MyViewHolder(v)
    }
    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.txtNombreBrigadista.text = NombreCompletoBrigadista?.get(position) ?: null
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, NombreCompletoBrigadista?.get(position), Toast.LENGTH_SHORT).show()
            ContextUtils.getActivity(context)?.finish()


            val intent = Intent(context, PacienteAddBrigadistActivity::class.java)

            intent.putExtra("from", "AdapterBrigadista")
            intent.putExtra("nombreBrigadist", NombreCompletoBrigadista?.get(position))
            intent.putExtra("idBrigadista", IdBrigadista?.get(position))

            intent.putExtra("nombre", context.intent.getStringExtra("nombre"))
            intent.putExtra("apellido", context.intent.getStringExtra("apellido"))
            intent.putExtra("idPaciente", context.intent.getStringExtra("idPaciente"))
            intent.putExtra("idPacienteUser", context.intent.getStringExtra("idPacienteUser"))
            intent.putExtra("birth", context.intent.getStringExtra("birth"))
            intent.putExtra("gestationWeeks", context.intent.getStringExtra("gestationWeeks"))


            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            //fragment.dismiss()
            //fragment.fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    override fun getItemCount(): Int {
        return NombreCompletoBrigadista?.size!!
    }
    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var txtNombreBrigadista: TextView = itemView.findViewById<View>(R.id.txtNombreBrigadista) as TextView
    }
}