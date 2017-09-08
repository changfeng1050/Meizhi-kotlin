package me.zjl.meizhi.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.squareup.otto.Subscribe
import me.zjl.meizhi.LoveBus
import me.zjl.meizhi.MeizhiFactory
import me.zjl.meizhi.R
import me.zjl.meizhi.data.GankData
import me.zjl.meizhi.data.entity.DGankData
import me.zjl.meizhi.data.entity.Gank
import me.zjl.meizhi.event.OnKeyBackClickEvent
import me.zjl.meizhi.ui.adapter.GankListAdapter
import me.zjl.meizhi.ui.base.BaseActivity
import me.zjl.meizhi.util.L
import me.zjl.meizhi.util.Once
import me.zjl.meizhi.widget.LoveVideoView
import me.zjl.meizhi.widget.VideoImageView
import me.zjl.smoothappbarlayout.SmoothAppBarLayout
import okhttp3.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.io.IOException

/**
 * Created by chang on 2017-08-27.
 */
class GankFragment : Fragment() {
    companion object {
        val TAG: String = L.makeLogTag(GankFragment::class.java)
        val ARG_YEAR = "year"
        val ARG_MONTH = "month"
        val ARG_DAY = "day"

        fun newInstance(year: Int, month: Int, day: Int): GankFragment {
            return GankFragment().withArguments(ARG_YEAR to year, ARG_MONTH to month, ARG_DAY to day)
        }
    }

    lateinit var recyclerView: RecyclerView
    lateinit var emptyViewStub: ViewStub
    lateinit var videoViewStub: ViewStub
    var videoImageView: VideoImageView? = null
    lateinit var videoView: LoveVideoView

    var year: Int = 2017
    var month: Int = 8
    var day: Int = 26

    lateinit var gankList: MutableList<Gank>

    var videoPreviewUrl: String? = null

    var isVideoViewInflated = false

    var subscription: Subscription? = null


    lateinit var adapter: GankListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gankList = mutableListOf()
        adapter = GankListAdapter(gankList)
        parseArgument()
        retainInstance = true
        setHasOptionsMenu(true)
    }

    private fun parseArgument() {
        year = arguments.getInt(ARG_YEAR)
        month = arguments.getInt(ARG_MONTH)
        day = arguments.getInt(ARG_DAY)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_gank, container, false)
        recyclerView = view.find(R.id.list)
        emptyViewStub = view.find(R.id.stub_empty_view)
        videoViewStub = view.find(R.id.stub_video_view)
        videoImageView = view.find(R.id.video_image)

        val headerAppbar: SmoothAppBarLayout = view.find(R.id.header_appbar)
        headerAppbar.onClick {
            resumeVideoView()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            if (gankList.firstOrNull()?.type == "休息视频") {
                longToast(R.string.loading)
            } else {
                closePlayer()
            }
        }

        initRecyclerView()
        setVideoViewPosition(resources.configuration)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (gankList.isEmpty()) {
            loadData()
        }
        if (videoPreviewUrl != null) {
            Glide.with(this).load(videoPreviewUrl!!).into(videoImageView)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(ctx)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }


    private fun loadData() {
        loadVideoPreview()
        subscription = BaseActivity.sGankIo
                .getGankData(year, month, day)
                .map { it.results }
                .map { addAllResults(it!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it!!.isEmpty()) {
                        showEmptyView()
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                }, {
                    L.e(TAG, "loadData()", it)
                })
    }

    private fun loadVideoPreview() {
        val where = "{\"tag\":\"$year-$month-$day\"}"
        MeizhiFactory.drakeet.getDGankData(where)
                .map { it.results }
                .single { it.size > 0 }
                .map { it[0] }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startPreview(it.preview)
                }, {
                    getOldVideoPreview(OkHttpClient())
                })
    }

    private fun getOldVideoPreview(client: OkHttpClient) {
        val url = "http://gank.io/${year}/${month}/${day}"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException?) {
                L.e(TAG, "getOldVideoPreview()", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body().string()
                videoPreviewUrl = getVideoPreviewImageUrl(body)
                startPreview(videoPreviewUrl)
            }
        })
    }

    private fun startPreview(preview: String?) {
        videoPreviewUrl = preview
        if (preview != null && videoImageView != null) {
            videoImageView!!.post {
                Glide.with(videoImageView!!.context)
                        .load(preview)
                        .into(videoImageView!!)
            }
        }
    }


    private fun showEmptyView() {
        emptyViewStub.inflate()
    }

    private fun addAllResults(results: GankData.Result): List<Gank> {
        with(results) {
            androidList?.let {
                gankList.addAll(it)
            }
            iOSList?.let {
                gankList.addAll(it)
            }
            appList?.let {
                gankList.addAll(it)
            }
            拓展资源List?.let {
                gankList.addAll(it)
            }
            瞎推荐List?.let {
                gankList.addAll(it)
            }
            休息视频?.let {
                gankList.addAll(it)
            }
        }
        return gankList
    }

    private fun setVideoViewPosition(newConfig: Configuration) {
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                if (isVideoViewInflated) {
                    videoViewStub.visibility = View.VISIBLE
                } else {
                    videoView = videoViewStub.inflate() as LoveVideoView
                    isVideoViewInflated = true

                    Once(videoView.context).show(R.string.tip_video_play) {
                        Snackbar.make(videoView, R.string.tip_video_play, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.i_know) {}
                                .show()
                    }
                }
                if (gankList.firstOrNull()?.type == "休息视频") {
                    videoView.loadUrl(gankList.first().url)
                }
            }
            else -> {
                videoViewStub.visibility = View.GONE
            }
        }
    }

    private fun closePlayer() {
        act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        toast(R.string.tip_for_no_gank)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        setVideoViewPosition(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    @Subscribe fun onKeyBackClick(event: OnKeyBackClickEvent) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        clearVideoView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val gank = gankList.firstOrNull()
                val shareText = if (gank != null) {
                    gank.desc + gank.url + getString(R.string.share_from)
                } else {
                    getString(R.string.share_text)
                }
                share(shareText)
                return true
            }
            R.id.action_subject -> {
                openTodaySubject()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openTodaySubject() {
        val url = "$year/$month/$day"
        val intent = WebActivity.newIntent(act, url, getString(R.string.action_subject))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        LoveBus.bus.register(this)
        resumeVideoView()
    }

    override fun onPause() {
        super.onPause()
        LoveBus.bus.unregister(this)
        pauseVideoView()
        clearVideoView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun pauseVideoView() {
        videoView.onPause()
        videoView.pauseTimers()
    }

    private fun resumeVideoView() {
        videoView.resumeTimers()
        videoView.onResume()
    }

    private fun clearVideoView() {
        videoView.clearHistory()
        videoView.clearCache(true)
        videoView.loadUrl("about:blank")
        videoView.pauseTimers()
    }


    fun getVideoPreviewImageUrl(resp: String): String? {
        val s0 = resp.indexOf("<h1>休息视频</h1>")
        if (s0 == -1) return null
        val s1 = resp.indexOf("<img", s0)
        if (s1 == -1) return null
        val s2 = resp.indexOf("http:", s1)
        if (s2 == -1) return null
        val e2 = resp.indexOf(".jpg", s2) + ".jpg".length
        if (e2 == -1) return null
        return try {
            resp.substring(s2, e2)
        } catch (e: StringIndexOutOfBoundsException) {
            e.printStackTrace()
            null
        }

    }


}