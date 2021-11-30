package com.unfpa.appsistenciamaternaunfpa.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.profile.ProfileActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.adapters.profile.IntroAdapter
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_e_age_group.*

/**
 * A simple [Fragment] subclass.
 */
class SlideEAgeGroupFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_slide_e_age_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerAgeGroup.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val users = ArrayList<Type>()
            /*users.add(Type("14 - 18 years old",false))
            users.add(Type("18 - 24 years old",false))
            users.add(Type("24 or above",false))
            users.add(Type("any",false))*/
            val adapter = IntroAdapter(
                (activity as ProfileActivity).getAgeGrpList() as ArrayList<Type>,
                this.activity!!,
                Constant.ITEM_AGE_GROUP
            )
            recyclerAgeGroup.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
