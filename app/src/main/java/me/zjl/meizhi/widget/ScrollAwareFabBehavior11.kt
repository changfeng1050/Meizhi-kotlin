package me.zjl.meizhi.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by chang on 2017-09-19.
 */
class ScrollAwareFabBehavior11 : FloatingActionButton.Behavior {

    companion object {
        val TAG: String = "ScrollAwareFabBehavior"
        val INTERPOLATOR = FastOutSlowInInterpolator()
    }

    var isAnimatingOut = false

    constructor(context: Context, attrs: AttributeSet) : super()

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && !isAnimatingOut && child.visibility == View.VISIBLE) {
            animateOut(child)
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            animateIn(child)
        }
        Log.i(TAG, "onNestedScroll() dyConsumed:$dyConsumed ${child.visibility}")
    }
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0 && !isAnimatingOut && child.visibility == View.VISIBLE) {
            animateOut(child)
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            animateIn(child)
        }
        Log.i(TAG, "onNestedScroll() dyConsumed:$dyConsumed ${child.visibility}")
    }

    private fun animateOut(button: FloatingActionButton) {
        ViewCompat.animate(button)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {
                        isAnimatingOut = true
                    }

                    override fun onAnimationCancel(view: View) {
                        isAnimatingOut = false
                    }

                    override fun onAnimationEnd(view: View) {
                        isAnimatingOut = false
                        view.visibility = View.GONE
                    }
                }).start()

    }

    private fun animateIn(button: FloatingActionButton) {
        Log.i(TAG, "animateIn()")
        button.visibility = View.VISIBLE
        ViewCompat.animate(button)
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(null)
                .start()

    }
}