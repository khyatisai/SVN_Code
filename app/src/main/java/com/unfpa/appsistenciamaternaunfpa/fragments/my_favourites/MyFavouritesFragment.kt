package com.unfpa.appsistenciamaternaunfpa.fragments.my_favourites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_favourites.SRHContentListAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import kotlinx.android.synthetic.main.fragment_quiz_list.*
import kotlinx.android.synthetic.main.fragment_srh_content_list.*

/**
 * Created by KhyatiShah on 9/30/2019.
 */
class MyFavouritesFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_my_favourites, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.menu_my_favourites)
            lstSRHContent.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
            val contentMasterDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO()
            val listSRHContent: List<SRHContent> = contentMasterDAO?.getFavourite()

            val contentAdapter = SRHContentListAdapter(this.activity!!)
            lstSRHContent.adapter = contentAdapter
            contentAdapter.setContentList(listSRHContent)

            if (listSRHContent.isEmpty()) {
                lstSRHContent.visibility = View.GONE
                txtNoItems.visibility = View.VISIBLE
            } else {
                lstSRHContent.visibility = View.VISIBLE
                txtNoItems.visibility = View.GONE
            }

            edtSearch.addTextChangedListener((object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    val lstSearch = contentMasterDAO?.getFavouriteSearched(p0.toString())
                    contentAdapter.setContentList(lstSearch)
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