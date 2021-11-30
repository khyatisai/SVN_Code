package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.my_knowledge_tabs.ClassFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.my_knowledge_tabs.GuidesFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.my_knowledge_tabs.HotTopicsFragment
import com.unfpa.appsistenciamaternaunfpa.viewmodels.SRHCategoryViewModel
import java.lang.Exception

/**
 * Created by KhyatiShah on 8/16/2019.
 */
class MyKnowledgeFragment : androidx.fragment.app.Fragment() {

    lateinit var srhCategoryViewModel: SRHCategoryViewModel
    private var tabLayout: TabLayout? = null
    var viewPager: androidx.viewpager.widget.ViewPager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_my_knowledge, container, false)
        setHasOptionsMenu(true)
        viewPager = rootView.findViewById(R.id.viewpager) as androidx.viewpager.widget.ViewPager
        setupViewPager(viewPager!!)
        tabLayout = rootView.findViewById(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
        return rootView
    }
    private fun setupViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(HotTopicsFragment(), activity!!.getString(R.string.hot_topics_tab))//"Hot Topics"
        adapter.addFragment(GuidesFragment(), activity!!.getString(R.string.guides_tab))//"Guides"
        adapter.addFragment(ClassFragment(), activity!!.getString(R.string.class_tab))//"Class"
        viewPager.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.menu_my_knowledge)


            /*lstCategory.layoutManager = GridLayoutManager(this.context, 2) as RecyclerView.LayoutManager?
            //srhCategoryViewModel = ViewModelProviders.of(this.activity!!).get(SRHCategoryViewModel::class.java)
            val srhContentCategoryDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.SRHContentCategoryDAO()
            val listLiveData: List<SRHContentCategory> = srhContentCategoryDAO?.getCategories()

            val categoryAdapter = SRHCategoryAdapter(this.activity!!)
            lstCategory.adapter = categoryAdapter
            categoryAdapter.setCategoryList(listLiveData)*/

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

    internal inner class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}