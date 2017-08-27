package me.zjl.meizhi

import android.app.Application
import com.litesuits.orm.LiteOrm

/**
 * Created by chang on 2017-08-20.
 */
class App:Application(){

    companion object {
        val DB_NAME = "gank.db"
        var sDb:LiteOrm?=null
    }

    override fun onCreate() {
        super.onCreate()
        sDb = LiteOrm.newSingleInstance(this, DB_NAME)
        if (BuildConfig.DEBUG) {
            sDb!!.setDebugged(true)
        }
    }
}