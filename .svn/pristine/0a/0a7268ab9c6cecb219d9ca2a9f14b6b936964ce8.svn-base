package com.unfpa.appsistenciamaternaunfpa.database.dao.myservicedao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity

//added by 35251

@Dao
interface ServiceCenterDetailDAO {
    @Query("SELECT * from service_center_details where field_service_provided_1 like :nid")
    fun getAllContent(nid: String): List<ServiceCenterDetailEntity>

    @Query("SELECT * from service_center_details ")//ORDER BY field_latitude DESC
    fun getAllContentForSearchScreen(): List<ServiceCenterDetailEntity>

    @Insert/*(onConflict = OnConflictStrategy.IGNORE)*/
    fun insert(ServiceCenterDetailEntity: ServiceCenterDetailEntity)

    @Query("SELECT * from service_center_details WHERE (field_name LIKE '%' || :strSearch || '%' OR  field_address_field_1 LIKE '%' || :strSearch || '%' OR  field_address_field_2 LIKE '%' || :strSearch || '%' OR  field_country LIKE '%' || :strSearch || '%' OR  field_city LIKE '%' || :strSearch || '%' OR  field_municipal_region LIKE '%' || :strSearch || '%' OR  field_postal_code LIKE '%' || :strSearch || '%')")//
    fun getContentCenterListSearch( strSearch: String): List<ServiceCenterDetailEntity>

    @Query("DELETE FROM service_center_details")
    fun deleteAll()
}