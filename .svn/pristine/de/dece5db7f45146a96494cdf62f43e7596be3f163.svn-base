package com.unfpa.appsistenciamaternaunfpa.database.dao.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification

/**
 * Created by KhyatiShah on 12/16/2019.
 */
@Dao
interface NotificationDAO {

    @Query("SELECT * from notification Where isDisplayed = 1")
    fun getAllNotification(): List<Notification>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(notification: Notification): Long

    @Query("SELECT * from notification where notificationId = :notificationId")
    fun getNotificationById(notificationId: Int): Notification

    @Query("SELECT * from notification where notificationItemId = :notificationItemId AND type = :notificationType")
    fun getNotificationByReminderItemId(notificationItemId: Int, notificationType: String): List<Notification>

    @Query("UPDATE notification SET isDisplayed = 1  WHERE notificationId= :notificationId")
    fun updateNotificationDisplayFlag(notificationId: Int)

    @Query("UPDATE notification SET notificationTime = :notificationTime  WHERE notificationId= :notificationId")
    fun updateNotificationTime(notificationId: Int, notificationTime: String)

    @Query("UPDATE notification SET notificationReqId = :notificationReqId  WHERE notificationId= :notificationId")
    fun updateNotificationReqId(notificationId: Int, notificationReqId: String)

    @Query("DELETE FROM notification WHERE  notificationId = :notificationId")
    fun deleteNotification(notificationId: Int)
}