package com.unfpa.appsistenciamaternaunfpa.utils.notification

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.os.Build
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.Constant


/**
 * Created by KhyatiShah on 12/17/2019.
 */
class OpenAppNotifyWorker(
    appContext: Context, workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    val context: Context? = appContext
   
    @NonNull
    override fun doWork(): Result {

        val reminderId = inputData.getInt(Constant.NOTIFICATION_ITEM_ID, 0)
        val notificationId = inputData.getInt(Constant.NOTIFICATION_ID, 0)

        val notificationDAO =
            MhealthRoomDatabase.getAppDataBase(this!!.context!!)!!.notificationDAO()
        val notification = notificationDAO.getNotificationById(notificationId)
        //update notification status
        notificationDAO.updateNotificationDisplayFlag(notificationId)

        val service = Intent(context, NotificationService::class.java)
        service.putExtra(Constant.NOTIFICATION_TYPE, notification.type)
        service.putExtra(Constant.NOTIFICATION_TITLE, notification.title)
        service.putExtra(Constant.NOTIFICATION_SUB_TITLE, notification.subTitile1)
        // context!!.startService(service)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(service)
        } else {
            context!!.startService(service)
        }
        return Result.success()
    }

    /*companion object {
        val WORKER_NAME = "OpenAppNotifyWorker"
        val NOTIFY_TO_OPEN_APP_IN_DAYS = 7
    }*/
}