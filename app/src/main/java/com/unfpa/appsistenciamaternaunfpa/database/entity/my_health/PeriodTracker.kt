package com.unfpa.appsistenciamaternaunfpa.database.entity.my_health

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KhyatiShah on 10/1/2019.
 */
@Entity(tableName = "period_master")
data class PeriodTracker(

    var startDate: String,
    var endDate: String,
    var periodLength: Int,
    var avgCycle: Int
) {
    @PrimaryKey(autoGenerate = true)
    var periodId: Int = 0
}