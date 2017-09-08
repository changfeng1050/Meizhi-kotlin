package me.zjl.meizhi.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import me.zjl.meizhi.R
import me.zjl.meizhi.util.L
import me.zjl.meizhi.widget.MultiSwipRefreshLayout

/**
 * Created by chang on 2017-08-17.
 */
abstract class SwipeRefreshBaseActivity : ToolbarActivity(),MultiSwipRefreshLayout.SwipeRefreshLayer {
    companion object {
        val TAG:String= L.makeLogTag(SwipeRefreshBaseActivity::class.java)
    }

    var swipeRefreshLayout:MultiSwipRefreshLayout ?= null

    private var isRequestDataRefresh = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
    }

    private fun trySetupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_1)

            swipeRefreshLayout!!.setOnRefreshListener {
               requestDataRefresh()
            }
        }
    }

    override fun setRefresh(requestDataRefresh: Boolean) {
        if (swipeRefreshLayout == null) {
            return
        }

        if (!requestDataRefresh) {
            isRequestDataRefresh =false
            swipeRefreshLayout?.postDelayed({
                swipeRefreshLayout?.isRefreshing = false
            }, 1000)
        } else {
            swipeRefreshLayout?.isRefreshing =true
        }
    }

    override fun requestDataRefresh() {
        isRequestDataRefresh=true
    }

    override fun setProgressViewOffset(scale: Boolean, start: Int, end: Int) {
        swipeRefreshLayout?.setProgressViewOffset(scale, start,end)
    }

    override fun setCanChildScrollUpCallback(callback: MultiSwipRefreshLayout.CanChildScrollUpCallback) {
        swipeRefreshLayout?.setCanChildScrollUpCallback(callback)
    }



}