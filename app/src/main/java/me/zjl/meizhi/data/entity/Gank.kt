package me.zjl.meizhi.data.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.Table
import java.util.*

/**
 * Created by chang on 2017-08-16.
 */

@Table("ganks")
class Gank : Soul() {
    @Column("url")
    var url: String? = null
    @Column("type")
    var type: String? = null
    @Column("dest")
    var desc: String? = null
    @Column("who")
    var who: String? = null
    @Column("used")
    var used: Boolean = false
    @Column("createdAt")
    var createdAt: Date? = null
    @Column("updatedAt")
    var updatedAt: Date? = null
    @Column("publishedAt")
    var publishedAt: Date? = null
}
