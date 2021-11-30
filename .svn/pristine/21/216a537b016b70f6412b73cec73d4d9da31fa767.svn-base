package com.unfpa.appsistenciamaternaunfpa.database.dao.my_health

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.PeriodTracker

/**
 * Created by KhyatiShah on 10/1/2019.
 */

@Dao
interface PeriodTrackerDAO {

    @Query("SELECT * from period_master")
    fun getAllPeriods(): List<PeriodTracker>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(period: PeriodTracker)

    @Query("SELECT * FROM period_master ORDER BY periodId DESC LIMIT 1")
    fun getLastPeriod(): PeriodTracker

    @Query("UPDATE period_master SET endDate = :endDate, periodLength =:periodLength  WHERE periodId= :periodId")
    fun upDateEndDate(endDate: String, periodLength: Int, periodId: Int)

    @Query("DELETE FROM period_master")
    fun deleteAll()
}