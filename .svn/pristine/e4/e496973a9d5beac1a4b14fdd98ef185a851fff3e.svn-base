package com.unfpa.appsistenciamaternaunfpa.fragments.MyService


import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.*

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.CenterListRecyclerItemInfoViewAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_my_service_details_page.*

/**
 * A simple [Fragment] subclass.
 *
 */
@Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class MyServiceItemInfoFragment : androidx.fragment.app.Fragment() {

    lateinit var myServiceId: String
    lateinit var myServiceName: String
    lateinit var myServiceDesc: String
    private var currentlat: Double = 0.0
    private var currentlong: Double = 0.0
    val PREFS_FILENAME = "com.unfpa.appsistenciamaternaunfpa.fragments.prefs"
    var prefs: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_service_details_page, container, false)
        setHasOptionsMenu(true)
        myServiceId = HtmlCompat.fromHtml(
            (this.arguments!!.getString("myServiceId")?.toString()!!),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()
        myServiceName = HtmlCompat.fromHtml(
            (this.arguments!!.getString("myServiceName")?.toString()!!),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()
        myServiceDesc = HtmlCompat.fromHtml(
            (this.arguments!!.getString("myServiceDesc")?.toString()!!),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (activity as AppCompatActivity).supportActionBar?.title = myServiceName
            txtItemName.text = myServiceName
            txtItemDesc.text = myServiceDesc.replace("\n", "")
            txtFindMore.setOnClickListener {
                var frag = MyServiceCenterDetailsFragment()
                var bundle = Bundle()
                //bundle.putString("nid", myServiceCenterList.nid)
                bundle.putString("myServiceId1", myServiceId)
                frag.arguments = bundle
                AppUtils.addFragment(activity!!, frag, true, "")
            }
            recyclerViewCenterList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val serviceCenterDetailDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.serviceCenterDetailDAO()
            val listLiveMyServiceCenterData: List<ServiceCenterDetailEntity> =
                serviceCenterDetailDAO?.getAllContent("%$myServiceId%")
            val locationDifference = getLocationDifference(listLiveMyServiceCenterData)
            val sharedPreference = view.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val currentlat = sharedPreference.getString("latitude", "0.0")
            val currentlong = sharedPreference.getString("longitude", "0.0")

            if (locationDifference.isEmpty()) {
                txtNoDataAvailable.visibility = View.VISIBLE
                scrlContainer.visibility = View.GONE

            } else {
                txtNoDataAvailable.visibility = View.GONE
                scrlContainer.visibility = View.VISIBLE
                val myServiceCenterListAdapter =
                    CenterListRecyclerItemInfoViewAdapter(this.activity!!,
                        currentlat.toString(), currentlong.toString(), 4)
                recyclerViewCenterList.adapter = myServiceCenterListAdapter
                myServiceCenterListAdapter.setMyServiceCenterList(locationDifference, myServiceId)
            }
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
                //serviceCenterDetailEntity.field_latitude = strDistance
                serviceCenterDetailEntity.distance = strDistance
                tempList.add(serviceCenterDetailEntity)
                /*if (strDistance.toDouble() <= 5) {//12866

                }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return tempList.sortedWith(compareBy { it.distance })
    }

}
