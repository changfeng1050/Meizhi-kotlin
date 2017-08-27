package me.zjl.meizhi

import com.google.gson.GsonBuilder
import me.zjl.meizhi.data.entity.Gank
import me.zjl.meizhi.ui.GankApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by chang on 2017-08-16.
 */
object MeizhiRetrofit {

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create()

    private val client:OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(12,TimeUnit.SECONDS)
            .build()

    private val builder = Retrofit.Builder()
            .baseUrl("http://gank.io/api/")
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))

    private val gankRest = builder.build()

    val gankService = gankRest.create(GankApi::class.java)

    private val drakeetRest = builder.baseUrl("https://leancloud.cn.443/1.1/classes/").build()

    val drakeetService= drakeetRest.create(DrakeetApi::class.java)


}