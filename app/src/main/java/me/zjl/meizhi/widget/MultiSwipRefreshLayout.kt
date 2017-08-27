package me.zjl.meizhi.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import me.zjl.meizhi.R

/**
 * Created by chang on 2017-08-17.
 */
class MultiSwipRefreshLayout : SwipeRefreshLayout {

    private var canChildScrollUPCallBack: CanChildScrollUpCallback? = null
    private var foregroundDrawable: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr) {
        val a = context.obtainStyledAttributes(attr, R.styleable.MultiSwipeRefreshLayout, 0, 0)
        foregroundDrawable = a.getDrawable(R.styleable.MultiSwipeRefreshLayout_foreground)
        foregroundDrawable?.callback = this
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        foregroundDrawable?.setBounds(0, 0, w, h)
    }

    fun setCanChildScrollUpCallback(callback: CanChildScrollUpCallback) {
        canChildScrollUPCallBack = callback
    }

    override fun canChildScrollUp(): Boolean {
        return canChildScrollUPCallBack?.canSwipeRefreshChildScrollUp() ?: super.canChildScrollUp()
    }

    interface CanChildScrollUpCallback {
        fun canSwipeRefreshChildScrollUp(): Boolean
    }


    interface SwipeRefreshLayer{
        fun requestDataRefresh()

        fun setRefresh(refresh: Boolean)

        fun setProgressViewOffset(scale:Boolean, start:Int, end:Int)

        fun setCanChildScrollUpCallback(callback: CanChildScrollUpCallback)
    }
}