package com.unfpa.appsistenciamaternaunfpa.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.profile.ProfileActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.profile.IntroAdapter
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_g_education.*

/**
 * Created by KhyatiShah on 12/24/2019.
 */
class SlideGEducationFragement : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_slide_g_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerEducation.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val adapter = IntroAdapter(
                (activity as ProfileActivity).getEducationList(),
                this.activity!!,
                Constant.ITEM_EDUCATION
            )
            recyclerEducation.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}