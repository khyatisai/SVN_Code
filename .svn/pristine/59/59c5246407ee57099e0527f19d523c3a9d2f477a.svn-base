package com.unfpa.appsistenciamaternaunfpa.adapters.my_health

import android.annotation.SuppressLint
import android.location.Location
import androidx.core.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.fragment_myservice_details_page_list_content.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by KhyatiShah on 10/22/2019.
 */
class CenterListAdapter(currentlat: String, currentlong: String, listener: Listener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var myServiceCenterList: List<ServiceCenterDetailEntity>
    private var currentlat: String = currentlat
    private var currentlong: String = currentlong
    private var listener = listener

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
            val address =
                myServiceCenterList.field_address_field_1 + "," + myServiceCenterList.field_address_field_2 + "," + myServiceCenterList.field_country + "," + myServiceCenterList.field_city
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val abc = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)


            //get location
            val startPoint = Location("locationA")
            startPoint.latitude = myServiceCenterList.field_latitude.toDouble()
            startPoint.longitude = myServiceCenterList.field_longitude.toDouble()
            val endPoint = Location("locationB")
            endPoint.latitude = currentlat.toDouble()
            endPoint.longitude = currentlong.toDouble()
            val distance = startPoint.distanceTo(endPoint).toDouble()
            val distanceInKiloMeter = distance * 0.001
            val strDistance = "%.2f".format(distanceInKiloMeter)

            val operationalHours =
                HtmlCompat.fromHtml(myServiceCenterList.field_hours_of_operation, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString()
            val resultHours: List<String> = operationalHours.split("\n").map { it.trim() }
            for (item in resultHours) {
                var i = item.indexOf(" ")
                var word = item.substring(0, i)
                var rest = item.substring(i + 1)
                if (word.contains(abc)) {
                    itemView.txtCentersTodayTiming.text = "Today, $rest"
                }
            }

            itemView.txtCenterHead.text = myServiceCenterList.field_name
            itemView.txtCenterAddress.text = address
            //itemView.txtDistance.text = strDistance + "km"
            itemView.txtDistance.text = myServiceCenterList.field_latitude + "km"
            itemView.txtCentersTodayTiming.text


            itemView.cardViewCenter.setOnClickListener {
                listener.onComplete(myServiceCenterList.nid + "," + myServiceCenterList.field_name)
            }
        }
    }

    fun removeItem(position: Int) {
        //myServiceCenterList.
    }

    fun setMyServiceCenterList(
        myServiceCenterList: List<ServiceCenterDetailEntity>
    ) {
        this.myServiceCenterList = myServiceCenterList
        notifyDataSetChanged()
    }

    fun setContentList(contentList: List<ServiceCenterDetailEntity>) {
        this.myServiceCenterList = contentList
        notifyDataSetChanged()
    }
}