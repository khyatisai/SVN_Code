package com.unfpa.appsistenciamaternaunfpa.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by KhyatiShah on 12/17/2019.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))

        context.startService(service)
    }

}