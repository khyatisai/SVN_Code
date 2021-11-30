package com.unfpa.appsistenciamaternaunfpa.database.entity.my_health

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KhyatiShah on 10/16/2019.
 */
@Entity(tableName = "medicine_reminder")
data class MedicineReminder(
    var date: String,
    var time: String,
    var withFood: Boolean,
    var medicineName: String,
    var dose: Int,
    var days: Int,
    var reminderBefore: String,
    var eventId: Long = 0L,
    var eventURI: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var medicineReminderId: Int = 0

}