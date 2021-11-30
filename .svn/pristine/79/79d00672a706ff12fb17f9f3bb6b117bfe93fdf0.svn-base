package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.api_controller.ServiceVolley
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import android.os.Bundle
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.VideoViewFragment
import kotlinx.android.synthetic.main.item_video_list_srh_content.view.*


/**
 * Created by KhyatiShah on 8/26/2019.
 */
class SRHContentVideoListAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var imageList: List<String>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    private var service = ServiceVolley()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return SRHImageListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.unfpa.appsistenciamaternaunfpa.R.layout.item_video_list_srh_content,
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
            try {
                //val thumbnailURL = AppUtils.getThubnailURL(imageURL.replace("\\s".toRegex(), ""))
                //remove all white spaces
                //val strURL = (EndPoints.URL_ROOT + imageURL).replace("\\s".toRegex(), "")
                //itemView.networkImageView.setImageUrl(thumbnailURL, service.imageLoader)
                itemView.networkImageView.setOnClickListener {
                   /* val videoClient = Intent(Intent.ACTION_VIEW)
                    videoClient.data = Uri.parse(imageURL.replace("\\s".toRegex(), ""))
                    activity.startActivityForResult(videoClient, 1234)*/

                    val bundle = Bundle()
                    bundle.putString("url", imageURL.replace("\\s".toRegex(), ""))

                    var fragment = VideoViewFragment()
                    fragment.arguments = bundle

                    AppUtils.addFragment(activity, fragment, true, "")

                    //AppUtils.showVideoDialog(activity, imageURL.replace("\\s".toRegex(), ""))

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun setIamgeList(imageList: List<String>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }
}