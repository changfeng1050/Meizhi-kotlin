package me.zjl.meizhi.ui.base

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import me.zjl.meizhi.R
import me.zjl.meizhi.util.L
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by chang on 2017-08-17.
 */
abstract class ToolBarActivity : BaseActivity() {
    companion object {
        val TAG: String = L.makeLogTag(ToolBarActivity::class.java)
    }

    abstract fun provideContentViewId(): Int
    open fun canBack(): Boolean {
        return false
    }

    protected open fun onToolbarClick(){}

    protected var appBar: AppBarLayout? = null
    protected var toolBar: Toolbar? = null
    protected var isHidden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideContentViewId())
        appBar = try {
            find(R.id.app_bar_layout)
        } catch (e: Exception) {
            L.e(TAG, "onCreate()", e)
            null
        }
        toolBar = try {
            find(R.id.toolbar)
        } catch (e: Exception) {
            L.e(TAG, "onCreate()", e)
            null
        }

        if (toolBar == null || appBar == null) {
            throw IllegalAccessException("The subclass of ToolbarActivity must contain a toolbar.")
        }
        toolBar!!.onClick {
            onToolbarClick()
        }

        setSupportActionBar(toolBar)

        if (canBack()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar!!.elevation = 10.06f
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId == R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    protected fun setAppBarAlpha(alpha: Float) {
        appBar!!.alpha = alpha
    }

    protected fun hideOrShowToolBar() {
        appBar!!.animate()
                .translationY(if (isHidden) 0f else (0 - appBar!!.height).toFloat())
                .setInterpolator(DecelerateInterpolator(2f))
                .start()

        isHidden = !isHidden
    }
}