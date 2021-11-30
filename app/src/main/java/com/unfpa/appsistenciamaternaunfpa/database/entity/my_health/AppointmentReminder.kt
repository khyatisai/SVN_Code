package com.unfpa.appsistenciamaternaunfpa.database.entity.my_health

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KhyatiShah on 10/22/2019.
 */
@Entity(tableName = "appointment_reminder")
data class AppointmentReminder(
    var appointmentName: String,
    var serviceCenterId: String,
    var serviceCenterName: String,
    var time: String,
    var date: String,
    var eventURI: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var appointmentId: Int = 0
}