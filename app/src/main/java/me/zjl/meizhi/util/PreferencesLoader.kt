package me.zjl.meizhi.util

import android.content.Context
import android.preference.PreferenceManager
import org.jetbrains.anko.defaultSharedPreferences

/**
 * Created by chang on 2017-08-25.
 */
class PreferencesLoader(val context: Context) {

    val pref = context.defaultSharedPreferences


    fun svaeBoolean(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value)
                .apply()
    }


    fun getBoolean(key: String) = pref.getBoolean(key, false)
    fun getBoolean(keyResId: Int) = getBoolean(context.getString(keyResId))

    fun getBoolean(key: String, def: Boolean) = pref.getBoolean(key, def)

    fun getBoolean(keyResId: Int, def: Boolean): Boolean = getBoolean(context.getString(keyResId), def)


}