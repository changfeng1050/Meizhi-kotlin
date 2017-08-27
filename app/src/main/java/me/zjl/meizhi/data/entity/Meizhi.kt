package me.zjl.meizhi.data.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.Table

import java.util.Date

/**
 * Created by chang on 2017-08-16.
 */

@Table("meizhis")
class Meizhi : Soul() {
    @Column("url")
    var url: String? = null
    @Column("type")
    var type: String? = null
    @Column("desc")
    var desc: String? = null
    @Column("used")
    var used: Boolean? = null
    @Column("createdAt")
    var createdAt: Date? = null
    @Column("updatedAt")
    var updatedAt: Date? = null
    @Column("publishedAt")
    var publishedAt: Date? = null
    @Column("imageWidth")
    var imageWidth: Date? = null
    @Column("imageHeight")
    var imageHeight: Date? = null
}
