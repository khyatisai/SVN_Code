package com.unfpa.appsistenciamaternaunfpa.adapters.my_service

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.MyServiceCenterDetails
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_myservice_details_page_list_content.view.*
import java.text.SimpleDateFormat
import java.util.*


class CenterListRecyclerViewAdapter(activity: androidx.fragment.app.FragmentActivity, currentlat:String, currentlong:String, count:Int) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var myServiceCenterList: List<ServiceCenterDetailEntity>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    private var currentlat: String = currentlat
    private var currentlong: String = currentlong
    private var count: Int = count
    lateinit var myServiceId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        return MyServiceCenterListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_myservice_details_page_list_content,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val myServiceCenterListViewHolder = viewHolder as MyServiceCenterListViewHolder
        myServiceCenterListViewHolder.bindView(myServiceCenterList[position])

    }

    override fun getItemCount(): Int = myServiceCenterList.size//count

    inner class MyServiceCenterListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(myServiceCenterList: ServiceCenterDetailEntity) {
            //itemView.imgMyServiceListItem = myServiceList.title
            val address = myServiceCenterList.field_address_field_1 +","+ myServiceCenterList.field_address_field_2+"," + myServiceCenterList.field_country+"," + myServiceCenterList.field_city
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val abc = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)


            //get location
            val startPoint = Location("locationA")
            startPoint.latitude =  myServiceCenterList.field_latitude.toDouble()
            startPoint.longitude = myServiceCenterList.field_longitude.toDouble()
            val endPoint = Location("locationB")
            endPoint.latitude = currentlat.toDouble()
            endPoint.longitude = currentlong.toDouble()
            val distance = startPoint.distanceTo(endPoint).toDouble()
            val distanceInKiloMeter = distance * 0.001
            val strDistance = "%.2f".format(distanceInKiloMeter)

            val operationalHours = HtmlCompat.fromHtml(myServiceCenterList.field_hours_of_operation, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            val resultHours: List<String> = operationalHours.split("\n").map { it.trim() }
            for(item in resultHours){
                var i = item.indexOf(" ")
                var word = item.substring(0, i)
                var rest = item.substring(i + 1)
                if(word.contains(abc)){
                    itemView.txtCentersTodayTiming.text = "Today, $rest"
                }
            }

                itemView.txtCenterHead.text = myServiceCenterList.field_name
                itemView.txtCenterAddress.text = address
                //itemView.txtDistance.text = strDistance + "km"
                itemView.txtDistance.text = myServiceCenterList.field_latitude + "km"
                itemView.txtCentersTodayTiming.text


                itemView.cardViewCenter.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("nid", myServiceCenterList.nid)
                bundle.putString("field_name", myServiceCenterList.field_name)
                bundle.putString("field_address", address)
                bundle.putString("field_telephone", myServiceCenterList.field_telephone)
                bundle.putString("operationalHours", operationalHours)
                bundle.putString("field_service_provided", myServiceCenterList.field_service_provided)
                bundle.putString("field_service_provided_1", myServiceCenterList.field_service_provided_1)
                bundle.putString("uuid", myServiceCenterList.uuid)
                bundle.putString("field_primary_doctor", myServiceCenterList.field_primary_doctor)
                bundle.putString("field_primary_nurse", myServiceCenterList.field_primary_nurse)
                bundle.putString("field_lat", myServiceCenterList.field_latitude)
                bundle.putString("field_long", myServiceCenterList.field_longitude)
                var frag = MyServiceCenterDetails()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }
    }
    fun removeItem(position: Int) {
        //myServiceCenterList.
    }

    fun setMyServiceCenterList(myServiceCenterList: List<ServiceCenterDetailEntity>, myServiceId: String
    ) {
        this.myServiceCenterList = myServiceCenterList
        this.myServiceId = myServiceId
        notifyDataSetChanged()
    }
    fun setContentList(contentList: List<ServiceCenterDetailEntity>) {
        this.myServiceCenterList = contentList
        notifyDataSetChanged()
    }
}
