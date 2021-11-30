package com.unfpa.appsistenciamaternaunfpa.database.dao.my_health

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder

/**
 * Created by KhyatiShah on 10/16/2019.
 */
@Dao
interface MedicineReminderDAO {

    @Query("SELECT * from medicine_reminder")
    fun getAllMedicine(): List<MedicineReminder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(medicineReminder: MedicineReminder)

    @Query("UPDATE medicine_reminder SET eventId = :eventId, eventURI = :eventURI  WHERE medicineReminderId= :medicineReminderId")
    fun upEventId(eventId: Long, eventURI: String, medicineReminderId: Int)

    @Query("DELETE FROM medicine_reminder WHERE medicineReminderId= :medicineReminderId")
    fun deleteMedicineReminder(medicineReminderId: Int)

    @Query("SELECT eventURI from medicine_reminder where eventId = :eventId")
    fun getEventURI(eventId: Long): String

    @Query("SELECT * FROM medicine_reminder ORDER BY medicineReminderId DESC LIMIT 1")
    fun getLatestReminder(): MedicineReminder

    @Query("DELETE FROM medicine_reminder")
    fun deleteAll()
}