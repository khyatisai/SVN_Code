package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_health.CenterListAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_center_list.*

/**
 * Created by KhyatiShah on 10/22/2019.
 */
class CenterListActivity : AppCompatActivity(), Listener {


    private var toolbar: Toolbar? = null

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_list)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.SelectCenter)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                setResult(2, intent);
                finish()
            }
        })

        lstServiceCenter.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val serviceCenterDetailDAO =
            MhealthRoomDatabase.getAppDataBase(this)!!.serviceCenterDetailDAO()
        val listLiveMyServiceCenterData: List<ServiceCenterDetailEntity> =
            serviceCenterDetailDAO?.getAllContentForSearchScreen()
        val sharedPreference = this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val currentlat = sharedPreference.getString("latitude", "0.0")
        val currentlong = sharedPreference.getString("longitude", "0.0")
        val myServiceCenterListAdapter =
            CenterListAdapter(currentlat.toString(), currentlong.toString(), this)
        lstServiceCenter.adapter = myServiceCenterListAdapter
        val locationDifference = getLocationDifference(listLiveMyServiceCenterData)
        myServiceCenterListAdapter.setMyServiceCenterList(locationDifference)
    }

    private fun getLocationDifference(listLiveMyServiceCenterData: List<ServiceCenterDetailEntity>): List<ServiceCenterDetailEntity> {
        val startPoint = Location("locationA")
        val tempList: ArrayList<ServiceCenterDetailEntity> = ArrayList<ServiceCenterDetailEntity>()
        for (serviceCenterDetailEntity in listLiveMyServiceCenterData) {
            try {
                startPoint.latitude = serviceCenterDetailEntity.field_latitude.toDouble()
                startPoint.longitude = serviceCenterDetailEntity.field_longitude.toDouble()
                val endPoint = Location("locationB")
                val sharedPreference = this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val currentlat =
                    sharedPreference.getString("latitude", "0.0")?.toDouble()
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val currentlong =
                    sharedPreference.getString("longitude", "0.0")?.toDouble()
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

    override fun onComplete(response: String) {
        val intent = Intent()
        intent.putExtra(Constant.CENTER_ID, response.split(",").get(0))
        intent.putExtra(Constant.CENTER_NAME, response.split(",").get(1))
        setResult(1, intent);
        finish()
    }
}