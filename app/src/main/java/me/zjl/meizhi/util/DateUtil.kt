package me.zjl.meizhi.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chang on 2017-08-20.
 */

fun Date.toDate(): String {
    return SimpleDateFormat("yyyy/MM/dd").format(this)
}

fun Date.toDate(add: Int): String {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.DATE, add)
    return c.time.toDate()
}

fun Date.lastDate(): String {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.DATE, -1)
    return c.time.toDate()
}

fun Date.nextDate(): String {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.DATE, 1)
    return c.time.toDate()
}

infix fun Date.inTheSameDateWith(another: Date): Boolean {
    val _one = Calendar.getInstance()
    _one.time = this
    val _another = Calendar.getInstance()
    _another.time = another
    val oneDay = _one.get(Calendar.DAY_OF_YEAR)
    val anotherDay = _another.get(Calendar.DAY_OF_YEAR)
    return oneDay == anotherDay
}
