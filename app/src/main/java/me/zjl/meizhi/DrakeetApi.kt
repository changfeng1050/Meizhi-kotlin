package me.zjl.meizhi

import me.zjl.meizhi.data.entity.DGankData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rx.Observable

/**
 * Created by chang on 2017-08-16.
 */
interface DrakeetApi {
    @Headers("X-LC-Id: 0azfScvBLCC9tAGRAwIhcC40", "X-LC-Key: gAuE93qAusvP8gk1VW8DtOUb", "Content-Type: application/json")
    @GET("Gank?limit=1")
    abstract fun getDGankData(
            @Query("where") where: String): Observable<DGankData> // format {"tag":"2015-11-10"}
}