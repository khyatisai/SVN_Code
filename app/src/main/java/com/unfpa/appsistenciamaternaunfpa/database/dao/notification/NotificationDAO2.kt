package com.unfpa.appsistenciamaternaunfpa.database.dao.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification2

/**
 * Created by KhyatiShah on 12/16/2019.
 */
@Dao
interface NotificationDAO2 {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotification(notification: Notification2)

    @Query("SELECT * from appointment_notification_reminder")
    fun getAllNotificationAppointment(): List<Notification2>
}