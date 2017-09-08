package me.zjl.meizhi.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import me.zjl.meizhi.R

/**
 * Created by chang on 2017-08-27.
 */
open class AnimRecyclerViewAdapter<T:RecyclerView.ViewHolder>:RecyclerView.Adapter<T>() {

    companion object {
        val DELAY = 138
    }
    var lastPos=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T ?{
        return null
    }

    override fun onBindViewHolder(holder: T, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }


    fun showItemAnim(view: View, pos: Int) {
       val context = view.context
        if (pos > lastPos) {
            view.alpha = 0f
            view.postDelayed({
                val a = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)

                a.setAnimationListener(object :Animation.AnimationListener{
                    override fun onAnimationStart(p0: Animation?) {
                        view.alpha =1f
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }
                })

            }, DELAY * pos.toLong())

            lastPos = pos
        }
    }

}