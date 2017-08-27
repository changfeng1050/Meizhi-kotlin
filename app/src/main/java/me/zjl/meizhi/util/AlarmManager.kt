package me.zjl.meizhi.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import me.zjl.meizhi.service.AlarmReceiver
import org.jetbrains.anko.alarmManager
import java.util.*

/**
 * Created by chang on 2017-08-25.
 */
class AlarmManagers {

    companion object {
        fun register(context: Context) {
            val today = Calendar.getInstance()
            val now = Calendar.getInstance()

            today.set(Calendar.HOUR_OF_DAY, 12)
            today.set(Calendar.MINUTE, 24)
            today.set(Calendar.SECOND, 38)
            if (now.after(today)) {
                return
            }

            val intent = Intent("me.zjl.meizhi.alarm")
            intent.setClass(context, AlarmReceiver::class.java)
            val broadcast = PendingIntent.getBroadcast(context, 520, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            context.alarmManager.set(AlarmManager.RTC_WAKEUP, today.timeInMillis, broadcast)
        }
    }
}