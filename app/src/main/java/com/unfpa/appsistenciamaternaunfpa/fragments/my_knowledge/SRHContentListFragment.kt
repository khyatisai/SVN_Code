package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentListAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_srh_content_list.*
import java.lang.Exception

/**
 * Created by KhyatiShah on 8/19/2019.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SRHContentListFragment() : androidx.fragment.app.Fragment() {

    lateinit var categoryId: String
    lateinit var categoryName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_srh_content_list, container, false)
        setHasOptionsMenu(true)
        categoryId = this.arguments!!.getString("categoryId").toString()
        categoryName = this.arguments!!.getString("categoryName").toString()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = categoryName
            lstSRHContent.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
            val contentMasterDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO()
            val listcontentData: List<SRHContent> = contentMasterDAO?.getContentCategoryWise(categoryId)
            val contentAdapter = SRHContentListAdapter(this.activity!!)
            lstSRHContent.adapter = contentAdapter
            contentAdapter.setContentList(listcontentData)

            if (listcontentData.isNotEmpty()) {
                linearLayoutNoArticle.visibility = View.GONE
                linearLayoutSearchArticles.visibility = View.VISIBLE
            } else {
                linearLayoutNoArticle.visibility = View.VISIBLE
                linearLayoutSearchArticles.visibility = View.GONE
            }
            edtSearch.addTextChangedListener((object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    if (!TextUtils.isEmpty(p0.toString())) {
                        val lstSearch = contentMasterDAO?.getContentListSearch(categoryName, p0.toString())
                        contentAdapter.setContentList(lstSearch)
                        //Logging firebase screen
                        AppUtils.trackScreen(Constant.SRH_CONTENT_SEARCH, activity!!)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            }))

        } catch (e: Exception) {
            e.printStackTrace()
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
