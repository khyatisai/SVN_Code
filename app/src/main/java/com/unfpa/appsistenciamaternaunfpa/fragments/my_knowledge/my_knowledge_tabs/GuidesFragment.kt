package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.my_knowledge_tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHCategoryAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory
import kotlinx.android.synthetic.main.fragment_my_knowledge_guides.*


class GuidesFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_knowledge_guides, container, false)
        // Inflate the layout for this fragment
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            /*lstCategory.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                this.context,
                2
            ) as androidx.recyclerview.widget.RecyclerView.LayoutManager?*/
            //srhCategoryViewModel = ViewModelProviders.of(this.activity!!).get(SRHCategoryViewModel::class.java)
            lstCategory.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
            val srhContentCategoryDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.SRHContentCategoryDAO()
            val listLiveData: List<SRHContentCategory> = srhContentCategoryDAO?.getCategories()

            val categoryAdapter = SRHCategoryAdapter(this.activity!!)
            lstCategory.adapter = categoryAdapter
            categoryAdapter.setCategoryList(listLiveData)

            if (listLiveData.isNotEmpty()) {
                linearLayoutNoArticleGuides.visibility = View.GONE
                lstCategory.visibility = View.VISIBLE
            } else {
                linearLayoutNoArticleGuides.visibility = View.VISIBLE
                lstCategory.visibility = View.GONE
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
