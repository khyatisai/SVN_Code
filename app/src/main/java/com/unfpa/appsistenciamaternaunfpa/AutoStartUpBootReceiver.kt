package com.unfpa.appsistenciamaternaunfpa

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService


class AutoStartUpBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, callService::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)

    }
}