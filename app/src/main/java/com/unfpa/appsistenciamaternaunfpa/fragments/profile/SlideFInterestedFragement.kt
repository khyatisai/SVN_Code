package com.unfpa.appsistenciamaternaunfpa.fragments.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.profile.ProfileActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.adapters.profile.IntroAdapterForInterest
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_f_interested_fragement.*

class SlideFInterestedFragement : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_slide_f_interested_fragement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerInterest.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val users = ArrayList<Type>()
            /*users.add(Type("Learn more about sexual and reproductive health topics.",false))
            users.add(Type("Get help fining a clinic.",false))
            users.add(Type("Talk to someone about my questions.",false))
            users.add(Type("Share my Story.",false))
            users.add(Type("Something else.",false))*/
            val adapter = IntroAdapterForInterest(
                (activity as ProfileActivity).getInterestList() as ArrayList<Type>,
                this.activity!!,
                Constant.ITEM_INTEREST
            )
            recyclerInterest.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
