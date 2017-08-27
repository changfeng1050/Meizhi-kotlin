package me.zjl.meizhi.util

import android.content.Context

/**
 * Created by chang on 2017-08-17.
 */
class Once(private val context: Context) {

    private val pref by lazy {
        context.getSharedPreferences("once", Context.MODE_PRIVATE)
    }

    fun show(key: String, action: () -> Unit) {
        val isSecondTime = pref.getBoolean(key, false)
        if (!isSecondTime) {
            action()
            val editor = pref.edit()
            editor.putBoolean(key, true)
            editor.apply()

            mutableListOf<String>().forEach { }
        }
    }

    fun show(keyResId: Int, action: () -> Unit) {
        show(context.getString(keyResId), action)
    }

}