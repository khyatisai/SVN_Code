package com.unfpa.appsistenciamaternaunfpa.database.dao.myvoice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity

//added by 35251

@Dao
interface MyVoiceDAO {
    @Query("SELECT * from my_voice")
    fun getAllContent(): List<MyVoiceEntity>

    /*@Query("SELECT * from my_voice WHERE(story_mode LIKE '%' || :story_mode || '%') ")
    fun update(story_mode:String): String*/

    @Update
    fun update(MyVoiceEntity: MyVoiceEntity)

    @Insert
    fun insert(MyVoiceEntity: MyVoiceEntity)

    @Query("DELETE FROM my_services")
    fun deleteAll()
}