package com.unfpa.appsistenciamaternaunfpa.database.dao.my_health

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder

/**
 * Created by KhyatiShah on 10/22/2019.
 */
@Dao
interface AppointmentReminderDAO {

    @Query("SELECT * from appointment_reminder")
    fun getAllAppointments(): List<AppointmentReminder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(appointmentReminder: AppointmentReminder)

    @Query("DELETE FROM appointment_reminder WHERE appointmentId= :AppointmentReminderId")
    fun deleteAppointmentReminder(AppointmentReminderId: Int)

    @Query("UPDATE appointment_reminder SET eventURI = :eventURI  WHERE appointmentId= :appointmentReminderId")
    fun updateEventId(eventURI: String, appointmentReminderId: Int)

    @Query("SELECT eventURI from appointment_reminder where appointmentId = :appointmentId")
    fun getEventURI(appointmentId: Int): String

    @Query("SELECT * FROM appointment_reminder ORDER BY appointmentId DESC LIMIT 1")
    fun getLatestAppointment(): AppointmentReminder

    @Query("DELETE FROM appointment_reminder")
    fun deleteAll()
}