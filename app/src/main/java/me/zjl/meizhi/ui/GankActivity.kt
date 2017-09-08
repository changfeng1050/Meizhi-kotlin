package me.zjl.meizhi.ui

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import me.zjl.meizhi.LoveBus
import me.zjl.meizhi.R
import me.zjl.meizhi.event.OnKeyBackClickEvent
import me.zjl.meizhi.ui.adapter.GankPagerAdapter
import me.zjl.meizhi.ui.base.ToolbarActivity
import me.zjl.meizhi.util.L
import me.zjl.meizhi.util.toDate
import org.jetbrains.anko.find
import java.util.*

/**
 * Created by chang on 2017-08-27.
 */
class GankActivity : ToolbarActivity(), ViewPager.OnPageChangeListener {
    companion object {
        val TAG: String = L.makeLogTag(GankActivity::class.java)
        val EXTRA_GANK_DATE = "gank_date"
    }

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    lateinit var pageAdapter: GankPagerAdapter

    lateinit var gankDate: Date

    override fun provideContentViewId() = R.layout.activity_gank

    override fun canBack() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gankDate = intent.getSerializableExtra(EXTRA_GANK_DATE) as Date
        title = gankDate.toDate()
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        viewPager = find(R.id.pager)
        pageAdapter = GankPagerAdapter(supportFragmentManager, gankDate)
        viewPager.adapter = pageAdapter
        viewPager.offscreenPageLimit = 1
        viewPager.addOnPageChangeListener(this)
    }

    private fun initTabLayout() {
        tabLayout = find(R.id.tab_layout)
        (0 until pageAdapter.count).forEach {
            tabLayout.addTab(tabLayout.newTab())
        }

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideOrShowToolbar()
        } else {
            hideOrShowToolbar()
        }
    }

    override fun hideOrShowToolbar() {
        val toolbar = find<Toolbar>(R.id.toolbar_with_indicator)
        toolbar.animate()
                .translationY(if (isHidden) 0.toFloat() else -toolbar.height.toFloat())
                .setInterpolator(DecelerateInterpolator(2f))
                .start()
        isHidden = !isHidden
        if (isHidden) {
            viewPager.tag = viewPager.paddingTop
            viewPager.setPadding(0, 0, 0, 0)
        } else {
            viewPager.setPadding(0, viewPager.tag as Int, 0, 0)
            viewPager.tag = null
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    LoveBus.bus.post(OnKeyBackClickEvent())
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gank, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        LoveBus.bus.register(this)
    }

    override fun onPause() {
        super.onPause()
        LoveBus.bus.unregister(this)
    }

    override fun onDestroy() {
        viewPager.removeOnPageChangeListener(this)
        super.onDestroy()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        title = gankDate.toDate(-position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }




}