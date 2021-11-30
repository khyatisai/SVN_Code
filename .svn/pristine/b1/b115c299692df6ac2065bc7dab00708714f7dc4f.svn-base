package com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.QuizResponse

/**
 * Created by KhyatiShah on 10/9/2019.
 */
@Dao
interface QuizResponseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(quizResponse: QuizResponse)

    @Query("SELECT * from quiz_response WHERE categoryId = :categoryId AND quizId = :quizId")
    fun getAllResponse(quizId: String, categoryId: String): List<QuizResponse>
}