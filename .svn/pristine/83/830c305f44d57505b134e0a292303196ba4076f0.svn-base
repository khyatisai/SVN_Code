package com.unfpa.appsistenciamaternaunfpa.database.dao.my_health

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineEvent

/**
 * Created by KhyatiShah on 10/21/2019.
 */
@Dao
interface MedicineEventDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(medicineEvent: MedicineEvent)

    @Query("SELECT * from medicine_event where medicineReminderId= :medicineReminderId")
    fun getAllEventByReminderId(medicineReminderId: Int): List<MedicineEvent>

    @Query("DELETE FROM medicine_event")
    fun deleteAll()
}