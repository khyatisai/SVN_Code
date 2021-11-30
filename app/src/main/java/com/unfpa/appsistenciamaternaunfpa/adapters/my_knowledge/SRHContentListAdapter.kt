package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.SRHContentDetailFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_srh_content_list.view.*

/**
 * Created by KhyatiShah on 8/19/2019.
 */
class SRHContentListAdapter(activity: androidx.fragment.app.FragmentActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var contentList: List<SRHContent>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return ContentListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_srh_content_list,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val contentViewHolder = viewHolder as ContentListViewHolder
        contentViewHolder.bindView(contentList.get(position))
    }

    inner class ContentListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(contentMaster: SRHContent) {
            itemView.txtHeader.text = HtmlCompat.fromHtml(contentMaster.title.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            itemView.txtShortDesc.text = HtmlCompat.fromHtml(contentMaster.field_short_description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            itemView.cardSRHContent.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("contentId", contentMaster.nid)
                var frag = SRHContentDetailFragment()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }

    }

    fun setContentList(contentList: List<SRHContent>) {
        this.contentList = contentList
        notifyDataSetChanged()
    }
}