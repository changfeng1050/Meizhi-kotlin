package me.zjl.meizhi.util

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import me.zjl.headsup.HeadsUp
import me.zjl.headsup.HeadsUpManager

/**
 * Created by chang on 2017-08-27.
 */
object HeadsUps {

    fun show(context: Context, targetActivity: Class<*>, title: String, content: String, largeIcon: Int, smallIcon: Int, code: Int) {
        val pendingIntent = PendingIntent.getActivity(context, 11,
                Intent(context, targetActivity), PendingIntent.FLAG_UPDATE_CURRENT)
        val manage = HeadsUpManager.getInstant(context)
        val builder = HeadsUp.Builder(context)
        builder.setContentTitle(title)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, false)
                .setAutoCancel(true)
                .setContentText(content)

        if (Build.VERSION.SDK_INT >= 21) {
            builder.setLargeIcon(
                    BitmapFactory.decodeResource(context.resources, largeIcon))
                    .setSmallIcon(smallIcon)
        } else {
            builder.setSmallIcon(largeIcon)
        }

        val headsUp = builder.buildHeadUp()
        headsUp.isSticky = true
        manage.notify(code, headsUp)
    }
}