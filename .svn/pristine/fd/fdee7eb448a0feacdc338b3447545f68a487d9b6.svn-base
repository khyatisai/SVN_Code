package com.unfpa.appsistenciamaternaunfpa.fragments.MyService

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.*

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.CenterListRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_my_service_search_center.*

@Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class MyServiceCenterDetailsFragment : androidx.fragment.app.Fragment() {
    var myServiceId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_service_search_center, container, false)
        setHasOptionsMenu(true)
        myServiceId = arguments!!.getString("myServiceId1").toString()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.search_center)
            recyclerViewCenterListLocation.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val serviceCenterDetailDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.serviceCenterDetailDAO()
            val listLiveMyServiceCenterData: List<ServiceCenterDetailEntity> =
                serviceCenterDetailDAO?.getAllContentForSearchScreen()
            val locationDifference = getLocationDifference(listLiveMyServiceCenterData)
            val sharedPreference = view.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val currentlat = sharedPreference.getString("latitude", "0.0")
            val currentlong = sharedPreference.getString("longitude", "0.0")
            val myServiceCenterListAdapter = CenterListRecyclerViewAdapter(
                this.activity!!,
                currentlat.toString(),
                currentlong.toString(),
                listLiveMyServiceCenterData.size
            )
            recyclerViewCenterListLocation.adapter = myServiceCenterListAdapter
            myServiceCenterListAdapter.setMyServiceCenterList(locationDifference, myServiceId)

            edtSearchLocation.addTextChangedListener((object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    //Logging firebase screen
                    AppUtils.trackScreen(Constant.SERVICE_PROVIDER_SEARCH, activity as AppCompatActivity)
                    //val lstSearch = ServiceCenterDetailDAO
                    val lstSearch = serviceCenterDetailDAO?.getContentCenterListSearch(p0.toString())
                    val locationDifference = getLocationDifference(lstSearch)
                    myServiceCenterListAdapter.setMyServiceCenterList(locationDifference, myServiceId)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            }))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLocationDifference(listLiveMyServiceCenterData: List<ServiceCenterDetailEntity>): List<ServiceCenterDetailEntity> {
        val startPoint = Location("locationA")
        val tempList: ArrayList<ServiceCenterDetailEntity> = ArrayList<ServiceCenterDetailEntity>()
        for (serviceCenterDetailEntity in listLiveMyServiceCenterData) {
            try {
                startPoint.latitude = serviceCenterDetailEntity.field_latitude.toDouble()
                startPoint.longitude = serviceCenterDetailEntity.field_longitude.toDouble()
                val endPoint = Location("locationB")
                val sharedPreference = context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val currentlat = sharedPreference.getString("latitude", "0.0")?.toDouble()
                val currentlong = sharedPreference.getString("longitude", "0.0")?.toDouble()
                if (currentlat != null) {
                    endPoint.latitude = currentlat
                }
                if (currentlong != null) {
                    endPoint.longitude = currentlong
                }
                val distance = startPoint.distanceTo(endPoint).toDouble()
                val distanceInKiloMeter = distance * 0.001
                val strDistance = "%.2f".format(distanceInKiloMeter)
                serviceCenterDetailEntity.field_latitude = strDistance
                tempList.add(serviceCenterDetailEntity)
                /*if (strDistance.toDouble() <= 5) {//12866

                }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return tempList.sortedWith(compareBy { it.field_latitude })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    companion object {
        fun newInstance(): MyServiceCenterDetailsFragment {
            return MyServiceCenterDetailsFragment()
        }
    }
}
