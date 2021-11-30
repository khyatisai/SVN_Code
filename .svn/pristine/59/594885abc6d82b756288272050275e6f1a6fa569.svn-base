package com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory

/**
 * Created by KhyatiShah on 8/16/2019.
 */
@Dao
interface SRHContentCategoryDAO {

    @Query("SELECT * from content_category")
    fun getCategories(): List<SRHContentCategory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contentCategory: SRHContentCategory)

    @Query("DELETE FROM content_category")
    fun deleteAll()

}