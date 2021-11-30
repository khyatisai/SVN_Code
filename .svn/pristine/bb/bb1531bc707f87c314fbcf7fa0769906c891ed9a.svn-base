package com.unfpa.appsistenciamaternaunfpa.fragments.MyService

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_service.MyServiceRecyclerViewAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity

import kotlinx.android.synthetic.main.fragment_myservicelistitem_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class MyServiceFragment : androidx.fragment.app.Fragment() {
    val PREFS_FILENAME = "com.unfpa.appsistenciamaternaunfpa.fragments.prefs"
    var prefs: SharedPreferences? = null
    lateinit var nid: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101
    var mLocationRequest: LocationRequest? = null
    val UPDATE_INTERVAL = (10 * 1000).toLong() /* 10 secs */
    val FASTEST_INTERVAL:Long = 2000 /* 2 sec */
    val dontAllowLocationPermission:Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_myservicelistitem_list, container, false)
        setHasOptionsMenu(true)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = UPDATE_INTERVAL
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL

        return rootView
    }
    /*override fun onResume() {
        super.onResume()
        try {
            (activity as AppCompatActivity).supportActionBar?.title = "My Service"
            setupPermissions()
        }
        catch (e:Exception){}
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1001){
            getServiceList()
        }
    }

    private fun obtainLocation(){
    try{
        getFusedLocationProviderClient(this.activity!!).requestLocationUpdates(mLocationRequest, object:
            LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // do work here
                if(activity != null && isAdded){
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val ab: Double = location?.latitude!!.toDouble()
                        val ba: Double = location.longitude
                        if (activity != null && isAdded) {
                            val sharedPreference = activity!!.getSharedPreferences(
                                "PREFERENCE_NAME",
                                Context.MODE_PRIVATE
                            )
                            var editor = sharedPreference.edit()
                            editor.putString("latitude", ab.toString())
                            editor.putString("longitude", ba.toString())
                            editor.apply()
                            editor.commit()
                        }
                    }
                }
            }
        },
            Looper.myLooper())
    }catch (e:Exception){
        e.printStackTrace()
    }

    }

    private fun setupPermissions() {
        try{
            val permission1 = ContextCompat.checkSelfPermission(this.activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            val permission2 = ContextCompat.checkSelfPermission(this.activity!!, Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED )  {
                Log.i(TAG, "Permission to location denied")
                makeRequest()
            } else{
                statusCheck()
                //getServiceList()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun statusCheck() {
        val manager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            getServiceList()
            // Toast.makeText(activity,"Permission has been granted by user", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Permission has been granted by user")
        }
        else{
            buildAlertMessageNoGps()
        }
    }
    private fun buildAlertMessageNoGps() {
        try{
            if(activity != null && isAdded) {
                val builder = AlertDialog.Builder(this.activity!!)
                builder.setMessage(activity!!.getString(R.string.gps_disabled_enable_it))
                .setCancelable(false)
                    .setPositiveButton(
                        activity!!.getString(R.string.dialog_yes)
                    ) { dialog, id ->
                        dialog.dismiss()
                        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1001)
                    }
                    .setNegativeButton(
                        activity!!.getString(R.string.dialog_no)
                    ) { dialog, id ->
                        dialog.dismiss()
                        getServiceList()
                    }
                val alert = builder.create()
                alert.show()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getServiceList(){


        obtainLocation()

        recyclerMyServiceList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity)
        val myServiceDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.myServiceDAO()
        val listLiveMyServiceData: List<MyServiceEntity> = myServiceDAO?.getAllContent()
        //recyclerMyServiceList.adapter =

        val myServiceAdapter =
            MyServiceRecyclerViewAdapter(this.activity!!)
        recyclerMyServiceList.adapter = myServiceAdapter
        myServiceAdapter.setMyServiceList(listLiveMyServiceData)


        edtSearchService.addTextChangedListener((object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val lstSearch = myServiceDAO?.getContentListSearch( p0.toString())
                myServiceAdapter.setContentList(lstSearch)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }))
    }
    private fun makeRequest() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION ), RECORD_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                Toast.makeText(activity,activity!!.getString(R.string.permission_has_been_denied_by_user), Toast.LENGTH_SHORT).show()
                                Log.i(TAG, "Permission has been denied by user")
                                val builder = AlertDialog.Builder(this.activity!!)
                                builder.setMessage(activity!!.getString(R.string.permission_access_the_nearby_centers))
                                    .setTitle(activity!!.getString(R.string.permission_required))
                                builder.setPositiveButton(activity!!.getString(R.string.allow)
                                ) { dialog, id ->
                                    Log.i(TAG, "Clicked Allow")
                                    makeRequest()
                                }
                                builder.setNegativeButton(activity!!.getString(R.string.cancel_1)){ dialog, id ->
                                    Log.i(TAG, "Clicked Cancel")
                                    val sharedPreference =  activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                                    var editor = sharedPreference.edit()
                                    editor.putBoolean("flag1", true)
                                    editor.apply()
                                    editor.commit()
                                    getServiceList()
                                }
                                builder.setCancelable(false)
                                val dialog = builder.create()
                                dialog.show()
                                //setupPermissions()
                    }else{
                        getServiceList()
                    }
                } else {
                    statusCheck()
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.menu_my_services)
        val sharedPreference =  view.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val deniedLocationPermission = sharedPreference.getBoolean("flag1",    false)
        if(!deniedLocationPermission){
            setupPermissions()
        }else{
        getServiceList()
    }
    }

    companion object {
        fun newInstance() : MyServiceFragment {
            return MyServiceFragment()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }
}

