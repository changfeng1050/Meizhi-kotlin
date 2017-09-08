package me.zjl.meizhi.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.zjl.meizhi.MeizhiFactory
import me.zjl.meizhi.ui.GankFragment
import java.util.*

/**
 * Created by chang on 2017-08-27.
 */
class GankPagerAdapter(fm:FragmentManager, val date:Date):FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -position)

        return with(c){
        GankFragment.newInstance(
                get(Calendar.YEAR),
                get(Calendar.MONTH)+1,
                get(Calendar.DAY_OF_MONTH)
        )}
    }


    override fun getCount(): Int {
        return MeizhiFactory.gankSize
    }
}