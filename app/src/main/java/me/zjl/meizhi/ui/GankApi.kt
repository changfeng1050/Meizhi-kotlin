package me.zjl.meizhi.ui

import me.zjl.meizhi.data.GankData
import me.zjl.meizhi.data.MeizhiData
import me.zjl.meizhi.data.休息视频Data
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by chang on 2017-08-15.
 */

interface GankApi {
    @GET("data/福利/{size}/{page}")
    fun getMeizhiData(@Path("size") size: Int, @Path("page") page: Int): Observable<MeizhiData>

    @GET("day/{year}/{month}/{day}")
    fun getGankData(@Path("year") year:Int, @Path("month")month:Int, @Path("day") day:Int):Observable<GankData>

    @GET("data/休息视频/{size}/{page}")
    fun get休息视频Data(@Path("size")size:Int, @Path("page")page:Int):Observable<休息视频Data>

}
