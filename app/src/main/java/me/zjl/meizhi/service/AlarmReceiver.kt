package me.zjl.meizhi.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.zjl.meizhi.R
import me.zjl.meizhi.ui.MainActivity
import me.zjl.meizhi.util.HeadsUps
import me.zjl.meizhi.util.PreferencesLoader

/**
 * Created by chang on 2017-08-25.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val loader = PreferencesLoader(p0!!)
        if (loader.getBoolean(R.string.action_notifiable, true)) {
            HeadsUps.show(p0, MainActivity::class.java,
                    p0.getString(R.string.heads_up_title),
                    p0.getString(R.string.heads_up_content),
                    R.mipmap.ic_meizhi_150602, R.mipmap.ic_female, 123123
                    )
        }
    }
}