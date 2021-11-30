package com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.QuizRequest

/**
 * Created by KhyatiShah on 9/25/2019.
 */
@Dao
interface QuizRequestDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(quizRequest: QuizRequest)

    @Query("SELECT * from quiz_request")
    fun getAllQuiz(): List<QuizRequest>

    @Query("SELECT * from quiz_request WHERE categoryId = :categoryId")
    fun getQuizCategoryWise(categoryId: String): List<QuizRequest>

    @Query("SELECT * from quiz_request WHERE categoryId = :categoryId AND quizId = :quizId")
    fun getQuizQuizIdWise(categoryId: String, quizId: String): QuizRequest

    @Query("DELETE FROM quiz_request")
    fun deleteAll()
}