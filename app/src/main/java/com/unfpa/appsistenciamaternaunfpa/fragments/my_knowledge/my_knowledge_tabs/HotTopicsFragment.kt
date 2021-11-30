package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.my_knowledge_tabs

import android.os.Bundle
import android.view.*

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentListAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import kotlinx.android.synthetic.main.fragment_my_knowledge_hot_topics.*


class HotTopicsFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_knowledge_hot_topics, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            lstSRHContent.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            val contentMasterDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO()

            val listcontentData: List<SRHContent> = contentMasterDAO?.getContentByHotTopics()
            val contentAdapter = SRHContentListAdapter(this.activity!!)
            lstSRHContent.adapter = contentAdapter
            contentAdapter.setContentList(listcontentData)


            if (listcontentData.isNotEmpty()) {
                linearLayoutNoArticleHotTopics.visibility = View.GONE
                lstSRHContent.visibility = View.VISIBLE
            } else {
                linearLayoutNoArticleHotTopics.visibility = View.VISIBLE
                lstSRHContent.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
