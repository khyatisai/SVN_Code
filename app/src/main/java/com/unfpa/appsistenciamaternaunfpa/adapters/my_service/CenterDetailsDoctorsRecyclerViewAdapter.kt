package com.unfpa.appsistenciamaternaunfpa.adapters.my_service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.User
import kotlinx.android.synthetic.main.fragment_myservice_doctors_and_nurses_content.view.*

class CenterDetailsDoctorsRecyclerViewAdapter(private val doctorList: ArrayList<User>) : androidx.recyclerview.widget.RecyclerView.Adapter<CenterDetailsDoctorsRecyclerViewAdapter.DoctorViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_myservice_doctors_and_nurses_content, parent, false)
        return DoctorViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bindItems(doctorList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return doctorList.size }

    class DoctorViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(user: User) {
            val textViewName = itemView.findViewById(R.id.txtDoctorName) as TextView
            val textViewProfession  = itemView.findViewById(R.id.txtProfession) as TextView
            val imgDocNurse  = itemView.findViewById(R.id.imgDocNurse) as ImageView
            textViewName.text = user.name
            textViewProfession.text = user.profession
            if(textViewProfession.text.contains("Doctor")){
                itemView.imgDocNurse.setImageResource(R.drawable.ic_user_yellow)
            }else{
                itemView.imgDocNurse.setImageResource(R.drawable.ic_user_blue)
            }
        }
    }
}