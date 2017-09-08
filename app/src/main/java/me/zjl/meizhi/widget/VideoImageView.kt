package me.zjl.meizhi.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by chang on 2017-08-27.
 */
class VideoImageView : ImageView, Animator.AnimatorListener {

    private var scale = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr){
        nextAnimation()
    }


    private fun nextAnimation() {
        val anim = AnimatorSet()
        if (scale) {
            anim.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 1.5f, 1f),
                    ObjectAnimator.ofFloat(this, "scaleY", 1.5f,1f))
        } else {
            anim.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.5f),
                    ObjectAnimator.ofFloat(this, "scaleY", 1f,1.5f))
        }

        anim.duration = 10987
        anim.addListener(this)
        anim.start()
        scale = !scale
    }


    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
        nextAnimation()
    }

    override fun onAnimationRepeat(p0: Animator?) {

    }

    override fun onAnimationStart(p0: Animator?) {

    }

}