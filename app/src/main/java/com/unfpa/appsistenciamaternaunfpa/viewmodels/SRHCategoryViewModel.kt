package com.unfpa.appsistenciamaternaunfpa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory

/**
 * Created by KhyatiShah on 8/16/2019.
 */
public class SRHCategoryViewModel(application: Application) : AndroidViewModel(application) {
    val listLiveData: List<SRHContentCategory>

    init {
        val mHealthRoomDB = MhealthRoomDatabase.getAppDataBase(application)
        val srhCategoryDAO = mHealthRoomDB?.SRHContentCategoryDAO()!!
        listLiveData = srhCategoryDAO?.getCategories()
        val count = listLiveData.size
    }

   /* public fun getSRHCategories(): LiveData<List<SRHContentCategory>> {
        return listLiveData
    }*/
}