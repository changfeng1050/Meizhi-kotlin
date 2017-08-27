package me.zjl.meizhi.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by chang on 2017-08-20.
 */
class RatioImageView : ImageView {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr)


    private var originalWidth: Int? = null
    private var originalHeight: Int? = null

    fun setOriginalSize(originalWidth: Int, originalHeight: Int) {
        this.originalWidth = originalWidth
        this.originalHeight = originalHeight
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (originalWidth != null && originalHeight != null && originalWidth!! > 0 && originalHeight!! > 0) {
            val ratio = (originalWidth!!.toFloat()) / (originalHeight!!)

            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = MeasureSpec.getSize(heightMeasureSpec)

            if (width > 0) {
                height = (width.toFloat() / ratio).toInt()
            }

            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}