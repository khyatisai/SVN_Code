package com.unfpa.appsistenciamaternaunfpa.database.dao.personal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unfpa.appsistenciamaternaunfpa.database.entity.personaldetails.PersonalDetailsEntity

//added by 35251

@Dao
interface PersonalDetailsDAO {
    @Query("SELECT * from personal_details")
    fun getAllContent(): List<PersonalDetailsEntity>

    /*@Query("SELECT * from my_voice WHERE(story_mode LIKE '%' || :story_mode || '%') ")
    fun update(story_mode:String): String*/

    @Update
    fun update(PersonalDetailsEntity: PersonalDetailsEntity)

    @Insert
    fun insert(PersonalDetailsEntity: PersonalDetailsEntity)

    @Query("DELETE FROM personal_details")
    fun deleteAll()
}