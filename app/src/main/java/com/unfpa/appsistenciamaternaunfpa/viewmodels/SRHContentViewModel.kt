package com.unfpa.appsistenciamaternaunfpa.viewmodels

import androidx.lifecycle.ViewModel
import android.content.Context
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent

class SRHContentViewModel(context: Context) : ViewModel() {

    private val listLiveData: List<SRHContent>

    init {
        val mHealthRoomDB = MhealthRoomDatabase.getAppDataBase(context)
        val contentMasterDAO = mHealthRoomDB?.contentMasterDAO()!!
        listLiveData = contentMasterDAO?.getAllContent()
    }

    fun getSRHContentMaster(): List<SRHContent> {
        return listLiveData
    }

}