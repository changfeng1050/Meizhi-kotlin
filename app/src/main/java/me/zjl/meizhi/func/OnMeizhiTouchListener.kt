package me.zjl.meizhi.func

import android.view.View
import me.zjl.meizhi.data.entity.Meizhi

/**
 * Created by chang on 2017-08-20.
 */
interface OnMeizhiTouchListener {
    fun onTouch(v:View, meizhiView:View, card:View, meizhi:Meizhi)
}