package me.zjl.meizhi.data.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.enums.AssignType

import java.io.Serializable

/**
 * Created by chang on 2017-08-16.
 */

open class Soul : Serializable {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    var id: Long = 0
}
