package com.unfpa.appsistenciamaternaunfpa.database.dao.personal

import androidx.room.*
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryOfficeSettingEntity

//added by 35251

@Dao
interface CountryOfficeDAO {
    @Query("SELECT * from country_office_listing")
    fun getAllContent(): List<CountryOfficeSettingEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(countryOfficeSettingEntity: CountryOfficeSettingEntity)

    @Query("SELECT * from country_office_listing WHERE field_country = :CountryCode")
    fun getModuleVisibility(CountryCode: String): CountryOfficeSettingEntity

    @Query("DELETE FROM country_office_listing")
    fun deleteAll()
}