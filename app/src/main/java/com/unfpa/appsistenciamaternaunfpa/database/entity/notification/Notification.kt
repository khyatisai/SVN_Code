package com.unfpa.appsistenciamaternaunfpa.database.entity.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KhyatiShah on 12/16/2019.
 */
@Entity(tableName = "notification")
data class Notification(

    var title: String,
    var subTitile1: String,
    var subTitile2: String,
    var timeStr: String,
    var type: String,
    var notificationTime: String,
    var isDisplayed: Boolean,
    var notificationItemId: Int = 0,
    var notificationReqId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var notificationId: Int = 0
}