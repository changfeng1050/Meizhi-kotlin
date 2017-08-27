package me.zjl.meizhi.data

import com.google.gson.annotations.SerializedName

import me.zjl.meizhi.data.entity.Gank

/**
 * Created by chang on 2017-08-16.
 */

class GrankData : BaseData() {
    var results: Result? = null
    var category: List<String>? = null

    class Result {
        @SerializedName("Android")
        var androidList: List<Gank>? = null
        @SerializedName("休息视频")
        var 休息视频: List<Gank>? = null
        @SerializedName("iOS")
        var iOSList: List<Gank>? = null
        @SerializedName("福利")
        var 妹纸List: List<Gank>? = null
        @SerializedName("拓展资源")
        var 拓展资源List: List<Gank>? = null
        @SerializedName("瞎推荐")
        var 瞎推荐List: List<Gank>? = null
        @SerializedName("App")
        var appList: List<Gank>? = null
    }
}
