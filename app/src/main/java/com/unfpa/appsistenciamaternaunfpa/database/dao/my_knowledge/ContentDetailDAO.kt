package com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.ContentDetail

@Dao
interface ContentDetailDAO {

    @Query("SELECT * from content_detail")
    fun getAllContent(): List<ContentDetail>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contentMaster: ContentDetail)

    @Query("DELETE FROM content_detail")
    fun deleteAll()


}