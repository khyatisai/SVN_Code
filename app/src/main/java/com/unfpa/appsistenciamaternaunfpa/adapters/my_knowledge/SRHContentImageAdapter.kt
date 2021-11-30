package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import com.unfpa.appsistenciamaternaunfpa.Mhealth
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.api_controller.ServiceVolley
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.ImageViewFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_image_list_srh_content.view.*

/**
 * Created by KhyatiShah on 8/23/2019.
 */
class SRHContentImageAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var imageList: List<String>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    private var service = ServiceVolley()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return SRHImageListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image_list_srh_content,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val imageViewHolder = viewHolder as SRHImageListViewHolder
        imageViewHolder.bindView(imageList.get(position))
    }

    inner class SRHImageListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(imageURL: String) {
            //remove all white spaces
            val strURL = (EndPoints.URL_ROOT + imageURL).replace("\\s".toRegex(), "")
            // itemView.networkImageView.setImageUrl(strURL, service.imageLoader)
            val imageLoader = ImageLoader.getInstance()
            imageLoader.displayImage(strURL, itemView.networkImageView, Mhealth.imageOptions)
            itemView.networkImageView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("url", strURL)

                var fragment = ImageViewFragment()
                fragment.arguments = bundle

                AppUtils.addFragment(activity, fragment, true, "")
            }
        }
    }


    fun setIamgeList(imageList: List<String>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }
}