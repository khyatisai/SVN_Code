package com.unfpa.appsistenciamaternaunfpa.fragments.introductory


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.IntroductoryActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.IntroAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_d_gender.*

/**
 * A simple [Fragment] subclass.
 */
class SlideDGenderFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_slide_d_gender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerGender.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val users = ArrayList<Type>()
            /*users.add(Type("Male",false))
            users.add(Type("Female",false))
            users.add(Type("Other / Self Described",false))*/
            val adapter = IntroAdapter(
                (activity as IntroductoryActivity).getGenderList() as ArrayList<Type>,
                this.activity!!,
                Constant.ITEM_GENDER
            )
            recyclerGender.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
