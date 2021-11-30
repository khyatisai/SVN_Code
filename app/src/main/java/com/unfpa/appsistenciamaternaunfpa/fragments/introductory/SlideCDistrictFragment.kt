package com.unfpa.appsistenciamaternaunfpa.fragments.introductory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.IntroAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_c_district.*

class SlideCDistrictFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_slide_c_district, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerDistrict.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val users = ArrayList<Type>()
            /*users.add(Type("District 1"))
            users.add(Type("District 2"))
            users.add(Type("District 3"))
            users.add(Type("District 4"))
            users.add(Type("District 5"))
            users.add(Type("District 6"))
            users.add(Type("District 7"))
            users.add(Type("District 8"))
            users.add(Type("District 9"))*/
            val adapter = IntroAdapter(users, this.activity!!, Constant.ITEM_DISTRICT)
            recyclerDistrict.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}