package me.zjl.meizhi.ui

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.litesuits.orm.db.assit.QueryBuilder
import com.litesuits.orm.db.model.ConflictAlgorithm
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import me.zjl.meizhi.App
import me.zjl.meizhi.R
import me.zjl.meizhi.data.MeizhiData
import me.zjl.meizhi.data.entity.Gank
import me.zjl.meizhi.data.entity.Meizhi
import me.zjl.meizhi.data.休息视频Data
import me.zjl.meizhi.func.OnMeizhiTouchListener
import me.zjl.meizhi.ui.adapter.MeizhiListAdapter
import me.zjl.meizhi.ui.base.SwipeRefreshBaseActivity
import me.zjl.meizhi.util.*
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class MainActivity : SwipeRefreshBaseActivity() {

    companion object {
        val TAG: String = L.makeLogTag(MainActivity::class.java)
        val PRELOAD_SIZE = 6
    }

    private var notifiableChecked by Preference(this, "notifiable_checked", false)

    private lateinit var meizhiListAdapter: MeizhiListAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var meizhiList: MutableList<Meizhi>

    private var page = 1

    private var size = 10

    private var meizhiBeTouched = false
    private var isFirstTimeTouchBottom = true
    private var lastVideoIndex = 0

    override fun provideContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        recyclerView = find(R.id.list)

//        val fab = find<FloatingActionButton>(R.id.main_fab)

//        fab.onClick {
//            if (meizhiList.isNotEmpty()) {
//                startGankActivity(meizhiList.first().publishedAt!!)
//            }
//        }

        meizhiList = mutableListOf()

        val query = QueryBuilder(Meizhi::class.java)
        query.appendOrderDescBy("publishedAt")
        query.limit(0, 10)
        L.i(TAG, "onCreate() query:$query meizhiList:$meizhiList db:${App.sDb} result:${App.sDb?.query(query)}")
        meizhiList.addAll(App.sDb!!.query(query))

        setupRecyclerView()
        AlarmManagers.register(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        Handler().postDelayed({
            setRefresh(true)
        }, 358)
        loadData(true)
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        meizhiListAdapter = MeizhiListAdapter(this, meizhiList)

        recyclerView.adapter = meizhiListAdapter

        Once(this).show("tip_guide_6") {
            Snackbar.make(recyclerView, getString(R.string.tip_guide), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.i_know) {

                    }.show()
        }

        recyclerView.addOnScrollListener(getOnBottomListener(layoutManager))
        meizhiListAdapter.onMeizhiTouchListener = onMeizhiTouchListener

    }

    private fun loadData(clean: Boolean) {
        lastVideoIndex = 0
        val s = Observable
                .zip(sGankIo.getMeizhiData(size, page),
                        sGankIo.get休息视频Data(size, page),
                        this::createMeizhiDataWith休息视频Desc)
                .map { t: MeizhiData? ->
                    t!!.results
                }.flatMap { Observable.from(it) }
                .toSortedList { t1, t2 ->
                    t2.publishedAt!!.compareTo(t1.publishedAt)
                }.doOnNext { saveMeizhis(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo { setRefresh(false) }
                .subscribe(
                        {
                            if (clean) {
                                meizhiList.clear()
                            }
                            meizhiList.addAll(it)
                            meizhiListAdapter.notifyDataSetChanged()
                            setRefresh(false)
                        },
                        {
                            loadError(it)
                        }
                )

    }

    private fun loadError(throwable: Throwable) {
        L.e(TAG, "loadError()", throwable)
        Snackbar.make(recyclerView, R.string.snap_load_fail, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) {
                    requestDataRefresh()
                }
                .show()
    }

    private fun saveMeizhis(meizhis: List<Meizhi>) {
        App.sDb!!.insert(meizhis, ConflictAlgorithm.Replace)
    }

    private fun createMeizhiDataWith休息视频Desc(data: MeizhiData, love: 休息视频Data): MeizhiData {

        L.i(TAG, "createMeizhiDataWith休息视频Desc() data:${MyGson.gson.toJson(data)}")
        L.i(TAG, "createMeizhiDataWith休息视频Desc() data:${MyGson.gson.toJson(love)}")

        data.results!!.onEach { it.desc = it.desc + " " + getFirstVideoDesc(it.publishedAt!!, love.results!!) }
        return data
    }

    private fun getFirstVideoDesc(publishedAt: Date, results: List<Gank>): String {

        return try {
            results.filterIndexed { index, _ ->
                index >= lastVideoIndex
            }.first {
                publishedAt inTheSameDateWith (if (it.publishedAt == null) it.createdAt!! else it.publishedAt!!)
            }.desc ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun loadData() {
        loadData(false)
    }

    private val onMeizhiTouchListener = object : OnMeizhiTouchListener {
        override fun onTouch(v: View, meizhiView: View, card: View, meizhi: Meizhi) {
            if (v == meizhiView && !meizhiBeTouched) {
                meizhiBeTouched = true
                Picasso.with(this@MainActivity).load(meizhi.url).fetch(object : Callback {
                    override fun onSuccess() {
                        meizhiBeTouched = false
                        startPictureActivity(meizhi, meizhiView)
                    }

                    override fun onError() {
                        meizhiBeTouched = false
                    }
                })
            } else if (v == card) {
                startGankActivity(meizhi.publishedAt!!)
            }
        }
    }

    private fun startGankActivity(publishedAt: Date) {
        startActivity(intentFor<GankActivity>(GankActivity.EXTRA_GANK_DATE to publishedAt))
    }


    private fun startPictureActivity(meizhi: Meizhi, transitView: View) {
        val intent = PictureActivity.newIntent(this@MainActivity, meizhi.url!!, meizhi.desc!!)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, transitView, PictureActivity.TRANSIT_PIC)
        try {
            ActivityCompat.startActivity(this@MainActivity, intent, optionsCompat.toBundle())
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            startActivity(intent)
        }
    }

    override fun onToolbarClick() {
        recyclerView.smoothScrollToPosition(0)
    }

    override fun requestDataRefresh() {
        super.requestDataRefresh()
        page = 1
        loadData(true)
    }

    private fun openGithubTrending() {
        val url = getString(R.string.url_github_trending)
        val title = getString(R.string.action_github_trending)
        val intent = WebActivity.newIntent(this, url, title)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu.findItem(R.id.action_notifiable)
        initNotifiableItemState(item)
        return true
    }

    private fun initNotifiableItemState(item: MenuItem) {
        item.isChecked = notifiableChecked
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_tending -> {
                openGithubTrending()
                true
            }
            R.id.action_notifiable -> {
                val isChecked = !item.isChecked
                item.isChecked = isChecked
                notifiableChecked = isChecked
                toast(if (isChecked) getString(R.string.notifiable_on) else getString(R.string.notifiable_off))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getOnBottomListener(layoutManager: StaggeredGridLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isBottom = layoutManager.findLastCompletelyVisibleItemPositions(kotlin.IntArray(2))[1] >= meizhiListAdapter.itemCount - PRELOAD_SIZE
                if (swipeRefreshLayout!!.isRefreshing && isBottom) {
                    if (isFirstTimeTouchBottom) {
                        swipeRefreshLayout!!.isRefreshing = true
                        page += 1
                        loadData()
                    } else {
                        isFirstTimeTouchBottom = false
                    }
                }
            }
        }
    }

}
