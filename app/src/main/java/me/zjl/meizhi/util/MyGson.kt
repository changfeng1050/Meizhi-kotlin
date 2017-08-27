package me.zjl.meizhi.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by chang on 2017-08-22.
 */
object MyGson {
    val gson:Gson = GsonBuilder().serializeNulls().create()
}