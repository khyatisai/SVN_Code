package com.unfpa.appsistenciamaternaunfpa.database.dao.country_office

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryList

/**
 * Created by KhyatiShah on 2/12/2020.
 */
@Dao
interface CountryListDAO {

    @Query("SELECT * from country_list")
    fun getAllCountry(): List<CountryList>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(countryList: CountryList)
}