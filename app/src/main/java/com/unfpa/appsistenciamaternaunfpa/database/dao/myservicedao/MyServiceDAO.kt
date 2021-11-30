package com.unfpa.appsistenciamaternaunfpa.database.dao.myservicedao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity

//added by 35251

@Dao
interface MyServiceDAO {
    @Query("SELECT * from my_services")
    fun getAllContent(): List<MyServiceEntity>

    @Query("SELECT uuid from my_services WHERE nid=:nid ")
    fun getServiceUuid(nid:String): String

    @Insert/*(onConflict = OnConflictStrategy.IGNORE)*/
    fun insert(myServiceEntity: MyServiceEntity)

    @Query("SELECT * from my_services WHERE (title LIKE '%' || :strSearch || '%' OR  body LIKE '%' || :strSearch || '%')")//
    fun getContentListSearch( strSearch: String): List<MyServiceEntity>

    @Query("DELETE FROM my_services")
    fun deleteAll()
}