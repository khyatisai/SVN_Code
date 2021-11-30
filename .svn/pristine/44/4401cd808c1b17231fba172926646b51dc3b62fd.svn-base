package com.unfpa.appsistenciamaternaunfpa.fragments.introductory


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.TypeForCountry
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryList
import kotlinx.android.synthetic.main.fragment_slide_b_country.*

/**
 * A simple [Fragment] subclass.
 */
class SlideBCountryFragment : androidx.fragment.app.Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_slide_b_country, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerCountry.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)

            val countryListDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.countryListDAO()
            val listCountry: List<CountryList> = countryListDAO?.getAllCountry()
            val arrTypeForCountry = ArrayList<TypeForCountry>()
            for (i in listCountry) {
                arrTypeForCountry.add(TypeForCountry(i.countryName, i.countryCode, false))
            }
            /*val adapter = IntroCountryAdapter(
                arrTypeForCountry,
                this.activity!!,
                Constant.ITEM_COUNTRY,
                Constant.ITEM_COUNTRY_CODE
            )*/
            /* val adapter = IntroCountryAdapter(
                 (AppUtils.getCountryList()) as ArrayList<TypeForCountry>,
                 this.activity!!,
                 Constant.ITEM_COUNTRY,
                 Constant.ITEM_COUNTRY_CODE
             )*/
            // recyclerCountry.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}