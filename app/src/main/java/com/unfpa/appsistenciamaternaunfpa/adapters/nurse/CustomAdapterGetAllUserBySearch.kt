package com.unfpa.appsistenciamaternaunfpa.adapters.nurse

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.DialogAddPacienteToDoctorFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogFromCalendarFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.appointment.DialogListUsersFragment
import kotlin.collections.ArrayList


class CustomAdapterGetAllUserBySearch(
    private var context: Activity,
    private var IdUser: ArrayList<String>? = null,
    private var IdBrigadista: ArrayList<String>? = null,
    private var cedulaList: ArrayList<String>? = null,
    private var NombreCompletoBrigadista: ArrayList<String>? = null,
    private var GestationWeeks: ArrayList<String>? = null,
    private var GestationWeeksDate: ArrayList<String>? = null,
    private var PathologicalAntecedents: ArrayList<String>? = null,
    private var TreatmentsReceived: ArrayList<String>? = null,
    private var MedicalObservations: ArrayList<String>? = null,
    private var tipo: String? = null,
    private val fragment: DialogListUsersFragment
) : RecyclerView.Adapter<CustomAdapterGetAllUserBySearch.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_users_searchlistitem, parent, false)
        return MyViewHolder(v)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.txtNombreBrigadista.text = NombreCompletoBrigadista?.get(position) ?: null
        holder.txtCedula.text = cedulaList?.get(position) ?: null

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
//            Toast.makeText(context, NombreCompletoBrigadista?.get(position), Toast.LENGTH_SHORT).show()
            //ContextUtils.getActivity(context)?.finish()
            var bottomSheetFragment = BottomSheetDialogFragment()
            if (tipo == "1") {
                (context as FragmentActivity).supportFragmentManager.findFragmentByTag("tagdialogcalendar")
                    .let {
                        (it as DialogFromCalendarFragment).dismiss()
                    }
//                (context as FragmentActivity).supportFragmentManager.findFragmentByTag("bottomSheetFragmentFromCalendar").let {
//                    (it as DialogListUsersFragment).dismiss()
//                }
                bottomSheetFragment = DialogFromCalendarFragment()
            } else {
                try {
                    //(context as FragmentActivity).supportFragmentManager.findFragmentByTag("bottomSheetFragment")
                    (context as FragmentActivity).supportFragmentManager.findFragmentByTag("bottomSheetFragment1")
                        .let {
                            (it as DialogAddPacienteToDoctorFragment).dismiss()
                        }

                    bottomSheetFragment = DialogAddPacienteToDoctorFragment()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            val bundle = Bundle()

            if (tipo == "2") {
                bundle.putString("IdUser", IdUser?.get(position))
            }

            bundle.putString("nombre", NombreCompletoBrigadista?.get(position))
            bundle.putString("gestationWeeks", GestationWeeks?.get(position))
            bundle.putString("gestationWeeksDate", GestationWeeksDate?.get(position))
            bundle.putString("pathologicalAntecedents", PathologicalAntecedents?.get(position))
            bundle.putString("treatmentsReceived", TreatmentsReceived?.get(position))
            bundle.putString("medicalObservations", MedicalObservations?.get(position))
            bundle.putString("id", IdBrigadista?.get(position))
            bottomSheetFragment.arguments = bundle
            if (tipo == "1") {
                bottomSheetFragment.show(
                    (context as FragmentActivity).supportFragmentManager,
                    "tagdialogcalendar"
                )
            } else {
                bottomSheetFragment.show(
                    (context as FragmentActivity).supportFragmentManager,
                    "bottomSheetFragment"
                )
            }

//            val intent = Intent(context, PacienteAddBrigadistActivity::class.java)
//            intent.putExtra("nombreBrigadist", NombreCompletoBrigadista?.get(position))
//            intent.putExtra("idBrigadista", IdBrigadista?.get(position))
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)


            fragment.dismiss()
            //fragment.fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    override fun getItemCount(): Int {
        return NombreCompletoBrigadista?.size!!
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var txtNombreBrigadista: TextView =
            itemView.findViewById<View>(R.id.txtNombreBrigadista) as TextView
        var txtCedula: TextView = itemView.findViewById<View>(R.id.tvCedula) as TextView

    }
}